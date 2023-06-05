#!/usr/bin/env bash

set -eu

LOCAL=0
while getopts "l" opt; do
  case ${opt} in
  l)
    LOCAL=1
    ;;
  *)
    usage
    exit 1
    ;;
  esac
done

echo -e "Resetting di-authentication-acceptance-tests test data..."

export AWS_REGION=eu-west-2
export ENVIRONMENT_NAME=build
export GDS_AWS_ACCOUNT=digital-identity-dev

if [ $LOCAL == "1" ]; then
  export $(grep -v '^#' .env | xargs)
fi

echo -e "Getting AWS credentials ..."
eval "$(gds aws ${GDS_AWS_ACCOUNT} -e)"
echo "done!"

function deleteUser() {
  echo "Truncating test user data in user-profile: $1"
  aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --region "${AWS_REGION}"

  echo "Truncating test user data in user-credentials: $1"
  aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --region "${AWS_REGION}"
}

function resetTermsAndConditions() {
  echo "resetting terms and conditions version for test user in user-profile: $1"
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #TC = :vtc" \
    --expression-attribute-names "{ \"#TC\": \"termsAndConditions\" }" \
    --expression-attribute-values "{ \":vtc\": { \"M\": { \"version\": { \"S\": \"1.0\"}, \"timestamp\": {\"S\": \"1970-01-01T00:00:00.000000\"} } } }" \
    --region "${AWS_REGION}"
}

function resetPassword() {
  echo "resetting password for reset password test user in user-credentials: $1"
  export HASHED_PASSWORD_RESET=$(echo -n "$2" | argon2 $(openssl rand -hex 32) -e -id -v 13 -k 15360 -t 2 -p 1)
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #PW = :vpw" \
    --expression-attribute-names "{ \"#PW\": \"Password\" }" \
    --expression-attribute-values "{ \":vpw\": { \"S\": \"${HASHED_PASSWORD_RESET}\"} }" \
    --region "${AWS_REGION}"
}

function updateAccountRecoveryBlock() {
  up="$(
    aws dynamodb get-item \
      --table-name "${ENVIRONMENT_NAME}-user-profile" \
      --key "{\"Email\": {\"S\": \"$1\"}}" \
      --projection-expression "#E, #ST, #S, #PS, #LS" \
      --expression-attribute-names "{\"#E\": \"Email\", \"#ST\": \"salt\", \"#S\": \"SubjectID\", \"#PS\": \"PublicSubjectID\", \"#LS\": \"LegacySubjectId\"}" \
      --region "${AWS_REGION}" \
      --no-paginate
  )"

  sectorHost="identity.build.account.gov.uk"
  ics="$(echo -n $up | jq -r '.Item.SubjectID.S')"
  salt="$(echo -n $up | jq -r '.Item.salt.B' | base64 -d)"
  digest="$(echo -n "$sectorHost$ics$salt" | openssl dgst -sha256 -binary | base64 | tr '/+' '_-' | tr -d '=')"
  pwid="urn:fdc:gov.uk:2022:$digest"

  echo "resetting account block for: $1 internalCommonSubjectId: $pwid"

  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-account-modifiers" \
    --key "{\"InternalCommonSubjectIdentifier\": {\"S\": \"$pwid\"}}" \
    --update-expression "SET #AR = :var" \
    --expression-attribute-names "{ \"#AR\": \"AccountRecovery\" }" \
    --expression-attribute-values "{ \":var\": { \"M\": { \"Blocked\": { \"BOOL\": false}, \"Created\": {\"S\": \"1970-01-01T00:00:00.000000\"}, \"Updated\": {\"S\": \"1970-01-01T00:00:00.000000\"} } } }" \
    --region "${AWS_REGION}"
}

function removeMfaMethods() {
  echo "Removing mfa methods for user $1 in user-credentials..."
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "REMOVE MfaMethods" \
    --region "${AWS_REGION}"
}

function updateMfaSMS() {
  echo "updating mfa SMS in user-profile: $1"
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #AV = :vav, #PH = :vph, #PHV = :vphv" \
    --expression-attribute-names "{ \"#AV\": \"accountVerified\", \"#PH\": \"PhoneNumber\", \"#PHV\": \"PhoneNumberVerified\" }" \
    --expression-attribute-values "{ \":vav\":{\"N\": \"1\"}, \":vph\":{\"S\": \"$2\"}, \":vphv\":{\"N\": \"$3\"} }" \
    --region "${AWS_REGION}"
}

function updateMfaAuthApp() {
  echo "Updating mfa methods for user $1 in user-credentials..."
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #MFA = :vmfa" \
    --expression-attribute-names "{ \"#MFA\": \"MfaMethods\" }" \
    --expression-attribute-values "{ \":vmfa\": { \"L\": [ { \"M\": { \"CredentialValue\": { \"S\": \"$2\" }, \"Enabled\": { \"N\": \"1\" }, \"MethodVerified\": { \"N\": \"1\" }, \"MfaMethodType\": { \"S\": \"AUTH_APP\"}, \"Updated\": {\"S\": \"1970-01-01T00:00:00.000000\"} } } ] } }" \
    --region "${AWS_REGION}"
}

deleteUser $TEST_USER_EMAIL
deleteUser $PHONE_CODE_LOCK_TEST_USER_EMAIL
deleteUser $TEST_USER_NEW_EMAIL
deleteUser $TEST_USER_AUTH_APP_EMAIL
deleteUser $IPN1_NEW_USER_EMAIL
deleteUser $IPN2_NEW_USER_EMAIL
deleteUser $IPN3_NEW_USER_EMAIL

resetTermsAndConditions $TERMS_AND_CONDITIONS_TEST_USER_EMAIL

resetPassword $RESET_PW_USER_EMAIL $RESET_PW_CURRENT_PW
resetPassword $REQ_RESET_PW_USER_EMAIL $REQ_RESET_PW_CURRENT_PW

removeMfaMethods $TEST_USER_ACCOUNT_RECOVERY_EMAIL_1
updateMfaSMS $TEST_USER_ACCOUNT_RECOVERY_EMAIL_1 $PW_RESET_TEST_USER_PHONE "1"
updateMfaAuthApp $TEST_USER_ACCOUNT_RECOVERY_EMAIL_2 $ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET
updateAccountRecoveryBlock $TEST_USER_ACCOUNT_RECOVERY_EMAIL_3
updateAccountRecoveryBlock $TEST_USER_ACCOUNT_RECOVERY_EMAIL_4
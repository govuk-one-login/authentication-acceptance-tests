#!/usr/bin/env bash

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

function updateTermsAndConditions() {
  echo "resetting terms and conditions version for test user in user-profile: $1"
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #TC = :vtc" \
    --expression-attribute-names "{ \"#TC\": \"termsAndConditions\" }" \
    --expression-attribute-values "{ \":vtc\": { \"M\": { \"version\": { \"S\": \"$2\"}, \"timestamp\": {\"S\": \"1970-01-01T00:00:00.000000\"} } } }" \
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

  ics="$(echo -n $up | jq -r '.Item.SubjectID.S')"
  salt="$(echo -n $up | jq -r '.Item.salt.B' | base64 -d)"
  digest="$(echo -n "$2$ics$salt" | openssl dgst -sha256 -binary | base64 | tr '/+' '_-' | tr -d '=')"
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

function createOrUpdateUser() {
  echo "updating user in user-profile: $1"
  sub="$(uuidgen)"
  psub="$(uuidgen)"
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #PSUB = :vpsub, #SUB = :vsub, #AV = :vav, #EV = :vev" \
    --expression-attribute-names "{ \"#PSUB\": \"PublicSubjectID\", \"#SUB\": \"SubjectID\", \"#AV\": \"accountVerified\", \"#EV\": \"EmailVerified\" }" \
    --expression-attribute-values "{ \":vpsub\":{\"S\": \"$psub\"}, \":vsub\":{\"S\": \"$sub\"}, \":vav\":{\"N\": \"1\"}, \":vev\":{\"N\": \"1\"} }" \
    --region "${AWS_REGION}"

  pwd=$(echo -n "$2" | argon2 $(openssl rand -hex 32) -e -id -v 13 -k 15360 -t 2 -p 1)
  aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"$1\"}}" \
    --update-expression "SET #PW = :vpw, #SUB = :vsub" \
    --expression-attribute-names "{ \"#PW\": \"Password\", \"#SUB\": \"SubjectID\" }" \
    --expression-attribute-values "{ \":vpw\": { \"S\": \"$pwd\"}, \":vsub\":{\"S\": \"$sub\"} }" \
    --region "${AWS_REGION}"
}

function createSMSUser() {
  echo "Creating SMS user $1..."
  createOrUpdateUser $1 $2
  updateMfaSMS $1 $3 "1"
  updateTermsAndConditions $1 $4
}

function createAuthAppUser() {
  echo "Creating Auth App user $1..."
  createOrUpdateUser $1 $2
  updateMfaAuthApp $1 $3
  updateTermsAndConditions $1 $4
}

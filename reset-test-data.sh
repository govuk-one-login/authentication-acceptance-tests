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

if [ $LOCAL == "1" ]; then
  export $(grep -v '^#' .env | xargs)
fi

export AWS_REGION=eu-west-2
export ENVIRONMENT_NAME=build
export GDS_AWS_ACCOUNT=digital-identity-dev

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

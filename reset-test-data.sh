#!/usr/bin/env bash

set -eu

printf "\nResetting di-authentication-acceptance-tests test data...\n"

export $(grep -v '^#' .env | xargs)
export AWS_REGION=eu-west-2
export ENVIRONMENT_NAME=build
export GDS_AWS_ACCOUNT=digital-identity-dev

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${PHONE_CODE_LOCK_TEST_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${PHONE_CODE_LOCK_TEST_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_NEW_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_NEW_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_AUTH_APP_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${TEST_USER_AUTH_APP_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${IPN1_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${IPN1_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${IPN2_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${IPN2_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${IPN3_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "Truncating test user data in user-credentials..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb delete-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${IPN3_NEW_USER_EMAIL}\"}}" \
    --region "${AWS_REGION}"

echo "resetting terms and conditions version for test user in user-profile..."
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-profile" \
    --key "{\"Email\": {\"S\": \"${TERMS_AND_CONDITIONS_TEST_USER_EMAIL}\"}}" \
    --update-expression "SET #TC = :vtc" \
    --expression-attribute-names "{ \"#TC\": \"termsAndConditions\" }" \
    --expression-attribute-values "{ \":vtc\": { \"M\": { \"version\": { \"S\": \"1.0\"}, \"timestamp\": {\"S\": \"1970-01-01T00:00:00.000000\"} } } }" \
    --region "${AWS_REGION}"

echo "resetting password for reset password test user in user-credentials..."
export HASHED_PASSWORD_RESET=$(echo -n "${RESET_PW_CURRENT_PW}" | argon2 $(openssl rand -hex 32) -e -id -v 13 -k 15360 -t 2 -p 1)
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${RESET_PW_USER_EMAIL}\"}}" \
    --update-expression "SET #PW = :vpw" \
    --expression-attribute-names "{ \"#PW\": \"Password\" }" \
    --expression-attribute-values "{ \":vpw\": { \"S\": \"${HASHED_PASSWORD_RESET}\"} }" \
    --region "${AWS_REGION}"

echo "resetting password for require password reset test user in user-credentials..."
export HASHED_PASSWORD_REQ_RESET=$(echo -n "${REQ_RESET_PW_CURRENT_PW}" | argon2 $(openssl rand -hex 32) -e -id -v 13 -k 15360 -t 2 -p 1)
gds-cli aws ${GDS_AWS_ACCOUNT} aws dynamodb update-item \
    --table-name "${ENVIRONMENT_NAME}-user-credentials" \
    --key "{\"Email\": {\"S\": \"${REQ_RESET_PW_USER_EMAIL}\"}}" \
    --update-expression "SET #PW = :vpw" \
    --expression-attribute-names "{ \"#PW\": \"Password\" }" \
    --expression-attribute-values "{ \":vpw\": { \"S\": \"${HASHED_PASSWORD_REQ_RESET}\"} }" \
    --region "${AWS_REGION}"

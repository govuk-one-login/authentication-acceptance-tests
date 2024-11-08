#!/bin/bash
# NOTE: this script only runs in codebuild in new secure pipeline

set -u

if [[ -z ${CODEBUILD_BUILD_ID:-} ]]; then
  echo 'This should only be run in codebuild'
  exit 1
fi

ENVIRONMENT=${TEST_ENVIRONMENT:-local}

case $ENVIRONMENT in
  build)
    CROSSACCOUNT_ROLEARN="arn:aws:iam::761723964695:role/CrossAccountRole-new-build"
    ;;
  staging)
    CROSSACCOUNT_ROLEARN="arn:aws:iam::758531536632:role/CrossAccountRole-new-staging"
    ;;
  *)
    echo "Unconfigured environment: $ENVIRONMENT"
    exit 1
    ;;
esac

/test/run-acceptance-tests.sh -e "${ENVIRONMENT}"
mv /test/*.env /test/.env
cat /test/.env

echo "Assuming cross-account role"
output=$(aws sts assume-role --role-arn "$CROSSACCOUNT_ROLEARN" --role-session-name "dynamo-db-access" --output json)

# shellcheck disable=SC2155
export AWS_ACCESS_KEY_ID="$(echo "$output" | jq -r '.Credentials.AccessKeyId')"
# shellcheck disable=SC2155
export AWS_SECRET_ACCESS_KEY="$(echo "$output" | jq -r '.Credentials.SecretAccessKey')"
# shellcheck disable=SC2155
export AWS_SESSION_TOKEN="$(echo "$output" | jq -r '.Credentials.SessionToken')"

/test/run-acceptance-tests.sh -s "${ENVIRONMENT}"
return_code=$?

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

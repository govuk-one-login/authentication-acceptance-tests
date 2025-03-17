#!/bin/bash
# NOTE: this script only runs in codebuild in new secure pipeline
# This file is put in place during docker build, so paths to other scripts are relative to the docker context
set -euo pipefail

if [[ -z ${CODEBUILD_BUILD_ID:-} ]]; then
  echo 'This should only be run in codebuild'
  exit 1
fi

export ENVIRONMENT=${TEST_ENVIRONMENT:-local}

if [ -z "${AWS_REGION:-}" ]; then
  export AWS_REGION="eu-west-2"
fi

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

pushd /test > /dev/null || exit 1

# shellcheck source=../scripts/fetch_envars.sh
/test/scripts/fetch_envars.sh "${ENVIRONMENT}" | tee /test/.env

echo "Assuming cross-account role"
output=$(aws sts assume-role --role-arn "$CROSSACCOUNT_ROLEARN" --role-session-name "dynamo-db-access" --output json)

# shellcheck disable=SC2155
export AWS_ACCESS_KEY_ID="$(echo "$output" | jq -r '.Credentials.AccessKeyId')"
# shellcheck disable=SC2155
export AWS_SECRET_ACCESS_KEY="$(echo "$output" | jq -r '.Credentials.SecretAccessKey')"
# shellcheck disable=SC2155
export AWS_SESSION_TOKEN="$(echo "$output" | jq -r '.Credentials.SessionToken')"

# shellcheck source=/dev/null
set -o allexport && source .env && set +o allexport
export USE_SSM=false
./gradlew --no-daemon cucumber
return_code=$?

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

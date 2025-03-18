#!/bin/bash
# NOTE: this script only runs in codebuild in new secure pipeline
# This file is put in place during docker build, so paths to other scripts are relative to the docker context
set -euo pipefail

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

PROCESSOR_CORES=$(nproc --all 2> /dev/null || echo 1)

# run one more browser than we have cores
export PARALLEL_BROWSERS=$((PROCESSOR_CORES + 1))

/test/run-acceptance-tests.sh -s "${ENVIRONMENT}"
return_code=$?

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

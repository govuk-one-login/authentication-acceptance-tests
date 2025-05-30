#!/bin/bash
# NOTE: this script only runs in codebuild in new secure pipeline
# This file is put in place during docker build, so paths to other scripts are relative to the docker context
set -uo pipefail

if [[ -z ${CODEBUILD_BUILD_ID:-} ]]; then
  echo 'This should only be run in codebuild'
  exit 1
fi

ENVIRONMENT=${TEST_ENVIRONMENT:-local}

case $ENVIRONMENT in
  dev)
    CROSSACCOUNT_ROLEARN="arn:aws:iam::653994557586:role/CrossAccountRole-new-dev"
    ;;
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
echo "Fetching environment variables for ${ENVIRONMENT} environment"

/test/scripts/fetch_envars.sh "${ENVIRONMENT}" | tee /test/.env

if [[ -f ui-env-override ]]; then
  echo "Adding local over-rides to env file:"
  cat ui-env-override
  cat ui-env-override >> .env
else
  echo "No local over-rides found"
fi

if [ "${SAM_STACK_NAME:-}" == "authentication-api" ]; then
  echo -e "\nEnvironment configuration overrides for ${SAM_STACK_NAME}:"
  if grep -qE '^API_RP_URL=' /test/.env; then
    API_RP_URL=$(grep ^API_RP_URL= /test/.env | cut -d= -f2-)

    if grep -qE '^RP_URL=' /test/.env; then
      sed -i '/^RP_URL=.*/d' /test/.env
    fi
    echo -e "RP_URL=${API_RP_URL}" | tee --append /test/.env
  fi

  if grep -qE '^API_STUB_RP_TYPE=' /test/.env; then
    API_STUB_RP_TYPE=$(grep ^API_STUB_RP_TYPE= /test/.env | cut -d= -f2-)

    if grep -qE '^STUB_RP_TYPE=' /test/.env; then
      sed -i '/^STUB_RP_TYPE=.*/d' /test/.env
    fi
    echo -e "STUB_RP_TYPE=${API_STUB_RP_TYPE}" | tee --append /test/.env
  fi

  if grep -qE '^API_CUCUMBER_FILTER_TAGS=' /test/.env; then
    API_CUCUMBER_FILTER_TAGS=$(grep ^API_CUCUMBER_FILTER_TAGS= /test/.env | cut -d= -f2-)

    if grep -qE '^CUCUMBER_FILTER_TAGS=' /test/.env; then
      sed -i '/^CUCUMBER_FILTER_TAGS=.*/d' /test/.env
    fi
    echo -e "CUCUMBER_FILTER_TAGS=${API_CUCUMBER_FILTER_TAGS}" | tee --append /test/.env
  fi
fi

echo
echo "Assuming cross-account role"
echo

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

/test/run-tests.sh -s "${ENVIRONMENT}"
return_code=$?

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

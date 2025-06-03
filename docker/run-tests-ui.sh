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
/test/scripts/fetch_envars.sh "${ENVIRONMENT}" | tee /test/.env

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

export ENVIRONMENT="${ENVIRONMENT}"

if [ -z "${AWS_REGION:-}" ]; then
  export AWS_REGION="eu-west-2"
fi

pushd /test > /dev/null || exit 1

if [[ -f ui-env-override ]]; then
  echo "Adding local over-rides to env file:"
  cat ui-env-override
  cat ui-env-override >> .env
else
  echo "No local over-rides found"
fi

if [ -f ".env" ]; then
  # source .env file if it exists (as we're running in docker, it's definitely needed if it's there)
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
else
  echo "No .env file found"
fi

echo
echo "********************************************************************************************"
echo "CUCUMBER FILTER TAGS: ${CUCUMBER_FILTER_TAGS}"
echo "********************************************************************************************"
echo

SELENIUM_BROWSER="$(cat /opt/selenium/browser_name)"
export SELENIUM_BROWSER

./gradlew --no-daemon cucumber

return_code=$?

popd > /dev/null || exit 1

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

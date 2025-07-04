#!/bin/bash
# NOTE: this script only runs in codebuild in new secure pipeline
# This file is put in place during docker build, so paths to other scripts are relative to the docker context
set -uo pipefail

export ENVIRONMENT=${TEST_ENVIRONMENT:-local}

check_guard_conditions() {
  allowed_environments=("dev" "build" "staging")
  if [[ ! " ${allowed_environments[*]} " =~ ${ENVIRONMENT} ]]; then
    echo "Unconfigured environment: $ENVIRONMENT"
    exit 1
  fi

  if [[ -z ${CODEBUILD_BUILD_ID:-} ]]; then
    echo 'This should only be run in codebuild'
    exit 1
  fi

  if [ -z "${AWS_REGION:-}" ]; then
    export AWS_REGION="eu-west-2"
  fi
}

setup_to_run_api_tests() {
  if [ "${SAM_STACK_NAME:-}" == "authentication-api" ]; then
    echo -e "\nEnvironment configuration overrides for ${SAM_STACK_NAME}:"
    if grep -qE '^API_RP_URL=' /test/.env; then
      API_RP_URL=$(grep ^API_RP_URL= /test/.env | cut -d= -f2- | tail -1)

      if grep -qE '^RP_URL=' /test/.env; then
        sed -i '/^RP_URL=.*/d' /test/.env
      fi
      echo -e "RP_URL=${API_RP_URL}" | tee --append /test/.env
    fi

    if grep -qE '^API_STUB_RP_TYPE=' /test/.env; then
      API_STUB_RP_TYPE=$(grep ^API_STUB_RP_TYPE= /test/.env | cut -d= -f2- | tail -1)

      if grep -qE '^STUB_RP_TYPE=' /test/.env; then
        sed -i '/^STUB_RP_TYPE=.*/d' /test/.env
      fi
      echo -e "STUB_RP_TYPE=${API_STUB_RP_TYPE}" | tee --append /test/.env
    fi

    if grep -qE '^API_CUCUMBER_FILTER_TAGS=' /test/.env; then
      API_CUCUMBER_FILTER_TAGS=$(grep ^API_CUCUMBER_FILTER_TAGS= /test/.env | cut -d= -f2- | tail -1)

      if grep -qE '^CUCUMBER_FILTER_TAGS=' /test/.env; then
        sed -i '/^CUCUMBER_FILTER_TAGS=.*/d' /test/.env
      fi
      echo -e "CUCUMBER_FILTER_TAGS=${API_CUCUMBER_FILTER_TAGS}" | tee --append /test/.env
    fi
  fi
}

assume_role_to_access_dynamodb_in_api_account() {
  local cross_account_role_arn
  case $ENVIRONMENT in
    dev)
      cross_account_role_arn="arn:aws:iam::653994557586:role/CrossAccountRole-new-dev"
      ;;
    build)
      cross_account_role_arn="arn:aws:iam::761723964695:role/CrossAccountRole-new-build"
      ;;
    staging)
      cross_account_role_arn="arn:aws:iam::758531536632:role/CrossAccountRole-new-staging"
      ;;
  esac

  echo "Assuming cross-account role"

  output=$(aws sts assume-role --role-arn "$cross_account_role_arn" --role-session-name "dynamo-db-access" --output json)

  AWS_ACCESS_KEY_ID="$(echo "$output" | jq -r '.Credentials.AccessKeyId')"
  export AWS_ACCESS_KEY_ID

  AWS_SECRET_ACCESS_KEY="$(echo "$output" | jq -r '.Credentials.SecretAccessKey')"
  export AWS_SECRET_ACCESS_KEY

  AWS_SESSION_TOKEN="$(echo "$output" | jq -r '.Credentials.SessionToken')"
  export AWS_SESSION_TOKEN
}

setup_cucumber_run_parameters() {
  local processor_cores
  processor_cores=$(nproc --all 2> /dev/null || echo 1)
  PARALLEL_BROWSERS=$((processor_cores + 1))
  export PARALLEL_BROWSERS

  SELENIUM_BROWSER="$(cat /opt/selenium/browser_name)"
  export SELENIUM_BROWSER
}

setup_environment_variables() {
  if [ ! -f /test/.env ]; then
    echo "No .env file provided so creating one from SSM."
    # shellcheck source=../scripts/fetch_envars.sh
    /test/scripts/fetch_envars.sh "${ENVIRONMENT}" | tee /test/.env
  fi
}

export_environment_variables() {
  # shellcheck source=/dev/null
  set -o allexport && source /test/.env && set +o allexport
}

log_run_configuration() {
  if [ -n "${CUCUMBER_FILTER_TAGS:-}" ]; then
    echo
    echo "********************************************************************************************"
    echo "CUCUMBER FILTER TAGS: ${CUCUMBER_FILTER_TAGS}"
    echo "RP_URL: ${RP_URL}"
    echo "STUB_RP_TYPE: ${STUB_RP_TYPE}"
    echo "********************************************************************************************"
    echo
  else
    echo "CUCUMBER_FILTER_TAGS not defined, cannot run tests"
    exit 1
  fi
}

copy_test_reports() {
  if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
    cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
  fi
}

execute_tests() {
  local acceptance_tests_exit_status

  pushd /test > /dev/null || exit 1
  ./gradlew --no-daemon cucumber
  acceptance_tests_exit_status=$?
  popd > /dev/null || exit 1

  copy_test_reports

  return $acceptance_tests_exit_status
}

check_guard_conditions
setup_cucumber_run_parameters
setup_environment_variables
setup_to_run_api_tests
export_environment_variables
log_run_configuration
assume_role_to_access_dynamodb_in_api_account
execute_tests

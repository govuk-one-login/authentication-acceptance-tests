#!/usr/bin/env bash
set -euo pipefail

export ENVIRONMENT="${2}"
export NEW_AM_ENV=true

check_guard_conditions() {
  if [ -z "${AWS_REGION:-}" ]; then
    export AWS_REGION="eu-west-2"
  fi

}

setup_environment_variables() {
  if [ ! -f /test/.env ]; then
    echo "No .env file provided so creating one from SSM."
    # shellcheck source=../scripts/fetch_envars.sh
    /test/scripts/fetch_envars.sh "${ENVIRONMENT}" | tee /test/.env
  fi
  # shellcheck source=/dev/null
  set -o allexport && source /test/.env && set +o allexport

  if [ -n "${AD_HOC_CUCUMBER_TAGS:-}" ]; then
    if [[ ! ${AD_HOC_CUCUMBER_TAGS} =~ ^[a-zA-Z0-9@_-]+(\s+(and|or|not)\s+[a-zA-Z0-9@_()-]+)*$ ]]; then
      echo "Error: Invalid cucumber tags format: ${AD_HOC_CUCUMBER_TAGS}"
      exit 1
    fi

    echo "Value from build-and-push-adhoc-tests workflow present."
    echo "Overwriting CUCUMBER_FILTER_TAGS value from SSM."

    sed -i '/^CUCUMBER_FILTER_TAGS=/d' /test/.env
    printf 'CUCUMBER_FILTER_TAGS=%q\n' "${AD_HOC_CUCUMBER_TAGS}" >> /test/.env
    export CUCUMBER_FILTER_TAGS="${AD_HOC_CUCUMBER_TAGS}"
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

log_run_configuration() {
  if [ -n "${CUCUMBER_FILTER_TAGS:-}" ]; then
    echo
    echo "********************************************************************************************"
    echo "CUCUMBER FILTER TAGS: ${CUCUMBER_FILTER_TAGS}"
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
  ./gradlew --no-daemon --offline :acceptance-tests:cucumber
  acceptance_tests_exit_status=$?
  popd > /dev/null || exit 1

  copy_test_reports

  return $acceptance_tests_exit_status
}

check_guard_conditions
setup_environment_variables
log_run_configuration
assume_role_to_access_dynamodb_in_api_account
execute_tests

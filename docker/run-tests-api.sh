#!/usr/bin/env bash
set -euo pipefail

export ENVIRONMENT="${2}"

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

setup_cucumber_run_parameters() {
  SELENIUM_BROWSER="$(cat /opt/selenium/browser_name)"
  export SELENIUM_BROWSER
}

copy_test_reports() {
  if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
    cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
  fi
}

execute_tests() {
  local acceptance_tests_exit_status
  trap 'exit $acceptance_tests_exit_status' EXIT

  pushd /test > /dev/null || exit 1
  ./gradlew --no-daemon cucumber
  acceptance_tests_exit_status=$?
  popd > /dev/null || exit 1

  copy_test_reports
}

check_guard_conditions
setup_cucumber_run_parameters
setup_environment_variables
log_run_configuration
execute_tests

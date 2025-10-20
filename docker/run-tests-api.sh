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

  pushd /test > /dev/null || exit 1
  ./gradlew --no-daemon :acceptance-tests:cucumber
  acceptance_tests_exit_status=$?
  popd > /dev/null || exit 1

  copy_test_reports

  return $acceptance_tests_exit_status
}

check_guard_conditions
setup_cucumber_run_parameters
setup_environment_variables
log_run_configuration
execute_tests

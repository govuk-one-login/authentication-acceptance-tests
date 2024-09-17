#!/bin/bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

ENVIRONMENT=${2:-local}

DOCKER_BASE=docker-compose
SSM_VARS_PATH="/acceptance-tests/$ENVIRONMENT"
TESTDIR="/test"

CUCUMBER_OPTIONS=""

EXPORT_ENV=0
LOCAL=0
while getopts "lret:" opt; do
  case ${opt} in
  l)
    LOCAL=1
    ;;
  r)
    LOCAL=0
    ;;
  e)
    EXPORT_ENV=1
    ;;
  *)
    usage
    exit 1
    ;;
  esac
done

echo "Running in $ENVIRONMENT environment..."

function get_env_vars_from_SSM() {

  echo "Getting environment variables from SSM ... "
  if [ ${EXPORT_ENV} == "1" ]; then
    dt="$(date "+%Y%m%d-%H%M%S")"
    envfile="${dt}.env"
    echo "Exporting environment variables from SSM to file ${envfile} ... "
    {
      echo "#"
      echo "# Acceptance test config exported from ${SSM_VARS_PATH} at ${dt}"
      echo "#"
      echo "# Rename to .env to use for testing"
      echo "#"
    } >>"${envfile}"
  fi

  envars="$(aws ssm get-parameters-by-path --with-decryption --path $SSM_VARS_PATH |
    jq -r '.Parameters[] | [(.Name|split("/")|last), .Value]|@tsv')"

  while IFS=$'\t' read -r name value; do
    export "${name}"="${value}"
    if [ ${EXPORT_ENV} == "1" ]; then
      echo "${name}=${value}" >>"${envfile}"
    fi
  done <<<"${envars}"
  echo "Exported SSM parameters completed."

  if [ ${EXPORT_ENV} == "1" ]; then
    exit 0
  fi
}

function export_selenium_config() {
  export SELENIUM_URL="http://localhost:4444/wd/hub"
  export SELENIUM_BROWSER=chrome
  export SELENIUM_LOCAL=true
  export SELENIUM_HEADLESS=true
  export DEBUG_MODE=false
}

echo -e "Building di-authentication-acceptance-tests..."

if [ -d "$TESTDIR" ]; then
  echo "Changing to $TESTDIR"
  cd $TESTDIR
fi

./gradlew clean spotlessApply build -x :acceptance-tests:test

build_and_test_exit_code=$?
if [ ${build_and_test_exit_code} -ne 0 ]; then
  echo -e "acceptance-tests failed."
  exit 1
fi

echo -e "Running di-authentication-acceptance-tests..."

export_selenium_config
if [ $LOCAL == "1" ]; then
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
else
  export AWS_PROFILE="gds-di-development-admin"
  # shellcheck source=./scripts/export_aws_creds.sh
  source "${DIR}/scripts/export_aws_creds.sh"
  get_env_vars_from_SSM
fi

if [ $LOCAL == "1" ]; then
  ./reset-test-data.sh -l $ENVIRONMENT
else
  ./reset-test-data.sh -r $ENVIRONMENT
fi

./gradlew cucumber -PcucumberOptions="${CUCUMBER_OPTIONS}"

build_and_test_exit_code=$?

if [ ${build_and_test_exit_code} -ne 0 ]; then
  echo -e "acceptance-tests failed."

else
  echo -e "acceptance-tests SUCCEEDED."
fi

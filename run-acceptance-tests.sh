#!/usr/bin/env bash

set -eu

DOCKER_BASE=docker-compose
SSM_VARS_PATH="/acceptance-tests/local"

EXPORT_ENV=0
LOCAL=0
while getopts "le" opt; do
  case ${opt} in
    l)
        LOCAL=1
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

function start_docker_services() {
  ${DOCKER_BASE} up --build -d --wait --no-deps --quiet-pull "$@"
}

function stop_docker_services() {
  ${DOCKER_BASE} down --rmi local --remove-orphans
}

function get_env_vars_from_SSM() {
  echo -n "Getting AWS credentials ... "
  eval "$(gds aws digital-identity-dev -e)"
  echo "done!"

  echo "Getting environment variables from SSM ... "
  if [ $EXPORT_ENV == "1" ]; then
    dt="$(date "+%Y%m%d-%H%M%S")"
    envfile="$dt.env"
    echo "Exporting environment variables from SSM to file $envfile ... "
    {
      echo "#"
      echo "# Acceptance test config exported from $SSM_VARS_PATH at $dt"
      echo "#"
      echo "# Rename to .env to use for testing"
      echo "#"
    } >> $envfile
  fi

  VARS="$(aws ssm get-parameters-by-path --region eu-west-2 --with-decryption --path $SSM_VARS_PATH | jq -r '.Parameters[] | @base64')"
  for VAR in $VARS; do
    VAR_NAME="$(echo ${VAR} | base64 -d | jq -r '.Name / "/" | .[3]')"
    VAR_NAME_VALUE=$VAR_NAME="$(echo ${VAR} | base64 -d | jq -r '.Value')"
    if [ $EXPORT_ENV == "1" ]; then
      echo "$VAR_NAME_VALUE" >> $envfile
    fi
    export "$VAR_NAME"="$(echo ${VAR} | base64 -d | jq -r '.Value')"
  done
  echo "done!"
  if [ $EXPORT_ENV == "1" ]; then
    exit 0
  fi
}

function export_selenium_config() {
  export SELENIUM_URL="http://localhost:4444/wd/hub"
  export SELENIUM_BROWSER=firefox
  export SELENIUM_LOCAL=true
  export SELENIUM_HEADLESS=false
  export DEBUG_MODE=false
}

echo -e "Building di-authentication-acceptance-tests..."

./gradlew clean spotlessApply build -x :acceptance-tests:test

build_and_test_exit_code=$?
if [ ${build_and_test_exit_code} -ne 0 ]
then
    echo -e "acceptance-tests failed."
    exit 1
fi

echo -e "Running di-authentication-acceptance-tests..."

start_docker_services selenium-firefox selenium-chrome

if [ $LOCAL == "1" ]; then
  export_selenium_config
  export $(grep -v '^#' .env | xargs)
else
  export_selenium_config
  get_env_vars_from_SSM
fi

./gradlew cucumber

build_and_test_exit_code=$?

stop_docker_services selenium-firefox selenium-chrome

if [ ${build_and_test_exit_code} -ne 0 ]
then
    echo -e "acceptance-tests failed."

else
    echo -e "acceptance-tests SUCCEEDED."
fi

./reset-test-data.sh

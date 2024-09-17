#!/bin/bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

export ENVIRONMENT=${2:-local}

DOCKER_BASE=docker-compose
SSM_VARS_PATH="/acceptance-tests/$ENVIRONMENT"
TESTDIR="/test"

WRITE_ENV=0
USE_SSM=0
while getopts "lret:" opt; do
  case ${opt} in
  l)
    USE_SSM=0
    ;;
  r)
    USE_SSM=1
    ;;
  e)
    WRITE_ENV=1
    ;;
  *)
    usage
    exit 1
    ;;
  esac
done

export USE_SSM

echo "Running in $ENVIRONMENT environment..."

function write_env_file() {

  echo "Getting environment variables from SSM ... "

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

  envars="$(aws ssm get-parameters-by-path --with-decryption --path "/acceptance-tests/${ENVIRONMENT}" |
    jq -r '.Parameters[] | [(.Name|split("/")|last), .Value]|@tsv')"

  while IFS=$'\t' read -r name value; do
    echo "${name}=${value}" >>"${envfile}"
  done <<<"${envars}"
  echo "Exporting SSM parameters completed."
  exit 0
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

export AWS_PROFILE="gds-di-development-admin"
# shellcheck source=./scripts/export_aws_creds.sh
source "${DIR}/scripts/export_aws_creds.sh"

if [ "${WRITE_ENV}" == "1" ]; then
  write_env_file
fi

if [ "${USE_SSM}" == "0" ]; then
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
fi

./gradlew cucumber

build_and_test_exit_code=$?

if [ ${build_and_test_exit_code} -ne 0 ]; then
  echo -e "acceptance-tests failed."

else
  echo -e "acceptance-tests SUCCEEDED."
fi

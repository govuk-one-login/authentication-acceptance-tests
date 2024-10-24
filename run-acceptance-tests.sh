#!/bin/bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd)"

export ENVIRONMENT=${2?Please provide the environment to run the tests against.}

SSM_VARS_PATH="/acceptance-tests/$ENVIRONMENT"
pushd "${DIR}" > /dev/null || exit 1

WRITE_ENV=false
USE_SSM=false
LOCAL=true
while getopts "lrse:" opt; do
  case ${opt} in
    l)
      LOCAL=true
      ;;
    r)
      LOCAL=false
      ;;
    s)
      USE_SSM=true
      ;;
    e)
      WRITE_ENV=true
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
  } >> "${envfile}"

  envars="$(aws ssm get-parameters-by-path --with-decryption --path "/acceptance-tests/${ENVIRONMENT}" \
    | jq -r '.Parameters[] | [(.Name|split("/")|last), .Value]|@tsv')"

  while IFS=$'\t' read -r name value; do
    echo "${name}=${value}" >> "${envfile}"
  done <<< "${envars}"
  echo "Exporting SSM parameters completed."
  exit 0
}

# shellcheck source=./scripts/check_aws_creds.sh
source "${DIR}/scripts/check_aws_creds.sh"

if [ "${WRITE_ENV}" == "true" ]; then
  write_env_file
fi

echo -e "Building di-authentication-acceptance-tests..."

#if ! ./gradlew clean build -x :acceptance-tests:test -x spotlessApply -x spotlessCheck; then
#  echo -e "acceptance-tests build failed."
#  exit 1
#fi

echo -e "Running di-authentication-acceptance-tests..."

if [ "${LOCAL}" == "true" ]; then
  export SELENIUM_URL="http://localhost:4444/wd/hub"
  export SELENIUM_BROWSER=chrome
  export SELENIUM_LOCAL=true
  export SELENIUM_HEADLESS=true
  export DEBUG_MODE=false
fi

if [ -f ".env" ]; then
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
fi

if ./gradlew test -PincludeTags=LegalAndPolicy; then
  echo -e "acceptance-tests SUCCEEDED."
else
  result=$?
  echo -e "acceptance-tests failed."
  exit ${result}
fi

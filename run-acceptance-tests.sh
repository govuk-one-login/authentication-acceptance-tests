#!/bin/bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd)"

WRITE_ENV=false
USE_SSM=true
LOCAL=true
FAIL_FAST_ENABLED=false
INCLUDE_TAGS=()
EXCLUDE_TAGS=()
while getopts "lrse:i:x:f" opt; do
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
    i)
      INCLUDE_TAGS+=("$OPTARG")
      ;;
    x)
      EXCLUDE_TAGS+=("$OPTARG")
      ;;
    f)
      FAIL_FAST_ENABLED=true
      ;;
    *)
      usage
      exit 1
      ;;
  esac
done
shift $((OPTIND - 1))

export ENVIRONMENT=${1?Please provide the environment to run the tests against.}
SSM_VARS_PATH="/acceptance-tests/$ENVIRONMENT"
pushd "${DIR}" > /dev/null || exit 1

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

if [ -n "${CUCUMBER_FILTER_TAGS}" ]; then
  INCLUDE_TAGS+=("${CUCUMBER_FILTER_TAGS//@/}")
fi

function run_tests() {
  GRADLE_ARGS=("test")
  if [ "${FAIL_FAST_ENABLED}" == "true" ]; then
    GRADLE_ARGS+=("-PfailFast")
  fi
  if [ ${#INCLUDE_TAGS[@]} -gt 0 ]; then
    GRADLE_ARGS+=("-PincludeTags=$(
      IFS=\|
      echo "${INCLUDE_TAGS[*]}"
    )")
  fi
  if [ ${#EXCLUDE_TAGS[@]} -gt 0 ]; then
    GRADLE_ARGS+=("-PexcludeTags=$(
      IFS=\|
      echo "${EXCLUDE_TAGS[*]}"
    )")
  fi
  echo "running ./gradlew ${GRADLE_ARGS[*]}"
  ./gradlew "${GRADLE_ARGS[@]}"
}

if run_tests; then
  echo -e "acceptance-tests SUCCEEDED."
else
  result=$?
  echo -e "acceptance-tests failed."
  exit ${result}
fi

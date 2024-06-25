#!/bin/bash

set -eu

envvalue=("sandpit" "authdev1" "authdev2" "dev")

select word in "${envvalue[@]}"; do
  if [[ -z "$word" ]]; then
    printf '"%s" is not a valid choice\n' "$REPLY" >&2
  else
    user_in="$((REPLY - 1))"
    break
  fi
done

for ((i = 0; i < ${#envvalue[@]}; ++i)); do
  if ((i == user_in)); then
    printf 'You picked "%s"\n' "${envvalue[$i]}"
    export ENVIRONMENT=${envvalue[$i]}
    printf "Running AC tests against %s\n" "${ENVIRONMENT}"
    read -r -p "Press enter to continue or Ctrl+C to abort"
  fi
done


DOCKER_BASE=docker-compose
SSM_VARS_PATH="/acceptance-tests/$ENVIRONMENT"
TESTDIR="/test"

echo "Running in $ENVIRONMENT environment..."

function start_docker_services() {
  ${DOCKER_BASE} up --build -d --wait --no-deps --quiet-pull "$@"
}

function stop_docker_services() {
  ${DOCKER_BASE} down --rmi local --remove-orphans
}

function get_env_vars_from_SSM() {

  echo "Getting environment variables from SSM ... "

  VARS="$(aws ssm get-parameters-by-path --with-decryption --path $SSM_VARS_PATH | jq -r '.Parameters[] | @base64')"
  for VAR in $VARS; do
    VAR_NAME="$(echo ${VAR} | base64 -d | jq -r '.Name / "/" | .[3]')"
    export "$VAR_NAME"="$(echo ${VAR} | base64 -d | jq -r '.Value')"
  done
  echo "Exported SSM parameters completed."
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

start_docker_services selenium-firefox selenium-chrome

export_selenium_config

set -o allexport && source .env && set +o allexport



./reset-test-data.sh -l "$ENVIRONMENT"


./gradlew cucumber

build_and_test_exit_code=$?

stop_docker_services selenium-firefox selenium-chrome

if [ ${build_and_test_exit_code} -ne 0 ]; then
  echo -e "acceptance-tests failed."

else
  echo -e "acceptance-tests SUCCEEDED."
fi

#!/usr/bin/env bash
DOCKER_BASE=docker-compose
function wait_for_docker_services() {
  RUNNING=0
  LOOP_COUNT=0
  echo -n "Waiting for service(s) to become healthy ($*) ."
  until [[ ${RUNNING} == $# || ${LOOP_COUNT} == 100 ]]; do
    RUNNING=$(${DOCKER_BASE} ps -q "$@" | xargs docker inspect | jq -rc '[ .[] | select(.State.Health.Status == "healthy")] | length')
    LOOP_COUNT=$((LOOP_COUNT + 1))
    echo -n "."
  done
  if [[ ${LOOP_COUNT} == 100 ]]; then
    echo "FAILED"
    return 1
  fi
  echo " done!"
  return 0
}

function start_docker_services() {
  ${DOCKER_BASE} up --build -d --no-deps --quiet-pull "$@"
}

function stop_docker_services() {
  ${DOCKER_BASE} down --rmi local --remove-orphans
}

function build_docker_service() {
  ${DOCKER_BASE} build --quiet "$@"
}

printf "\nBuilding di-authentication-acceptance-tests...\n"

./gradlew clean build -x :acceptance-tests:test

build_and_test_exit_code=$?
if [ ${build_and_test_exit_code} -ne 0 ]
then
    printf "\nacceptance-tests failed.\n"
    exit 1
fi

printf "\nRunning di-authentication-acceptance-tests...\n"

start_docker_services selenium
sleep 5

export $(grep -v '^#' .env | xargs)

./gradlew cucumber

build_and_test_exit_code=$?

stop_docker_services selenium

if [ ${build_and_test_exit_code} -ne 0 ]
then
    printf "\nacceptance-tests failed.\n"

else
    printf "\nacceptance-tests SUCCEEDED.\n"
fi
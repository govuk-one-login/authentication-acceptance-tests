#!/usr/bin/env bash

set -eu

ENVIRONMENT=${2:-build}

source ./scripts/database.sh
source ./scripts/reset-test-users.sh

LOCAL=0
while getopts "lr" opt; do
  case ${opt} in
  l)
    LOCAL=1
    ;;
  r)
    LOCAL=0
    ;;
  *)
    usage
    exit 1
    ;;
  esac
done

echo -e "Resetting di-authentication-acceptance-tests test data..."

export AWS_REGION=eu-west-2
export ENVIRONMENT_NAME=$ENVIRONMENT
export GDS_AWS_ACCOUNT=digital-identity-dev

if [ $LOCAL == "1" ]; then
  export $(grep -v '^#' .env | xargs)
fi

#if [ $LOCAL == "1" ]; then
#  echo -e "Getting AWS credentials ..."
#  eval "$(gds aws ${GDS_AWS_ACCOUNT} -e)"
#  echo "done!"
#fi

resetTestUsers

#!/usr/bin/env bash

set -eu

source ./scripts/database.sh
source ./scripts/reset-test-users.sh

LOCAL=0
while getopts "l" opt; do
  case ${opt} in
  l)
    LOCAL=1
    ;;
  *)
    usage
    exit 1
    ;;
  esac
done

echo -e "Resetting di-authentication-acceptance-tests test data..."

export AWS_REGION=eu-west-2
export ENVIRONMENT_NAME=build
export GDS_AWS_ACCOUNT=digital-identity-dev

if [ $LOCAL == "1" ]; then
  export $(grep -v '^#' .env | xargs)
fi

echo -e "Getting AWS credentials ..."
eval "$(gds aws ${GDS_AWS_ACCOUNT} -e)"
echo "done!"

resetTestUsers

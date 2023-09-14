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

if [ $LOCAL == "1" ]; then
  # Ensure AWS credentials are set in the environment
  if [[ -z "${AWS_ACCESS_KEY_ID:-}" || -z "${AWS_SECRET_ACCESS_KEY:-}" ]]; then
    echo "!! AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY must be set in the environment" >&2
    exit 1
  fi
fi

resetTestUsers

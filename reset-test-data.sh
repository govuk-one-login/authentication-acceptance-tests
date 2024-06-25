#!/usr/bin/env bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

ENVIRONMENT=${2:-build}

source ./scripts/database.sh
source ./scripts/reset-test-users.sh
source ./scripts/reset-account-interventions.sh
source ./scripts/re-authentication.sh
source ./scripts/2hr-lockout-period.sh

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

echo -e "Resetting di-authentication-acceptance-tests test data in ${ENVIRONMENT}..."

export ENVIRONMENT_NAME="${ENVIRONMENT}"

if [ "${LOCAL}" == "1" ]; then
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
  echo "reset-test-data ENVIRONMENT: ${ENVIRONMENT}"
  if [ "${ENVIRONMENT}" == "staging" ]; then
    export AWS_PROFILE="di-auth-staging-admin"
  else
    if [ "${ENVIRONMENT}" == "authdev1" ] || [ "${ENVIRONMENT}" == "authdev2" ] || [ "${ENVIRONMENT}" == "dev" ]; then
        export AWS_PROFILE="di-auth-development-admin"
    else
        export AWS_PROFILE="gds-di-development-admin"
    fi
  fi
  # shellcheck source=./scripts/export_aws_creds.sh
  source "${DIR}/scripts/export_aws_creds.sh"
fi

resetTestUsers
resetAccountInterventions
reAuthentication
2hrLockoutPeriod

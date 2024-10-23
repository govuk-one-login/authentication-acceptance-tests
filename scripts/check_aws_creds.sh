#!/bin/bash
set -euo pipefail

[[ ${BASH_SOURCE[0]} != "${0}" ]] || {
  echo "Error: Script must be sourced, not executed"
  exit 1
}

if [[ -n ${AWS_VAULT:-} ]]; then
  # shellcheck disable=SC2016
  echo 'FATAL: aws-vault / gds-cli are deprecated and should no longer be used. Instead, run the script directly (ie. `./deploy-sandpit.sh`)' >&2
  exit 1
fi

if [[ -n ${CODEBUILD_BUILD_ID:-} ]]; then
  return 0 # Running in CodeBuild, do nothing
fi

if [[ -z ${AWS_PROFILE:-} ]]; then
  # shellcheck disable=SC2016
  echo 'ERROR: $AWS_PROFILE is not set. This should be exported before running this script.'
  exit 1
fi
echo "Validating credentials from AWS CLI profile ${AWS_PROFILE}"

# Test if the AWS CLI is configured with the correct profile
if ! sso_session="$(aws configure get sso_session --profile "${AWS_PROFILE}")"; then
  echo "AWS CLI profile ${AWS_PROFILE} is not configured."
  echo "Please visit https://govukverify.atlassian.net/wiki/x/IgFm5 for instructions."
  exit 1
fi
if ! aws sts get-caller-identity --profile "${AWS_PROFILE}" > /dev/null; then
  aws sso login --sso-session "${sso_session}"
fi

configured_region="$(aws configure get region --profile "${AWS_PROFILE}" 2> /dev/null || true)"
export AWS_REGION="${configured_region:-eu-west-2}"

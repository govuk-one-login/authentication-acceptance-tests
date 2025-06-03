#!/usr/bin/env bash
set -euo pipefail

ENVIRONMENT="${1:-}"
if [ -z "${ENVIRONMENT}" ]; then
  echo "Usage: $0 <environment> [<browser>]"
  exit 1
fi

BROWSER="${2:-chromium}"

SP=false
case "${ENVIRONMENT}" in
  build-sp)
    SP=true
    AWS_PROFILE=di-authentication-build-admin
    ENVIRONMENT=build
    ;;
  staging-sp)
    SP=true
    AWS_PROFILE=di-authentication-staging-admin
    ENVIRONMENT=staging
    ;;
  build)
    AWS_PROFILE=gds-di-development-admin
    ;;
  staging)
    AWS_PROFILE=di-auth-staging-admin
    ;;
  dev)
    AWS_PROFILE=di-auth-development-admin
    ;;
  dev-sp)
    SP=true
    AWS_PROFILE=di-authentication-development-admin
    ENVIRONMENT=dev
    ;;
  *)
    echo "Unconfigured environment: $ENVIRONMENT"
    exit 1
    ;;
esac

[ -f api-env-override ] || touch api-env-override
[ -f ui-env-override ] || touch ui-env-override

export AWS_PROFILE
source ./scripts/check_aws_creds.sh
results_dir=./tmp/results/$(date +%Y-%m-%d-%H-%M-%S)
mkdir -p "${results_dir}"

if [ "${SP}" = "true" ]; then
  docker build . -t acceptance:latest --target ui \
    --build-arg "SELENIUM_BASE=selenium/standalone-${BROWSER}:132.0"
  docker run -p 4442-4444:4442-4444 --privileged \
    -e CODEBUILD_BUILD_ID=1 -e AWS_REGION="${AWS_REGION:-eu-west-2}" \
    -e TEST_ENVIRONMENT="${ENVIRONMENT}" -e PARALLEL_BROWSERS=1 \
    -v "${results_dir}":/test/results \
    --env-file <(aws configure export-credentials --format env-no-export) \
    -it --rm --entrypoint /bin/bash --shm-size="2g" acceptance:latest /run-ui-tests.sh
else
  docker build . -t acceptance:latest --target api \
    --build-arg "SELENIUM_BASE=selenium/standalone-${BROWSER}:132.0"

  docker run -p 4442-4444:4442-4444 --privileged \
    -e PARALLEL_BROWSERS=1 \
    -v "${results_dir}":/test/acceptance-tests/target/cucumber-report \
    --env-file <(aws configure export-credentials --format env-no-export) \
    -it --rm --entrypoint /bin/bash --shm-size="2g" \
    acceptance:latest /run-api-tests.sh -s "${ENVIRONMENT}"
fi

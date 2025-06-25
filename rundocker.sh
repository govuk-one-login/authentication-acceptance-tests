#!/usr/bin/env bash
set -euo pipefail

ENVIRONMENT="${1:-}"
if [ -z "${ENVIRONMENT}" ]; then
  echo "Usage: $0 <environment> [<base_image>]"

  echo "Example: $0 dev-ui"
  echo "Example: $0 staging-api selenium/standalone-firefox:132.0"
  exit 1
fi

BASE_IMAGE="${2:-}"

case "${ENVIRONMENT}" in
  authdev*-api)
    TEST_MODE=API
    AWS_PROFILE=di-auth-development-admin
    ENVIRONMENT=${ENVIRONMENT%-*}
    ;;
  authdev*-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-development-admin
    ENVIRONMENT=${ENVIRONMENT%-*}
    ;;
  dev-api)
    TEST_MODE=API
    AWS_PROFILE=di-auth-development-admin
    ENVIRONMENT=dev
    ;;
  dev-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-development-admin
    ENVIRONMENT=dev
    ;;
  build-api)
    TEST_MODE=API
    AWS_PROFILE=gds-di-development-admin
    ENVIRONMENT=build
    ;;
  build-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-build-admin
    ENVIRONMENT=build
    ;;
  staging-api)
    TEST_MODE=API
    AWS_PROFILE=di-auth-staging-admin
    ENVIRONMENT=staging
    ;;
  staging-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-staging-admin
    ENVIRONMENT=staging
    ;;
  *)
    echo "Unconfigured environment: $ENVIRONMENT"
    exit 1
    ;;
esac

create_local_env_file() {
  local override_file="$1"
  [ -f "${override_file}" ] || touch "${override_file}"

  scripts/fetch_envars.sh "${ENVIRONMENT}" | tee env-generated.env

  if [[ -f ${override_file} ]]; then
    printf "\nAdding local overrides to env file:\n"
    cat "${override_file}"
    cat "${override_file}" >> env-generated.env
  else
    printf "\nNo local overrides found\n"
  fi
}

export AWS_PROFILE
source ./scripts/check_aws_creds.sh
results_dir=./tmp/results/$(date +%Y-%m-%d-%H-%M-%S)
mkdir -p "${results_dir}"

BUILD_ARG_ARGS=()

if [ -n "${BASE_IMAGE}" ]; then
  echo "Using base image: ${BASE_IMAGE}"
  BUILD_ARG_ARGS+=("--build-arg" "SELENIUM_BASE=${BASE_IMAGE}")
fi

if [ "${TEST_MODE}" = "UI" ]; then
  docker build . -t ui-acceptance-tests:latest --target auth-ui \
    "${BUILD_ARG_ARGS[@]+"${BUILD_ARG_ARGS[@]}"}"

  create_local_env_file "env-override-ui.env"

  docker run -p 4442-4444:4442-4444 \
    -e CODEBUILD_BUILD_ID=1 \
    -e AWS_REGION="${AWS_REGION:-eu-west-2}" \
    -e TEST_ENVIRONMENT="${ENVIRONMENT}" \
    -e PARALLEL_BROWSERS=1 \
    -v "$(pwd)/env-generated.env:/test/.env" \
    -v "${results_dir}":/test/results \
    --env-file <(aws configure export-credentials --format env-no-export) \
    -it --rm --entrypoint /bin/bash --shm-size="2g" \
    ui-acceptance-tests:latest /run-tests.sh
fi

if [ "${TEST_MODE}" = "API" ]; then
  docker build . -t api-acceptance-tests:latest --target auth-api \
    "${BUILD_ARG_ARGS[@]+"${BUILD_ARG_ARGS[@]}"}"

  create_local_env_file "env-override-api.env"

  docker run -p 4442-4444:4442-4444 \
    -e PARALLEL_BROWSERS=1 \
    -v "$(pwd)/env-generated.env:/test/.env" \
    -v "${results_dir}":/test/acceptance-tests/target/cucumber-report \
    --env-file <(aws configure export-credentials --format env-no-export) \
    -it --rm --entrypoint /bin/bash --shm-size="2g" \
    api-acceptance-tests:latest /test/run-acceptance-tests.sh -s "${ENVIRONMENT}"
fi

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
  local-ui)
    TEST_MODE=UI
    ENVIRONMENT=local
    USE_LOCAL=true
    ;;
  authdev*-api)
    TEST_MODE=API
    AWS_PROFILE=di-auth-development-AdministratorAccessPermission
    ENVIRONMENT=${ENVIRONMENT%-*}
    ;;
  authdev*-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-development-AdministratorAccessPermission
    ENVIRONMENT=${ENVIRONMENT%-*}
    ;;
  dev-api)
    TEST_MODE=API
    AWS_PROFILE=di-auth-development-AdministratorAccessPermission
    ENVIRONMENT=dev
    ;;
  dev-ui)
    TEST_MODE=UI
    AWS_PROFILE=di-authentication-development-AdministratorAccessPermission
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

  if [ "${USE_LOCAL:-false}" = "true" ]; then
    echo "Using local environment configuration..."
    cp .env.local env-generated.env
  else
    scripts/fetch_envars.sh "${ENVIRONMENT}" | tee env-generated.env
  fi

  if [[ -f ${override_file} ]]; then
    printf "\nAdding local overrides to env file:\n"
    cat "${override_file}"
    cat "${override_file}" >> env-generated.env
  else
    printf "\nNo local overrides found\n"
  fi
}

export AWS_PROFILE
if [ "${USE_LOCAL:-false}" != "true" ]; then
  source ./scripts/check_aws_creds.sh
fi

# Cleanup old test results (keep only last 10 runs)
if [ -d "./test-reports" ]; then
  find ./test-reports -maxdepth 1 -type d -name "20*" | sort -r | tail -n +11 | xargs rm -rf 2> /dev/null || true
fi

results_dir=./test-reports/$(date +%Y-%m-%d-%H-%M-%S)
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

  DOCKER_RUN_ARGS=(
    -p 4442-4444:4442-4444
    -e PARALLEL_BROWSERS=1
    -v "$(pwd)/env-generated.env:/test/.env"
    -v "${results_dir}":/test/results
    --rm --entrypoint /bin/bash --shm-size="2g"
  )

  if [ "${USE_LOCAL:-false}" = "true" ]; then
    DOCKER_RUN_ARGS+=(
      --network host
      -e CODEBUILD_BUILD_ID=local
      -e TEST_ENVIRONMENT=local
      -e AWS_REGION="${AWS_REGION:-eu-west-2}"
    )
  else
    DOCKER_RUN_ARGS+=(
      -e CODEBUILD_BUILD_ID=1
      -e AWS_REGION="${AWS_REGION:-eu-west-2}"
      -e TEST_ENVIRONMENT="${ENVIRONMENT}"
      --env-file <(aws configure export-credentials --format env-no-export)
    )
  fi

  docker run "${DOCKER_RUN_ARGS[@]}" \
    ui-acceptance-tests:latest /run-tests.sh
fi

if [ "${TEST_MODE}" = "API" ]; then

  create_local_env_file "env-override-api.env"

  # Source the generated env file to make variables available to this script
  set -a
  if [[ -f env-generated.env ]]; then
    # shellcheck disable=SC1091
    source env-generated.env
  fi
  set +a

  if [ "${HOST_ENVIRONMENT:-}" == "local" ]; then
    # Must have authentication-api cloned in above directory
    # Creates bastion host to access private API outside of VPC
    API_PROXY_PORT=${API_PROXY_PORT:-8123}
    echo "Starting API proxy on port $API_PROXY_PORT..."
    ../authentication-api/scripts/api-proxy.sh account-management "${ENVIRONMENT}" "$API_PROXY_PORT" &
    API_PROXY_PID=$!
  fi

  docker build . -t api-acceptance-tests:latest --target auth-api \
    "${BUILD_ARG_ARGS[@]+"${BUILD_ARG_ARGS[@]}"}"

  docker run -p 4442-4444:4442-4444 \
    -e PARALLEL_BROWSERS=1 \
    -v "$(pwd)/env-generated.env:/test/.env" \
    -v "${results_dir}":/test/acceptance-tests/target/cucumber-report \
    --env-file <(aws configure export-credentials --format env-no-export) \
    -it --rm --entrypoint /bin/bash --shm-size="2g" \
    api-acceptance-tests:latest /test/run-acceptance-tests.sh -s "${ENVIRONMENT}"

  # Clean up API proxy
  if [ -n "${API_PROXY_PID:-}" ]; then
    kill "$API_PROXY_PID" 2> /dev/null || true
  fi
fi

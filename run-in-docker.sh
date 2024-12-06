#!/usr/bin/env bash
set -euo pipefail

ENVIRONMENT="${1?Usage: $0 <environment> [# parallel_browsers]}"
export PARALLEL_BROWSERS="${2:-1}"

export AWS_PROFILE="${AWS_PROFILE?AWS_PROFILE must be set}"

docker build . -t acceptance:latest -f Dockerfile.chromium
source ./scripts/check_aws_creds.sh
docker run -p 4442-4444:4442-4444 --privileged -e PARALLEL_BROWSERS="${PARALLEL_BROWSERS}" --env-file <(aws configure export-credentials --format env-no-export) -it --rm --entrypoint /bin/bash --shm-size="2g" acceptance:latest /auth-docker-entrypoint.sh "${ENVIRONMENT}"

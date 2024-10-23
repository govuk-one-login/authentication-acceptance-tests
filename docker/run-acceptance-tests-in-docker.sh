#!/usr/bin/env bash
set -euo pipefail

export ENVIRONMENT="${1}"

if [ -z "${AWS_REGION:-}" ]; then
  export AWS_REGION="eu-west-2"
fi

./gradlew --no-daemon cucumber

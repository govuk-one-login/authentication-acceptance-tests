#!/usr/bin/env bash
set -euo pipefail

export ENVIRONMENT="${2}"

if [ -z "${AWS_REGION:-}" ]; then
  export AWS_REGION="eu-west-2"
fi

pushd /test > /dev/null || exit 1
./gradlew --no-daemon cucumber
popd > /dev/null || exit 1

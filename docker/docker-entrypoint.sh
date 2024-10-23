#!/usr/bin/env bash
set -euxo pipefail

ENVIRONMENT="${1}"

if [ "${ENVIRONMENT}" == "-r" ]; then
  ENVIRONMENT="${2}"
fi

nohup /opt/bin/entry_point.sh &

timeout 15 sh -c "until curl -sqL 127.0.0.1:4444 > /dev/null; do echo .; sleep 1; done"

pushd /test > /dev/null || exit 1
exec /test/docker/run-acceptance-tests-in-docker.sh "${ENVIRONMENT}"

#!/usr/bin/env bash
set -euo pipefail

export ENVIRONMENT="${2}"

if [ -z "${AWS_REGION:-}" ]; then
  export AWS_REGION="eu-west-2"
fi

pushd /test > /dev/null || exit 1

if [ -f ".env" ]; then
  # source .env file if it exists (as we're running in docker, it's definitely needed if it's there)
  # shellcheck source=/dev/null
  set -o allexport && source .env && set +o allexport
else
  echo "No .env file found"
fi

echo
echo "********************************************************************************************"
echo "CUCUMBER FILTER TAGS: ${CUCUMBER_FILTER_TAGS}"
echo "********************************************************************************************"
echo

SELENIUM_BROWSER="$(cat /opt/selenium/browser_name)"
export SELENIUM_BROWSER

./gradlew --no-daemon cucumber

popd > /dev/null || exit 1

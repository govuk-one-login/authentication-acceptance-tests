#!/bin/bash

set -u

ENVIRONMENT=${TEST_ENVIRONMENT:-local}
export CUCUMBER_FILTER_TAGS=${CUCUMBER_FILTER_TAGS:-"@build-sp"}

/test/run-acceptance-tests.sh -r "${ENVIRONMENT}"
return_code=$?

if [ -d "/test/acceptance-tests/target/cucumber-report/" ]; then
  cp -r /test/acceptance-tests/target/cucumber-report/* "$(pwd)"/results/
fi

exit $return_code

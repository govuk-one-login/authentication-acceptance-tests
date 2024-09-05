#!/bin/bash

set -u

ENVIRONMENT=${TEST_ENVIRONMENT:-local}
export CUCUMBER_FILTER_TAGS="@build-sp"

/test/run-acceptance-tests.sh -r ${ENVIRONMENT}
return_code=$?

cp -r /test/acceptance-tests/target/cucumber-report/* $(pwd)/results/

exit $return_code

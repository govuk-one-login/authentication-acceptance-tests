#!/bin/bash

set -eu

ENVIRONMENT=${TEST_ENVIRONMENT:-local}

/test/run-acceptance-tests.sh -r ${ENVIRONMENT}

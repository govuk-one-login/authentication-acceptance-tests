#!/usr/bin/env bash

set -euo pipefail

ENVIRONMENT="${1?Please provide the environment to fetch environment variables for.}"

envars="$(aws ssm get-parameters-by-path --with-decryption --path "/acceptance-tests/${ENVIRONMENT}" \
  | jq -r '.Parameters[] | [(.Name|split("/")|last), .Value]|@tsv')"

while IFS=$'\t' read -r name value; do
  echo "${name}='${value}'"
done <<< "${envars}"

#!/bin/bash

set -eu

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" > /dev/null 2>&1 && pwd)"

envvalue=("authdev1" "authdev2" "dev")

select word in "${envvalue[@]}"; do
  if [[ -z ${word} ]]; then
    printf '"%s" is not a valid choice\n' "${REPLY}" >&2
  else
    user_in="$((REPLY - 1))"
    break
  fi
done

for ((i = 0; i < ${#envvalue[@]}; ++i)); do
  if ((i == user_in)); then
    printf 'You picked "%s"\n' "${envvalue[$i]}"
    export ENVIRONMENT=${envvalue[$i]}
    printf "Running AC tests against %s\n" "${ENVIRONMENT}"
    read -r -p "Press enter to continue or Ctrl+C to abort"
  fi
done

if [[ ${ENVIRONMENT} == authdev* ]] || [[ ${ENVIRONMENT} == "dev" ]]; then
<<<<<<< HEAD
  export AWS_PROFILE="di-auth-development-AdministratorAccessPermission"
=======
  export AWS_PROFILE="di-auth-development-admin"
>>>>>>> 71a029c (AUT-4727: Removing sandpit config from respo)
else
  echo "Unknown environment: ${ENVIRONMENT}"
  exit 1
fi

"${DIR}"/run-acceptance-tests.sh -s "${ENVIRONMENT}"

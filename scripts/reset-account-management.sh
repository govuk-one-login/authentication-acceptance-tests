#!/usr/bin/env bash

function resetAccountManagement() {
  createSMSUser \
    "${TEST_USER_ACCOUNT_MANAGEMENT_EMAIL}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  deleteUser "${NONEXISTENT_USER_ACCOUNT_MANAGEMENT_EMAIL}"
}

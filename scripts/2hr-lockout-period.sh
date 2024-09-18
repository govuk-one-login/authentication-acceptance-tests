#!/usr/bin/env bash

function 2hrLockoutPeriod() {

  createSMSUser \
    "${INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${PASSWORD_RESET_SMS_USER_1}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${PASSWORD_RESET_SMS_USER_2}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createAuthAppUser \
    "${PASSWORD_RESET_AUTH_APP_USER_1}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${TEST_USER_ACCOUNT_RECOVERY_EMAIL_3}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${ACCOUNT_RECOVERY_REQUEST_SMS_CODE_LOCKOUT}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${ACCOUNT_RECOVERY_INCORRECT_AUTH_APP_CODE_NO_LIMIT}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${ACCOUNT_RECOVERY_INCORRECT_EMAIL_CODE}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${ACCOUNT_RECOVERY_INCORRECT_SMS_CODE}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${SIGN_IN_INCORRECT_PASSWORD_LOCKOUT}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${SIGN_IN_INCORRECT_SMS_CODE_LOCKOUT}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${SIGN_IN_REQUEST_SMS_CODE_LOCKOUT}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createAuthAppUser \
    "${SIGN_IN_AUTH_APP_CODE_LOCKOUT}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  createSMSUser \
    "${SIGN_IN_INCORRECT_PASSWORD_LOCKOUT_RESET_PW}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}"

  deleteUser "${CREATE_INCORRECT_EMAIL_CODE_LOCKOUT}"
  deleteUser "${CREATE_INCORRECT_EMAIL_CODE_LOCKOUT_1}"
  deleteUser "${CREATE_REQUEST_EMAIL_CODE_LOCKOUT}"
  deleteUser "${CREATE_INCORRECT_SMS_CODE_LOCKOUT}"
  deleteUser "${CREATE_REQUEST_SMS_CODE_LOCKOUT}"
  deleteUser "${CREATE_INCORRECT_AUTH_APP_CODE_LOCKOUT}"

  removeMfaMethods "${ACCOUNT_RECOVERY_INCORRECT_SMS_CODE}"

  updateMfaSMS "${ACCOUNT_RECOVERY_INCORRECT_SMS_CODE}" "${TEST_USER_PHONE_NUMBER}" "1"
  updateAccountRecoveryBlock "${TEST_USER_ACCOUNT_RECOVERY_EMAIL_3}" "${SECTOR_HOST}"

  resetPassword "${SIGN_IN_INCORRECT_PASSWORD_LOCKOUT_RESET_PW}" "${TEST_USER_PASSWORD}"

}

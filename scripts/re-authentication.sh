#!/usr/bin/env bash

function reAuthentication() {

  createSMSUser \
    $TEST_USER_REAUTH_SMS_1 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_2 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_3 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_4 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_5 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_6 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createSMSUser \
    $TEST_USER_REAUTH_SMS_7 \
    $TEST_USER_PASSWORD \
    $TEST_USER_PHONE_NUMBER \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createAuthAppUser \
    $TEST_USER_REAUTH_AUTH_APP_1 \
    $TEST_USER_PASSWORD \
    $ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createAuthAppUser \
    $TEST_USER_REAUTH_AUTH_APP_2 \
    $TEST_USER_PASSWORD \
    $ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  createAuthAppUser \
    $TEST_USER_REAUTH_AUTH_APP_3 \
    $TEST_USER_PASSWORD \
    $ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET \
    $TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION

  }
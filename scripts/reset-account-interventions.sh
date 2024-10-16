#!/usr/bin/env bash

function resetAccountInterventions() {

  #  # Have to delete existing account interventions here as any update to the user in user-profile resets the
  #  # PublicSubjectID and SubjectID fields which are used to generate the pairwise id for the intervention, which then
  #  # breaks the link between the user and any existing intervention in the stub table, and leaves an orphaned intervention

  deleteIntervention "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1}" &
  deleteIntervention "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2}" &
  deleteIntervention "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_3}" &
  deleteIntervention "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_1}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_3}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_4}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_5}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6}" &
  deleteIntervention "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_7}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6}" &
  deleteIntervention "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_7}" &
  deleteIntervention "${ACCOUNT_INTERVENTION_REMOVED_EMAIL_1}" &
  wait

  createSMSUser \
    "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_3}" \
    "${TOP_100K_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_1}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_3}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_4}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_5}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_7}" \
    "${TOP_100K_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${ACCOUNT_INTERVENTION_REMOVED_EMAIL_1}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1}" \
    "${TEST_USER_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createSMSUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5}" \
    "${TOP_100K_PASSWORD}" \
    "${TEST_USER_PHONE_NUMBER}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &

  createAuthAppUser \
    "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_7}" \
    "${TEST_USER_PASSWORD}" \
    "${ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET}" \
    "${TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION}" &
  wait

  updateTermsAndConditions "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4}" "1.0" &
  updateTermsAndConditions "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6}" "1.0" &
  updateTermsAndConditions "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6}" "1.0" &
  wait

  createSuspendedAccountInterventionsBlock "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1}" "${SECTOR_HOST}" &
  createSuspendedAccountInterventionsBlock "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2}" "${SECTOR_HOST}" &
  createSuspendedAccountInterventionsBlock "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_3}" "${SECTOR_HOST}" &
  createSuspendedAccountInterventionsBlock "${TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_1}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_3}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_4}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_5}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6}" "${SECTOR_HOST}" &
  createBlockedAccountInterventionsBlock "${PERMANENTLY_LOCKED_ACCOUNT_EMAIL_7}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6}" "${SECTOR_HOST}" &
  createResetPasswordInterventionsBlock "${TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_7}" "${SECTOR_HOST}" &
  removeAccountInterventionBlocks "${ACCOUNT_INTERVENTION_REMOVED_EMAIL_1}" "${SECTOR_HOST}" &
  wait
}

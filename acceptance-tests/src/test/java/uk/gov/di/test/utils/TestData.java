package uk.gov.di.test.utils;

import static uk.gov.di.test.utils.Constants.TOP_100K_PASSWORD;

public class TestData {
    private Dynamo dynamo = new Dynamo();

    String testPhoneNumber = System.getenv("TEST_USER_PHONE_NUMBER");

    public void SetupUserData(String emailAddressDataTag) {

        switch (emailAddressDataTag.toUpperCase()) {
            case "TEST_USER_EMAIL":
            case "TEST_USER_EMAIL_2":
            case "TEST_USER_EMAIL_3":
            case "TEST_USER_EMAIL_4":
            case "TEST_USER_AUTH_APP_EMAIL":
            case "IPN1_NEW_USER_EMAIL":
            case "IPN2_NEW_USER_EMAIL":
            case "CREATE_INCORRECT_EMAIL_CODE_LOCKOUT":
            case "CREATE_INCORRECT_EMAIL_CODE_LOCKOUT_1":
            case "CREATE_REQUEST_EMAIL_CODE_LOCKOUT":
            case "CREATE_INCORRECT_SMS_CODE_LOCKOUT":
            case "CREATE_REQUEST_SMS_CODE_LOCKOUT":
            case "CREATE_INCORRECT_AUTH_APP_CODE_LOCKOUT":
            case "TEST_USER_STATE_PRESERVATION_EMAIL1":
            case "TEST_USER_STATE_PRESERVATION_EMAIL2":
            case "NONEXISTENT_USER_ACCOUNT_MANAGEMENT_EMAIL":
                dynamo.deleteUser(emailAddressDataTag);
                break;

            case "TEST_USER_2_EMAIL":
            case "TEST_USER_4_EMAIL":
            case "TEST_USER_5_EMAIL":
            case "TEST_USER_6_EMAIL":
            case "ACCOUNT_RECOVERY_INCORRECT_EMAIL_CODE":
            case "TEST_USER_ACCOUNT_RECOVERY_EMAIL_3":
            case "ACCOUNT_RECOVERY_INCORRECT_SMS_CODE":
            case "ACCOUNT_RECOVERY_REQUEST_SMS_CODE_LOCKOUT":
            case "INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL":
            case "TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL":
            case "PASSWORD_RESET_SMS_USER_1":
            case "PASSWORD_RESET_SMS_USER_2":
            case "TEST_USER_REAUTH_SMS_1":
            case "TEST_USER_REAUTH_SMS_2":
            case "TEST_USER_REAUTH_SMS_3":
            case "TEST_USER_REAUTH_SMS_4":
            case "TEST_USER_REAUTH_SMS_5":
            case "TEST_USER_REAUTH_SMS_6":
            case "TEST_USER_REAUTH_SMS_8":
            case "TEST_USER_REAUTH_SMS_9":
            case "TEST_USER_ACCOUNT_MANAGEMENT_EMAIL":
            case "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT":
            case "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT_RESET_PW":
            case "SIGN_IN_INCORRECT_SMS_CODE_LOCKOUT":
            case "SIGN_IN_REQUEST_SMS_CODE_LOCKOUT":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                break;

            case "":
                // NO USER REQUIRED
                break;

            case "TERMS_AND_CONDITIONS_TEST_USER_EMAIL":
                dynamo.createUpdateSmsUserTermsAndConditionsOutOfDate(emailAddressDataTag);
                break;

            case "TEST_USER_AUTH_APP_EMAIL_2":
            case "TEST_USER_AUTH_APP_EMAIL_3":
            case "TEST_USER_REAUTH_AUTH_APP_1":
            case "TEST_USER_REAUTH_AUTH_APP_2":
            case "TEST_USER_REAUTH_AUTH_APP_3":
            case "PASSWORD_RESET_AUTH_APP_USER_1":
            case "SIGN_IN_AUTH_APP_CODE_LOCKOUT":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                break;

            case "RESET_PW_USER_EMAIL_2":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.updateAccountRecoveryBlock(emailAddressDataTag);
                break;

            case "RESET_PW_USER_EMAIL":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                dynamo.updateAccountRecoveryBlock(emailAddressDataTag);
                break;

            case "REQ_RESET_PW_USER_EMAIL": // TOP 100k PW - SMS USER
                dynamo.createOrUpdateUser(emailAddressDataTag, TOP_100K_PASSWORD, 1, 1);
                dynamo.updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
                dynamo.updateTermsAndConditions(emailAddressDataTag, "latest");
                dynamo.updateAccountRecoveryBlock(emailAddressDataTag);
                break;

            case "REQ_RESET_PW_USER_EMAIL_2": // TOP 100k PW - AUTH APP USER
                dynamo.createOrUpdateUser(emailAddressDataTag, TOP_100K_PASSWORD, 1, 1);
                dynamo.updateMfaToAuthApp(emailAddressDataTag);
                dynamo.updateTermsAndConditions(emailAddressDataTag, "latest");
                dynamo.updateAccountRecoveryBlock(emailAddressDataTag);
                break;

            case "ACCOUNT_RECOVERY_INCORRECT_AUTH_APP_CODE_NO_LIMIT":
            case "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1":
            case "TEST_USER_REAUTH_SMS_7":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                dynamo.removeMfaMethod(emailAddressDataTag);
                dynamo.updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
                break;

            case "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.removeMfaMethod(emailAddressDataTag);
                dynamo.updateMfaToAuthApp(emailAddressDataTag);
                break;

            case "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, false, true);
                break;

            case "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, false, true);
                break;

            case "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_3":
                dynamo.createOrUpdateUser(emailAddressDataTag, TOP_100K_PASSWORD, 1, 1);
                dynamo.updateMfaToAuthApp(emailAddressDataTag);
                dynamo.updateTermsAndConditions(emailAddressDataTag, "latest");
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, false, true);
                break;

            case "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4":
                dynamo.createUpdateSmsUserTermsAndConditionsOutOfDate(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, false, true);
                break;

            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_1":
            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_3":
            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_4":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, true, false, false, false);
                break;

            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2":
            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_5":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, true, false, false, false);
                break;

            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6":
                dynamo.createUpdateAuthAppUserTermsAndConditionsOutOfDate(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, true, false, false, false);
                break;

            case "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_7":
                dynamo.createOrUpdateUser(emailAddressDataTag, TOP_100K_PASSWORD, 1, 1);
                dynamo.updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, true, false, false, false);
                break;

            case "ACCOUNT_INTERVENTION_REMOVED_EMAIL_1":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, false, false);
                break;

            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1":
                dynamo.createUpdateStandardSmsUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, true, true);
                break;

            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2":
            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, true, true);
                break;

            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5":
                dynamo.createOrUpdateUser(emailAddressDataTag, TOP_100K_PASSWORD, 1, 1);
                dynamo.updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, true, true);
                break;

            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6":
                dynamo.createUpdateAuthAppUserTermsAndConditionsOutOfDate(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, true, true);
                break;

            case "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_7":
                dynamo.createUpdateStandardAuthAppUser(emailAddressDataTag);
                dynamo.updateAccountRecoveryBlock(emailAddressDataTag);
                dynamo.createUpdateAccountInterventionForUser(
                        emailAddressDataTag, false, false, true, true);
                break;

            default:
                throw new RuntimeException("Data tag " + emailAddressDataTag + " not recognised");
        }
    }
}

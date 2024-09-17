package uk.gov.di.test.controllers;

import org.apache.commons.text.StringSubstitutor;
import uk.gov.di.test.entity.MFAMethod;
import uk.gov.di.test.entity.TermsAndConditions;
import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Crypto;
import uk.gov.di.test.utils.Environment;
import uk.gov.di.test.utils.PasswordGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static uk.gov.di.test.utils.Constants.DEFAULT_TIMESTAMP;
import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER;

public class UserLifecycleController {
    private static volatile UserLifecycleController instance;
    protected static volatile SecretsManagerController secretsManagerController =
            SecretsManagerController.getInstance();

    private final Long instantiationMillis;
    private final Map<String, String> baseEmailFormatValues;

    protected static final String ENVIRONMENT = Environment.getOrThrow("ENVIRONMENT");

    private UserLifecycleController() {
        // Private constructor to prevent direct instantiation
        instantiationMillis = System.currentTimeMillis();
        baseEmailFormatValues =
                Map.of(
                        "environment",
                        ENVIRONMENT,
                        "instantiationMillis",
                        String.valueOf(instantiationMillis));
    }

    public static UserLifecycleController getInstance() {
        if (instance == null) {
            synchronized (UserLifecycleController.class) {
                if (instance == null) {
                    instance = new UserLifecycleController();
                }
            }
        }
        return instance;
    }

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static final DynamoDbController dynamoDbController = DynamoDbController.getInstance();

    private static final AtomicLong emailSubAddressCounter = new AtomicLong();

    private String generateNewUniqueEmailAddress() {
        Map<String, String> values = new HashMap<>(baseEmailFormatValues);
        values.put("counter", String.valueOf(emailSubAddressCounter.getAndIncrement()));
        StringSubstitutor sub = new StringSubstitutor(values);
        return sub.replace(
                secretsManagerController.getSecretValue("acceptance-test-email-address-format"));
    }

    private TermsAndConditions buildTermsAndConditions(String version) {
        return new TermsAndConditions(version, DEFAULT_TIMESTAMP);
    }

    public TermsAndConditions buildTermsAndConditions() {
        return buildTermsAndConditions(
                secretsManagerController.getDeploySecretValue(
                        "test_user_latest_terms_and_conditions"));
    }

    public TermsAndConditions buildOldTermsAndConditions() {
        TermsAndConditions termsAndConditions = buildTermsAndConditions();
        return termsAndConditions.withVersion(
                Double.parseDouble(termsAndConditions.getVersion()) - 0.1);
    }

    public MFAMethod buildAppMfaMethod(String secret) {
        return new MFAMethod()
                .withCredentialValue(secret)
                .withEnabled(true)
                .withMethodVerified(true)
                .withMfaMethodType("AUTH_APP")
                .withUpdated(DEFAULT_TIMESTAMP);
    }

    public MFAMethod buildAppMfaMethod() {
        return buildAppMfaMethod(
                secretsManagerController.getDeploySecretValue(
                        "test_user_pw_reset_auth_app_secret"));
    }

    public static String generateValidPassword() {
        return passwordGenerator.generatePassword();
    }

    public void changeUserPassword(
            String newPassword, UserProfile userProfile, UserCredentials userCredentials) {
        String encodedPassword = Crypto.encodePassword(newPassword, userProfile.getSalt());
        userCredentials.setPassword(encodedPassword);
        updateUserProfileInDynamodb(userProfile);
        updateUserCredentialsInDynamodb(userCredentials);
    }

    public void putUserProfileToDynamodb(UserProfile userProfile) {
        dynamoDbController.putUserProfile(userProfile);
    }

    public void updateUserProfileInDynamodb(UserProfile userProfile) {
        dynamoDbController.updateUserProfile(userProfile);
    }

    public void deleteUserProfileFromDynamodb(UserProfile userProfile) {
        dynamoDbController.deleteUserProfile(userProfile);
    }

    public UserProfile buildNewUserProfile() {
        return new UserProfile()
                .withTestUser(1)
                .withEmail(generateNewUniqueEmailAddress())
                .withPhoneNumber(UK_MOBILE_PHONE_NUMBER)
                .withPublicSubjectID(UUID.randomUUID().toString())
                .withSubjectID(UUID.randomUUID().toString())
                .withSalt(Crypto.generateSalt())
                .withTermsAndConditions(buildTermsAndConditions());
    }

    public UserProfile buildNewUserProfileAndPutToDynamodb() {
        UserProfile userProfile = buildNewUserProfile();
        putUserProfileToDynamodb(userProfile);
        return userProfile;
    }

    public void updateUserMfaSms(UserProfile userProfile) {
        userProfile.setAccountVerified(1);
        userProfile.setPhoneNumber(UK_MOBILE_PHONE_NUMBER);
        userProfile.setPhoneNumberVerified(true);
        updateUserProfileInDynamodb(userProfile);
    }

    public void updateUserMfaApp(UserCredentials userCredentials) {
        userCredentials.setMfaMethod(buildAppMfaMethod());
        updateUserCredentialsInDynamodb(userCredentials);
    }

    public void updateUserTermsAndConditions(
            UserProfile userProfile, TermsAndConditions termsAndConditions) {
        userProfile.setTermsAndConditions(termsAndConditions);
        updateUserProfileInDynamodb(userProfile);
    }

    public void putUserCredentialsToDynamodb(UserCredentials userCredentials) {
        dynamoDbController.putUserCredentials(userCredentials);
    }

    public void updateUserCredentialsInDynamodb(UserCredentials userCredentials) {
        dynamoDbController.updateUserCredentials(userCredentials);
    }

    public void deleteUserCredentialsFromDynamodb(UserCredentials userCredentials) {
        dynamoDbController.deleteUserCredentials(userCredentials);
    }

    public UserCredentials buildNewUserCredentials(UserProfile userProfile, String userPassword) {
        String encodedPassword = Crypto.encodePassword(userPassword, userProfile.getSalt());
        return new UserCredentials()
                .withTestUser(1)
                .withEmail(userProfile.getEmail())
                .withPassword(encodedPassword)
                .withSubjectID(userProfile.getSubjectID());
    }

    public UserCredentials buildNewUserCredentialsAndPutToDynamodb(
            UserProfile userProfile, String userPassword) {
        UserCredentials userCredentials = buildNewUserCredentials(userProfile, userPassword);
        putUserCredentialsToDynamodb(userCredentials);
        return userCredentials;
    }
}

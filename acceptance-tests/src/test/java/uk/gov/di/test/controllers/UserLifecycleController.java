package uk.gov.di.test.controllers;

import uk.gov.di.test.entity.MFAMethod;
import uk.gov.di.test.entity.TermsAndConditions;
import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Crypto;
import uk.gov.di.test.utils.Environment;
import uk.gov.di.test.utils.PasswordGenerator;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserLifecycleController {
    private static volatile UserLifecycleController instance;

    private final Long instantiationMillis;

    private UserLifecycleController() {
        // Private constructor to prevent direct instantiation
        instantiationMillis = System.currentTimeMillis();
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

    private static final String DEFAULT_TIMESTAMP = "1970-01-01T00:00:00.000000";
    private static final String TEST_USER_PHONE_NUMBER = "07700900000";

    protected static final String ENVIRONMENT = Environment.getOrThrow("ENVIRONMENT");

    protected static final String TEST_EMAIL_COUNTER_START =
            Environment.getOrThrow("TEST_EMAIL_COUNTER_START");
    protected static final String TEST_USER_EMAIL_USER =
            Environment.getOrThrow("TEST_USER_EMAIL_USER");
    protected static final String TEST_USER_EMAIL_DOMAIN =
            Environment.getOrThrow("TEST_USER_EMAIL_DOMAIN");
    protected static final String TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION =
            Environment.getOrThrow("TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION");
    protected static final String ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET =
            Environment.getOrThrow("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static final DynamoDbController dynamoDbController = DynamoDbController.getInstance();

    private static final AtomicLong emailSubAddressCounter =
            new AtomicLong(Long.parseLong(TEST_EMAIL_COUNTER_START));

    private String generateNewUniqueEmailAddress() {
        return String.format(
                "%s+%s-%d-%d@%s",
                TEST_USER_EMAIL_USER,
                ENVIRONMENT,
                instantiationMillis,
                emailSubAddressCounter.getAndIncrement(),
                TEST_USER_EMAIL_DOMAIN);
    }

    private TermsAndConditions buildTermsAndConditions(String version) {
        return new TermsAndConditions(version, DEFAULT_TIMESTAMP);
    }

    public TermsAndConditions buildTermsAndConditions() {
        return buildTermsAndConditions(TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION);
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
        return buildAppMfaMethod(ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET);
    }

    public static String generateValidPassword() {
        return passwordGenerator.generatePassword(10);
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
                .withEmail(generateNewUniqueEmailAddress())
                .withPublicSubjectID(UUID.randomUUID().toString())
                .withSubjectID(UUID.randomUUID().toString())
                .withSalt(Crypto.generateSalt())
                .withPassword(generateValidPassword())
                .withTermsAndConditions(buildTermsAndConditions());
    }

    public UserProfile buildNewUserProfileAndPutToDynamodb() {
        UserProfile userProfile = buildNewUserProfile();
        putUserProfileToDynamodb(userProfile);
        return userProfile;
    }

    public void updateUserMfaSms(UserProfile userProfile) {
        userProfile.setMfaToSms(TEST_USER_PHONE_NUMBER);
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

    public UserCredentials buildNewUserCredentials(UserProfile userProfile) {
        String encodedPassword =
                Crypto.encodePassword(userProfile.getPassword(), userProfile.getSalt());
        return new UserCredentials()
                .withEmail(userProfile.getEmail())
                .withPassword(encodedPassword)
                .withSubjectID(userProfile.getSubjectID());
    }

    public UserCredentials buildNewUserCredentialsAndPutToDynamodb(UserProfile userProfile) {
        UserCredentials userCredentials = buildNewUserCredentials(userProfile);
        putUserCredentialsToDynamodb(userCredentials);
        return userCredentials;
    }
}

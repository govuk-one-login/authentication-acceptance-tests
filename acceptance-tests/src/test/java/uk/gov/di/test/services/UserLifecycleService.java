package uk.gov.di.test.services;

import org.apache.commons.text.StringSubstitutor;
import uk.gov.di.test.entity.MFAMethod;
import uk.gov.di.test.entity.MFAMethodType;
import uk.gov.di.test.entity.PriorityIdentifier;
import uk.gov.di.test.entity.TermsAndConditions;
import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Constants;
import uk.gov.di.test.utils.Crypto;
import uk.gov.di.test.utils.Environment;
import uk.gov.di.test.utils.PasswordGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static uk.gov.di.test.utils.Constants.*;

public class UserLifecycleService {
    private static volatile UserLifecycleService instance;

    private static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

    private final Map<String, String> baseEmailFormatValues;
    private final String emailAddressFormat;

    private static final String ENVIRONMENT = Environment.getOrThrow("ENVIRONMENT");

    private UserLifecycleService() {
        baseEmailFormatValues =
                Map.of(
                        "environment",
                        ENVIRONMENT,
                        "instantiationMillis",
                        String.valueOf(System.currentTimeMillis()));
        emailAddressFormat = TEST_CONFIG_SERVICE.get("EMAIL_ADDRESS_FORMAT");
    }

    public static UserLifecycleService getInstance() {
        if (instance == null) {
            synchronized (UserLifecycleService.class) {
                if (instance == null) {
                    instance = new UserLifecycleService();
                }
            }
        }
        return instance;
    }

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();

    private static final AtomicLong emailSubAddressCounter = new AtomicLong();

    public String generateNewUniqueEmailAddress() {
        Map<String, String> values = new HashMap<>(baseEmailFormatValues);
        values.put("counter", String.valueOf(emailSubAddressCounter.getAndIncrement()));
        StringSubstitutor sub = new StringSubstitutor(values);
        return sub.replace(emailAddressFormat);
    }

    private TermsAndConditions buildTermsAndConditions(String version) {
        return new TermsAndConditions(version, DEFAULT_TIMESTAMP);
    }

    public TermsAndConditions buildTermsAndConditions() {
        return buildTermsAndConditions(TEST_CONFIG_SERVICE.get("TERMS_AND_CONDITIONS_VERSION"));
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
        return buildAppMfaMethod(Constants.AUTH_APP_SECRET);
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
        DYNAMO_DB_SERVICE.putUserProfile(userProfile);
    }

    public void updateUserProfileInDynamodb(UserProfile userProfile) {
        DYNAMO_DB_SERVICE.updateUserProfile(userProfile);
    }

    public void deleteUserProfileFromDynamodb(UserProfile userProfile) {
        DYNAMO_DB_SERVICE.deleteUserProfile(userProfile);
    }

    public UserProfile buildNewUserProfile() {
        return new UserProfile()
                .withTestUser(1)
                .withEmail(generateNewUniqueEmailAddress())
                .withPhoneNumber(UK_MOBILE_PHONE_NUMBER_WITH_INTERNATIONAL_DIALING_CODE)
                .withPhoneNumberVerified(true)
                .withPublicSubjectID(UUID.randomUUID().toString())
                .withSubjectID(UUID.randomUUID().toString())
                .withSalt(Crypto.generateSalt())
                .withTermsAndConditions(buildTermsAndConditions());
    }

    public UserProfile buildNewMigratedUserProfile() {
        return new UserProfile()
                .withTestUser(1)
                .withEmail(generateNewUniqueEmailAddress())
                .withPublicSubjectID(UUID.randomUUID().toString())
                .withSubjectID(UUID.randomUUID().toString())
                .withSalt(Crypto.generateSalt())
                .withMfaMigratedUser(true)
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
        DYNAMO_DB_SERVICE.putUserCredentials(userCredentials);
    }

    public void updateUserCredentialsInDynamodb(UserCredentials userCredentials) {
        DYNAMO_DB_SERVICE.updateUserCredentials(userCredentials);
    }

    public void deleteUserCredentialsFromDynamodb(UserCredentials userCredentials) {
        DYNAMO_DB_SERVICE.deleteUserCredentials(userCredentials);
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

    public UserCredentials buildNewMigratedUserCredentials(
            UserProfile userProfile, String userPassword, MFAMethodType methodType) {
        String encodedPassword = Crypto.encodePassword(userPassword, userProfile.getSalt());
        MFAMethod methodToAdd = getMfaMethod(methodType);

        return new UserCredentials()
                .withTestUser(1)
                .withEmail(userProfile.getEmail())
                .withPassword(encodedPassword)
                .withSubjectID(userProfile.getSubjectID())
                .withMfaMethods(List.of(methodToAdd));
    }

    private static MFAMethod getMfaMethod(MFAMethodType methodType) {
        MFAMethod methodToAdd;
        if (methodType == MFAMethodType.AUTH_APP) {
            methodToAdd =
                    new MFAMethod(
                            MFAMethodType.AUTH_APP.name(),
                            "credential",
                            true,
                            true,
                            "test",
                            PriorityIdentifier.DEFAULT,
                            "1");
        } else {
            methodToAdd =
                    new MFAMethod(
                            MFAMethodType.SMS.name(),
                            true,
                            true,
                            "+447700900000",
                            "date",
                            PriorityIdentifier.DEFAULT,
                            "1");
        }
        return methodToAdd;
    }

    public UserCredentials createUserCredentials(
            UserProfile userProfile, String userPassword, MFAMethodType methodType) {
        UserCredentials userCredentials =
                buildNewMigratedUserCredentials(userProfile, userPassword, methodType);
        putUserCredentialsToDynamodb(userCredentials);
        return userCredentials;
    }
}

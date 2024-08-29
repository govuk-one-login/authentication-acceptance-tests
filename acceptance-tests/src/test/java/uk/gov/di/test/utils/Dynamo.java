package uk.gov.di.test.utils;

import org.jose4j.base64url.Base64Url;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Dynamo {

    private static final String PAIRWISE_PREFIX = "urn:fdc:gov.uk:2022:";
    private static final String PAST_TIMESTAMP = "1970-01-01T00:00:00.000000";
    private final String testPassword = System.getenv("TEST_USER_PASSWORD");
    private final String testPhoneNumber = System.getenv("TEST_USER_PHONE_NUMBER");
    private final String sectorHost = System.getenv("SECTOR_HOST");
    private final String environment = getEnvironmentName(sectorHost);
    private final String ACCOUNT_MODIFIERS_TABLE = environment + "-account-modifiers";
    private final String USER_PROFILE_TABLE = environment + "-user-profile";
    private final String USER_CREDENTIALS_TABLE = environment + "-user-credentials";
    private final String STUB_ACCOUNT_INTERVENTIONS_TABLE =
            environment + "-stub-account-interventions";
    private final Region region = Region.EU_WEST_2;

    DynamoDbClient client =
            DynamoDbClient.builder()
                    .region(region)
                    .credentialsProvider(
                            ProfileCredentialsProvider.create(
                                    getAWSProfileForEnvironment(environment)))
                    .build();

    public String getEnvironmentName(String secHost) {
        String[] sectorHostComponents = secHost.split("\\.");
        String env = sectorHostComponents[1];
        System.out.println(">>>>>>>>>> Environment: " + env);
        return env;
    }

    public String getAWSProfileForEnvironment(String env) {

        String profile = null;

        switch (env.toLowerCase()) {
            case "build":
                profile = "gds-di-development-admin";
                break;
            case "staging":
                profile = "di-auth-staging-admin";
                break;
            case "authdev1":
            case "authdev2":
            case "dev":
                profile = "di-auth-development-admin";
                break;
            default:
                throw new RuntimeException("Environment: " + env + " not recognised");
        }
        System.out.println(">>>>>>>>>> AWS Profile: " + profile);
        return profile;
    }

    public void createOrUpdateUser(
            String emailAddressDataTag,
            String usersPassword,
            Integer accountVerified,
            Integer emailVerified) {

        String emailAddress = System.getenv(emailAddressDataTag);

        // Check if user already exists or not
        GetItemRequest checkIfUserExistsRequest =
                GetItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(Map.of("Email", AttributeValue.fromS(emailAddress)))
                        .build();

        GetItemResponse checkIfUserExistsResponse = client.getItem(checkIfUserExistsRequest);
        String doesUserAlreadyHaveSubjectID = checkIfUserExistsResponse.item().get("SubjectID").s();
        System.out.println(">>>>>>>>>> SubjectID: " + doesUserAlreadyHaveSubjectID);

        String PublicSubjectID = UUID.randomUUID().toString();
        String SubjectID = UUID.randomUUID().toString();

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedUserProfileValues = new HashMap<>();

        // Don't update SubjectID if user already has one as this will affect any associated
        // Pairwise IDs (e.g. Account Interventions)
        if (doesUserAlreadyHaveSubjectID.isEmpty()) {

            updatedUserProfileValues.put(
                    "SubjectID",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(SubjectID).build())
                            .action(AttributeAction.PUT)
                            .build());
        }

        updatedUserProfileValues.put(
                "PublicSubjectID",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(PublicSubjectID).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedUserProfileValues.put(
                "accountVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(accountVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedUserProfileValues.put(
                "EmailVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(emailVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(itemKey)
                        .attributeUpdates(updatedUserProfileValues)
                        .build();

        try {
            client.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("User " + emailAddress + " updated in " + USER_PROFILE_TABLE);

        String encodedPassword = Argon2EncoderHelper.argon2Hash(usersPassword);

        HashMap<String, AttributeValue> itemKey2 = new HashMap<>();
        itemKey2.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedUserCredentialsValues = new HashMap<>();

        updatedUserCredentialsValues.put(
                "Password",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(encodedPassword).build())
                        .action(AttributeAction.PUT)
                        .build());

        if (doesUserAlreadyHaveSubjectID.isEmpty()) {

            updatedUserCredentialsValues.put(
                    "SubjectID",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(SubjectID).build())
                            .action(AttributeAction.PUT)
                            .build());
        }

        UpdateItemRequest request2 =
                UpdateItemRequest.builder()
                        .tableName(USER_CREDENTIALS_TABLE)
                        .key(itemKey2)
                        .attributeUpdates(updatedUserCredentialsValues)
                        .build();

        try {
            client.updateItem(request2);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("User " + emailAddress + " updated in " + USER_CREDENTIALS_TABLE);
    }

    public void updateTermsAndConditions(String emailAddressDataTag, String requiredTAndCsVersion) {

        String emailAddress = System.getenv(emailAddressDataTag);

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValue> termsAndConditionsFieldsAndValues = new HashMap<>();

        String termsAndConditionsVersion;
        if (requiredTAndCsVersion.equalsIgnoreCase("latest")) {
            termsAndConditionsVersion =
                    System.getenv("TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION");
        } else {
            termsAndConditionsVersion = "1.0";
        }

        termsAndConditionsFieldsAndValues.put(
                "timestamp", AttributeValue.builder().s(PAST_TIMESTAMP).build());

        termsAndConditionsFieldsAndValues.put(
                "version", AttributeValue.builder().s(termsAndConditionsVersion).build());

        HashMap<String, AttributeValueUpdate> termsAndConditions = new HashMap<>();

        termsAndConditions.put(
                "termsAndConditions",
                AttributeValueUpdate.builder()
                        .value(
                                AttributeValue.builder()
                                        .m(termsAndConditionsFieldsAndValues)
                                        .build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(itemKey)
                        .attributeUpdates(termsAndConditions)
                        .build();

        try {
            client.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(
                "Terms and Conditions updated to "
                        + termsAndConditionsVersion
                        + " for user "
                        + emailAddress);
    }

    public void updateMfaToSms(
            String emailAddressDataTag,
            Integer accountVerified,
            String phoneNumber,
            Integer phoneNumberVerified) {

        String emailAddress = System.getenv(emailAddressDataTag);

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();

        updatedValues.put(
                "accountVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(accountVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues.put(
                "PhoneNumber",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(phoneNumber).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues.put(
                "PhoneNumberVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(phoneNumberVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(itemKey)
                        .attributeUpdates(updatedValues)
                        .build();

        try {
            client.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("MFA set to SMS for user " + emailAddress + " in " + USER_PROFILE_TABLE);
    }

    public void deleteUser(String emailAddressDataTag) {

        String emailAddress = System.getenv(emailAddressDataTag);

        HashMap<String, AttributeValue> userProfileItemKey = new HashMap<>();
        userProfileItemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        DeleteItemRequest userProfileRequest =
                DeleteItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(userProfileItemKey)
                        .build();

        try {
            client.deleteItem(userProfileRequest);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(
                environment + "User " + emailAddress + " removed from " + USER_PROFILE_TABLE);

        HashMap<String, AttributeValue> userCredentialsItemKey = new HashMap<>();
        userCredentialsItemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        DeleteItemRequest userCredentialsRequest =
                DeleteItemRequest.builder()
                        .tableName(USER_CREDENTIALS_TABLE)
                        .key(userCredentialsItemKey)
                        .build();

        try {
            client.deleteItem(userCredentialsRequest);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(
                environment + "User " + emailAddress + " removed from " + USER_CREDENTIALS_TABLE);
    }

    public void updateMfaToAuthApp(String emailAddressDataTag) {

        String emailAddress = System.getenv(emailAddressDataTag);

        String credentialValue = System.getenv("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValue> mfaMethodsFieldsAndValues = new HashMap<>();
        mfaMethodsFieldsAndValues.put(
                "CredentialValue", AttributeValue.builder().s(credentialValue).build());
        mfaMethodsFieldsAndValues.put("Enabled", AttributeValue.builder().n("1").build());
        mfaMethodsFieldsAndValues.put("MethodVerified", AttributeValue.builder().n("1").build());
        mfaMethodsFieldsAndValues.put(
                "MfaMethodType", AttributeValue.builder().s("AUTH_APP").build());
        mfaMethodsFieldsAndValues.put(
                "Updated", AttributeValue.builder().s(PAST_TIMESTAMP).build());

        HashMap<String, AttributeValueUpdate> mfaMethods = new HashMap<>();
        mfaMethods.put(
                "MfaMethods",
                AttributeValueUpdate.builder()
                        .value(
                                AttributeValue.builder()
                                        .l(AttributeValue.fromM(mfaMethodsFieldsAndValues))
                                        .build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(USER_CREDENTIALS_TABLE)
                        .key(itemKey)
                        .attributeUpdates(mfaMethods)
                        .build();

        try {
            client.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(
                environment
                        + "MFA set to Auth App for user "
                        + emailAddress
                        + " in "
                        + USER_CREDENTIALS_TABLE);
    }

    public void removeMfaMethod(String emailAddressDataTag) {

        String emailAddress = System.getenv(emailAddressDataTag);

        HashMap<String, AttributeValue> itemKey = new HashMap<>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(USER_CREDENTIALS_TABLE)
                        .key(itemKey)
                        .updateExpression("REMOVE MfaMethods")
                        .build();

        try {
            client.updateItem(request);
        } catch (ResourceNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        System.out.println(
                "MFA methods for user " + emailAddress + " removed from " + USER_CREDENTIALS_TABLE);
    }

    public void updateAccountRecoveryBlock(String emailAddressDataTag) {

        String usersEmail = System.getenv(emailAddressDataTag);

        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(Map.of("Email", AttributeValue.fromS(usersEmail)))
                        .build();

        GetItemResponse response = client.getItem(request);

        if (response != null) {

            String subjectID = response.item().get("SubjectID").s();
            SdkBytes salt = response.item().get("salt").b();
            byte[] bytes = salt.asByteArray();
            String pwid = calculatePairwiseIdentifier(subjectID, sectorHost, bytes);

            HashMap<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put(
                    "InternalCommonSubjectIdentifier", AttributeValue.builder().s(pwid).build());

            HashMap<String, AttributeValue> accountRecoveryFieldsAndValues = new HashMap<>();
            accountRecoveryFieldsAndValues.put(
                    "Blocked", AttributeValue.builder().bool(false).build());
            accountRecoveryFieldsAndValues.put(
                    "Created", AttributeValue.builder().s(PAST_TIMESTAMP).build());
            accountRecoveryFieldsAndValues.put(
                    "Updated", AttributeValue.builder().s(PAST_TIMESTAMP).build());

            HashMap<String, AttributeValueUpdate> accountRecovery = new HashMap<>();
            accountRecovery.put(
                    "AccountRecovery",
                    AttributeValueUpdate.builder()
                            .value(
                                    AttributeValue.builder()
                                            .m(accountRecoveryFieldsAndValues)
                                            .build())
                            .action(AttributeAction.PUT)
                            .build());

            UpdateItemRequest req =
                    UpdateItemRequest.builder()
                            .tableName(ACCOUNT_MODIFIERS_TABLE)
                            .key(itemKey)
                            .attributeUpdates(accountRecovery)
                            .build();

            try {
                client.updateItem(req);
            } catch (ResourceNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            System.out.println(
                    "Account Recovery block updated for user "
                            + usersEmail
                            + " in "
                            + ACCOUNT_MODIFIERS_TABLE);
        }
    }

    public void deleteAccountIntervention(String emailAddressDataTag) {

        String usersEmail = System.getenv(emailAddressDataTag);

        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(Map.of("Email", AttributeValue.fromS(usersEmail)))
                        .build();

        GetItemResponse response = client.getItem(request);

        if (response.hasItem()) {

            String subjectID = response.item().get("SubjectID").s();
            SdkBytes salt = response.item().get("salt").b();
            byte[] bytes = salt.asByteArray();
            String pwid = calculatePairwiseIdentifier(subjectID, sectorHost, bytes);

            HashMap<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("InternalPairwiseId", AttributeValue.builder().s(pwid).build());

            DeleteItemRequest accountIntervention =
                    DeleteItemRequest.builder()
                            .tableName(STUB_ACCOUNT_INTERVENTIONS_TABLE)
                            .key(itemKey)
                            .build();

            try {
                client.deleteItem(accountIntervention);
            } catch (ResourceNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            System.out.println(
                    "Account Intervention for user "
                            + usersEmail
                            + " with Pairwise ID: "
                            + pwid
                            + " deleted from "
                            + STUB_ACCOUNT_INTERVENTIONS_TABLE);
        } else {
            System.out.println(
                    "Skipping Account Intervention deletion - user "
                            + usersEmail
                            + " does not exist");
        }
    }

    public void createUpdateAccountInterventionForUser(
            String emailAddressDataTag,
            Boolean isBlocked,
            Boolean isReproveIdentity,
            Boolean isResetPassword,
            Boolean isSuspended) {

        String usersEmail = System.getenv(emailAddressDataTag);

        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(USER_PROFILE_TABLE)
                        .key(Map.of("Email", AttributeValue.fromS(usersEmail)))
                        .build();

        GetItemResponse response = client.getItem(request);

        if (response.hasItem()) {

            String subjectID = response.item().get("SubjectID").s();
            SdkBytes salt = response.item().get("salt").b();
            byte[] bytes = salt.asByteArray();
            String pwid = calculatePairwiseIdentifier(subjectID, sectorHost, bytes);

            HashMap<String, AttributeValue> itemKey = new HashMap<>();
            itemKey.put("InternalPairwiseId", AttributeValue.builder().s(pwid).build());

            HashMap<String, AttributeValueUpdate> accountInterventionFields = new HashMap<>();
            accountInterventionFields.put(
                    "Blocked",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().bool(isBlocked).build())
                            .action(AttributeAction.PUT)
                            .build());

            accountInterventionFields.put(
                    "EquivalentPlainEmailAddress",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(usersEmail).build())
                            .action(AttributeAction.PUT)
                            .build());

            accountInterventionFields.put(
                    "ReproveIdentity",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().bool(isReproveIdentity).build())
                            .action(AttributeAction.PUT)
                            .build());

            accountInterventionFields.put(
                    "ResetPassword",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().bool(isResetPassword).build())
                            .action(AttributeAction.PUT)
                            .build());

            accountInterventionFields.put(
                    "Suspended",
                    AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().bool(isSuspended).build())
                            .action(AttributeAction.PUT)
                            .build());

            UpdateItemRequest updateAccountIntervention =
                    UpdateItemRequest.builder()
                            .tableName(STUB_ACCOUNT_INTERVENTIONS_TABLE)
                            .key(itemKey)
                            .attributeUpdates(accountInterventionFields)
                            .build();

            try {
                client.updateItem(updateAccountIntervention);
            } catch (ResourceNotFoundException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            } catch (DynamoDbException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            System.out.println(
                    "Account Intervention for user "
                            + usersEmail
                            + " with Pairwise ID: "
                            + pwid
                            + " updated in "
                            + STUB_ACCOUNT_INTERVENTIONS_TABLE);
        } else {
            System.out.println(
                    "Skipping Account Intervention update - user "
                            + usersEmail
                            + " does not exist");
        }
    }

    public static String calculatePairwiseIdentifier(
            String subjectID, String sectorHost, byte[] salt) {

        try {
            var md = MessageDigest.getInstance("SHA-256");

            md.update(sectorHost.getBytes(StandardCharsets.UTF_8));
            md.update(subjectID.getBytes(StandardCharsets.UTF_8));

            byte[] bytes = md.digest(salt);

            var sb = Base64Url.encode(bytes);

            return PAIRWISE_PREFIX + sb;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUpdateStandardSmsUser(String emailAddressDataTag) {
        createOrUpdateUser(emailAddressDataTag, testPassword, 1, 1);
        updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
        updateTermsAndConditions(emailAddressDataTag, "latest");
    }

    public void createUpdateStandardAuthAppUser(String emailAddressDataTag) {
        createOrUpdateUser(emailAddressDataTag, testPassword, 1, 1);
        updateMfaToAuthApp(emailAddressDataTag);
        updateTermsAndConditions(emailAddressDataTag, "latest");
    }

    public void createUpdateSmsUserTermsAndConditionsOutOfDate(String emailAddressDataTag) {
        createOrUpdateUser(emailAddressDataTag, testPassword, 1, 1);
        updateMfaToSms(emailAddressDataTag, 1, testPhoneNumber, 1);
        updateTermsAndConditions(emailAddressDataTag, "old");
    }

    public void createUpdateAuthAppUserTermsAndConditionsOutOfDate(String emailAddressDataTag) {
        createOrUpdateUser(emailAddressDataTag, testPassword, 1, 1);
        updateMfaToAuthApp(emailAddressDataTag);
        updateTermsAndConditions(emailAddressDataTag, "old");
    }
}

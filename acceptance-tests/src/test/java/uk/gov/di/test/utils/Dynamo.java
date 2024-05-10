package uk.gov.di.test.utils;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Dynamo {
    Region region = Region.EU_WEST_2;
    String environment = "build";

    DynamoDbClient client =
            DynamoDbClient.builder()
                    .region(region)
                    .credentialsProvider(
                            ProfileCredentialsProvider.create(
                                    "gds-di-development-admin"))
                    .build();

    public void getUser(String emailDataLabel) {

        System.out.println("emailDataLabel = " + emailDataLabel);
        String usersEmail = System.getenv(emailDataLabel);
        System.out.println("Email address = " + usersEmail);
        System.out.println("Client: " + client);

        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(environment + "-user-credentials")
                        .key(Map.of("Email", AttributeValue.fromS(usersEmail)))
                        .build();

        GetItemResponse response = client.getItem(request);
        System.out.println("Item is: " + response);
    }

    public void createOrUpdateUser(
            String emailAddress,
            String usersPassword,
            Integer accountVerified,
            Integer emailVerified) {

        String PublicSubjectID = UUID.randomUUID().toString();
        String SubjectID = UUID.randomUUID().toString();

        HashMap<String, AttributeValue> itemKey = new HashMap<String, AttributeValue>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedValues =
                new HashMap<String, AttributeValueUpdate>();

        updatedValues.put(
                "PublicSubjectID",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(PublicSubjectID).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues.put(
                "SubjectID",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(SubjectID).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues.put(
                "accountVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(accountVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues.put(
                "EmailVerified",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().n(emailVerified.toString()).build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(environment + "-user-profile")
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
        System.out.println(environment + "-user-profile done!");

        String encodedPassword = Argon2EncoderHelper.argon2Hash(usersPassword);

        HashMap<String, AttributeValue> itemKey2 = new HashMap<String, AttributeValue>();
        itemKey2.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedValues2 =
                new HashMap<String, AttributeValueUpdate>();

        updatedValues2.put(
                "Password",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(encodedPassword).build())
                        .action(AttributeAction.PUT)
                        .build());

        updatedValues2.put(
                "SubjectID",
                AttributeValueUpdate.builder()
                        .value(AttributeValue.builder().s(SubjectID).build())
                        .action(AttributeAction.PUT)
                        .build());

        UpdateItemRequest request2 =
                UpdateItemRequest.builder()
                        .tableName(environment + "-user-credentials")
                        .key(itemKey2)
                        .attributeUpdates(updatedValues2)
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
        System.out.println(environment + "-user-credentials. User update done!");
    }

    public void updateTermsAndConditions(String emailAddress, String requiredTAndCsVersion) {

        HashMap<String, AttributeValue> itemKey = new HashMap<String, AttributeValue>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValue> termsAndConditionsFieldsAndValues =
                new HashMap<String, AttributeValue>();

        String termsAndConditionsVersion;
        if (requiredTAndCsVersion.equalsIgnoreCase("latest")) {
            termsAndConditionsVersion =
                    System.getenv("TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION");
        } else {
            termsAndConditionsVersion = "1.0";
        }

        termsAndConditionsFieldsAndValues.put(
                "timestamp", AttributeValue.builder().s("1970-01-01T00:00:00.000000").build());
        termsAndConditionsFieldsAndValues.put(
                "version", AttributeValue.builder().s(termsAndConditionsVersion).build());

        HashMap<String, AttributeValueUpdate> termsAndConditions =
                new HashMap<String, AttributeValueUpdate>();

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
                        .tableName(environment + "-user-profile")
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
        System.out.println(environment + "-user-profile. T&C's update done!");
    }

    public void updateMfaSms(
            String emailAddress,
            Integer accountVerified,
            String phoneNumber,
            Integer phoneNumberVerified) {

        HashMap<String, AttributeValue> itemKey = new HashMap<String, AttributeValue>();
        itemKey.put("Email", AttributeValue.builder().s(emailAddress).build());

        HashMap<String, AttributeValueUpdate> updatedValues =
                new HashMap<String, AttributeValueUpdate>();

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
                        .tableName(environment + "-user-profile")
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
        System.out.println(environment + "-user-profile. MFA update done!");
    }

    public void createUpdateStandardSmsUser(String email) {

        String emailAddress = System.getenv(email);
        String testPassword = System.getenv("TEST_USER_PASSWORD");
        String testPhoneNumber = System.getenv("TEST_USER_PHONE_NUMBER");

        createOrUpdateUser(emailAddress, testPassword, 1, 1);
        updateMfaSms(emailAddress, 1, testPhoneNumber, 1);
        updateTermsAndConditions(emailAddress, "latest");
    }
}

package uk.gov.di.test.controllers;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserInterventions;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Environment;

public class DynamoDbController {
    private static volatile DynamoDbController instance;

    private DynamoDbController() {
        // Private constructor to prevent direct instantiation
    }

    public static DynamoDbController getInstance() {
        if (instance == null) {
            synchronized (DynamoDbController.class) {
                if (instance == null) {
                    instance = new DynamoDbController();
                }
            }
        }
        return instance;
    }

    protected static final String ENVIRONMENT = Environment.getOrThrow("ENVIRONMENT");
    protected static final String AWS_REGION = Environment.getOrThrow("AWS_REGION");

    private static final DynamoDbClient standardClient =
            DynamoDbClient.builder().region(Region.of(AWS_REGION)).build();

    private static final DynamoDbEnhancedClient enhancedClient =
            DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();

    private static final DynamoDbTable<UserProfile> userProfileTable =
            enhancedClient.table(
                    ENVIRONMENT + "-user-profile", TableSchema.fromBean(UserProfile.class));

    private static final DynamoDbTable<UserCredentials> userCredentialsTable =
            enhancedClient.table(
                    ENVIRONMENT + "-user-credentials", TableSchema.fromBean(UserCredentials.class));

    private static final DynamoDbTable<UserInterventions> userInterventionsTable =
            enhancedClient.table(
                    ENVIRONMENT + "-stub-account-interventions",
                    TableSchema.fromBean(UserInterventions.class));

    public void putUserProfile(UserProfile userProfile) {
        userProfileTable.putItem(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileTable.updateItem(userProfile);
    }

    public void deleteUserProfile(UserProfile userProfile) {
        userProfileTable.deleteItem(userProfile);
    }

    public void putUserCredentials(UserCredentials userCredentials) {
        userCredentialsTable.putItem(userCredentials);
    }

    public void updateUserCredentials(UserCredentials userCredentials) {
        userCredentialsTable.updateItem(userCredentials);
    }

    public void deleteUserCredentials(UserCredentials userCredentials) {
        userCredentialsTable.deleteItem(userCredentials);
    }

    public void putUserInterventions(UserInterventions userInterventions) {
        userInterventionsTable.putItem(userInterventions);
    }

    public void updateUserInterventions(UserInterventions userInterventions) {
        userInterventionsTable.updateItem(userInterventions);
    }

    public void deleteUserInterventions(UserInterventions userInterventions) {
        userInterventionsTable.deleteItem(userInterventions);
    }
}

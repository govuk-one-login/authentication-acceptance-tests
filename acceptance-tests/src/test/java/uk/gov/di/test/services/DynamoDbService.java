package uk.gov.di.test.services;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserInterventions;
import uk.gov.di.test.entity.UserProfile;

public class DynamoDbService {
    private static volatile DynamoDbService instance;

    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

    private DynamoDbService() {
        // Private constructor to prevent direct instantiation
    }

    public static DynamoDbService getInstance() {
        if (instance == null) {
            synchronized (DynamoDbService.class) {
                if (instance == null) {
                    instance = new DynamoDbService();
                }
            }
        }
        return instance;
    }

    private static final DynamoDbClient standardClient = DynamoDbClient.builder().build();

    private static final DynamoDbEnhancedClient enhancedClient =
            DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();

    private static final DynamoDbTable<UserProfile> userProfileTable =
            enhancedClient.table(
                    TEST_CONFIG_SERVICE.get("USER_PROFILE_TABLE"),
                    TableSchema.fromBean(UserProfile.class));

    private static final DynamoDbTable<UserCredentials> userCredentialsTable =
            enhancedClient.table(
                    TEST_CONFIG_SERVICE.get("USER_CREDENTIALS_TABLE"),
                    TableSchema.fromBean(UserCredentials.class));

    private static final DynamoDbTable<UserInterventions> userInterventionsTable =
            enhancedClient.table(
                    TEST_CONFIG_SERVICE.get("ACCOUNT_INTERVENTIONS_TABLE"),
                    TableSchema.fromBean(UserInterventions.class));

    public UserProfile getUserProfile(String key) {
        Key dbkey = Key.builder().partitionValue(key).build();
        return userProfileTable.getItem(dbkey);
    }

    public void putUserProfile(UserProfile userProfile) {
        userProfileTable.putItem(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileTable.updateItem(userProfile);
    }

    public void deleteUserProfile(UserProfile userProfile) {
        userProfileTable.deleteItem(userProfile);
    }

    public UserCredentials getUserCredentials(String key) {
        Key dbkey = Key.builder().partitionValue(key).build();
        return userCredentialsTable.getItem(dbkey);
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

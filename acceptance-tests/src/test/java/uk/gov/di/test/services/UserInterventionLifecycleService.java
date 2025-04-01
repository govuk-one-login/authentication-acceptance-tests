package uk.gov.di.test.services;

import uk.gov.di.test.entity.UserInterventions;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Crypto;

public class UserInterventionLifecycleService {
    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();
    private static volatile UserInterventionLifecycleService instance;

    private UserInterventionLifecycleService() {
        // Private constructor to prevent direct instantiation
    }

    public static UserInterventionLifecycleService getInstance() {
        if (instance == null) {
            synchronized (UserInterventionLifecycleService.class) {
                if (instance == null) {
                    instance = new UserInterventionLifecycleService();
                }
            }
        }
        return instance;
    }

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();

    public static UserInterventions buildBaseInterventions(UserProfile userProfile) {
        String pairwiseID =
                Crypto.calculatePairwiseIdentifier(
                        userProfile.getSubjectID(),
                        TEST_CONFIG_SERVICE.get("SECTOR_HOST"),
                        userProfile.getSalt());

        return new UserInterventions()
                .withInternalPairwiseID(pairwiseID)
                .withEquivalentPlainEmailAddress(userProfile.getEmail())
                .withSuspended(false)
                .withResetPassword(false)
                .withBlocked(false)
                .withReproveIdentity(false);
    }

    public void removeAllInterventionsBlocksAndUpdateInDynamodb(
            UserInterventions userInterventions) {
        userInterventions.disableAllInterventions();
        updateInterventionInDynamoDb(userInterventions);
    }

    public UserInterventions buildAndPutSuspendedInterventions(UserProfile userProfile) {
        UserInterventions userInterventions =
                buildBaseInterventions(userProfile).withSuspended(true);
        putInterventionInDynamoDb(userInterventions);
        return userInterventions;
    }

    public UserInterventions buildAndPutBlockedInterventions(UserProfile userProfile) {
        UserInterventions userInterventions = buildBaseInterventions(userProfile).withBlocked(true);
        putInterventionInDynamoDb(userInterventions);
        return userInterventions;
    }

    public UserInterventions buildAndPutResetPasswordInterventions(UserProfile userProfile) {
        UserInterventions userInterventions =
                buildBaseInterventions(userProfile).withSuspended(true).withResetPassword(true);
        putInterventionInDynamoDb(userInterventions);
        return userInterventions;
    }

    public UserInterventions buildAndPutReproveIdentityInterventions(UserProfile userProfile) {
        UserInterventions userInterventions =
                buildBaseInterventions(userProfile).withReproveIdentity(true);
        putInterventionInDynamoDb(userInterventions);
        return userInterventions;
    }

    public void putInterventionInDynamoDb(UserInterventions userInterventions) {
        DYNAMO_DB_SERVICE.putUserInterventions(userInterventions);
    }

    public void updateInterventionInDynamoDb(UserInterventions userInterventions) {
        DYNAMO_DB_SERVICE.updateUserInterventions(userInterventions);
    }

    public void deleteInterventionInDynamoDb(UserInterventions userInterventions) {
        DYNAMO_DB_SERVICE.deleteUserInterventions(userInterventions);
    }
}

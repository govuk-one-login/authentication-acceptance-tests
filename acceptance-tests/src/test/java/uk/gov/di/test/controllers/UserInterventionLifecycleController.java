package uk.gov.di.test.controllers;

import uk.gov.di.test.entity.UserInterventions;
import uk.gov.di.test.entity.UserProfile;
import uk.gov.di.test.utils.Crypto;
import uk.gov.di.test.utils.Environment;

public class UserInterventionLifecycleController {
    private static volatile UserInterventionLifecycleController instance;

    private UserInterventionLifecycleController() {
        // Private constructor to prevent direct instantiation
    }

    public static UserInterventionLifecycleController getInstance() {
        if (instance == null) {
            synchronized (UserInterventionLifecycleController.class) {
                if (instance == null) {
                    instance = new UserInterventionLifecycleController();
                }
            }
        }
        return instance;
    }

    private static final DynamoDbController dynamoDbController = DynamoDbController.getInstance();

    public static UserInterventions buildBaseInterventions(UserProfile userProfile) {
        String b64Digest =
                Crypto.generatePairwiseIdDigest(
                        Environment.getOrThrow("SECTOR_HOST"),
                        userProfile.getSubjectID(),
                        userProfile.getSalt());
        String pairwiseID = String.format("urn:fdc:gov.uk:2022:%s", b64Digest);

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
        dynamoDbController.putUserInterventions(userInterventions);
    }

    public void updateInterventionInDynamoDb(UserInterventions userInterventions) {
        dynamoDbController.updateUserInterventions(userInterventions);
    }

    public void deleteInterventionInDynamoDb(UserInterventions userInterventions) {
        dynamoDbController.deleteUserInterventions(userInterventions);
    }
}

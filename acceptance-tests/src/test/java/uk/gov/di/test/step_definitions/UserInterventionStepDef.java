package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import uk.gov.di.test.entity.UserInterventions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserInterventionStepDef {
    private final World world;

    protected static final String ENVIRONMENT = System.getenv().get("ENVIRONMENT");
    protected static final String AWS_REGION = System.getenv().get("AWS_REGION");

    private static final DynamoDbClient standardClient =
            DynamoDbClient.builder().region(Region.of(AWS_REGION)).build();

    private static final DynamoDbEnhancedClient DYNAMODB_CLIENT =
            DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();

    private static final DynamoDbTable<UserInterventions> userInterventionsTable =
            DYNAMODB_CLIENT.table(
                    ENVIRONMENT + "-stub-account-interventions",
                    TableSchema.fromBean(UserInterventions.class));

    public UserInterventionStepDef(World world) {
        this.world = world;
    }

    @ParameterType("temporarily suspended|permanently blocked|password reset|reprove identity")
    public String intervention(String intervention) {
        return intervention;
    }

    private UserInterventions buildBaseIntervention() throws NoSuchAlgorithmException {
        String rawDigest =
                String.format(
                        "identity.%s.account.gov.uk%s%s",
                        ENVIRONMENT,
                        world.userProfile.getSubjectID(),
                        Base64.getEncoder().encodeToString(world.userProfile.getSalt()));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digestBytes = messageDigest.digest(rawDigest.getBytes());
        String b64Digest = Base64.getEncoder().encodeToString(digestBytes);
        b64Digest = b64Digest.replace("/", "_").replace("+", "-").replace("=", "");
        String pairwiseID = String.format("urn:fdc:gov.uk:2022:%s", b64Digest);

        return new UserInterventions()
                .withInternalPairwiseID(pairwiseID)
                .withEquivalentPlainEmailAddress(world.userProfile.getEmail())
                .withSuspended(false)
                .withResetPassword(false)
                .withBlocked(false)
                .withReproveIdentity(false);
    }

    @Given("and the user has a {intervention} intervention")
    public void andHasAnIntervention(String intervention) throws NoSuchAlgorithmException {
        if (world.userProfile == null || world.userCredentials == null) {
            throw new RuntimeException("User profile or credentials do not exist");
        }

        switch (intervention) {
            case "temporarily suspended":
                world.userInterventions = buildBaseIntervention().withSuspended(true);
                break;
            case "permanently blocked":
                world.userInterventions = buildBaseIntervention().withBlocked(true);
                break;
            case "password reset":
                world.userInterventions =
                        buildBaseIntervention().withSuspended(true).withResetPassword(true);
                break;
            case "reprove identity":
                world.userInterventions = buildBaseIntervention().withReproveIdentity(true);
                break;
            default:
                throw new RuntimeException("Invalid intervention");
        }
        userInterventionsTable.putItem(world.userInterventions);
    }

    public void removeInterventionBlocks() {
        world.userInterventions.disableAllInterventions();
    }

    @After
    public void deleteIntervention() {
        if (world.userInterventions != null) {
            userInterventionsTable.deleteItem(world.userInterventions);
        }
    }
}

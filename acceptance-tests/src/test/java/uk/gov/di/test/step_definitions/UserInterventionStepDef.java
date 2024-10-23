package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import uk.gov.di.test.services.UserInterventionLifecycleService;

public class UserInterventionStepDef {
    private final World world;

    private static final UserInterventionLifecycleService lifecycleController =
            UserInterventionLifecycleService.getInstance();

    public UserInterventionStepDef(World world) {
        this.world = world;
    }

    @ParameterType("temporarily suspended|permanently locked|password reset|reprove identity")
    public String intervention(String intervention) {
        return intervention;
    }

    @Given("the user has a {intervention} intervention")
    public void andHasAnIntervention(String intervention) {
        if (world.userProfile == null || world.userCredentials == null) {
            throw new RuntimeException("User profile or credentials do not exist");
        }

        switch (intervention) {
            case "temporarily suspended":
                world.userInterventions =
                        lifecycleController.buildAndPutSuspendedInterventions(world.userProfile);
                break;
            case "permanently locked":
                world.userInterventions =
                        lifecycleController.buildAndPutBlockedInterventions(world.userProfile);
                break;
            case "password reset":
                world.userInterventions =
                        lifecycleController.buildAndPutResetPasswordInterventions(
                                world.userProfile);
                break;
            case "reprove identity":
                world.userInterventions =
                        lifecycleController.buildAndPutReproveIdentityInterventions(
                                world.userProfile);
                break;
            default:
                throw new RuntimeException("Invalid intervention");
        }
    }

    @Given("the user's interventions have been removed")
    public void removeInterventionBlocks() {
        lifecycleController.removeAllInterventionsBlocksAndUpdateInDynamodb(
                world.userInterventions);
    }

    @After
    public void deleteIntervention() {
        if (world.userInterventions != null) {
            System.out.printf(
                    "Deleting user interventions with email %s%n",
                    world.userInterventions.getEquivalentPlainEmailAddress());
            lifecycleController.deleteInterventionInDynamoDb(world.userInterventions);
        }
    }
}

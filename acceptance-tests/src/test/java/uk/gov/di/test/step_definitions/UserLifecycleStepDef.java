package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import uk.gov.di.test.controllers.UserLifecycleController;

public class UserLifecycleStepDef {
    private final World world;

    private static final UserLifecycleController userLifecycleController =
            UserLifecycleController.getInstance();

    public UserLifecycleStepDef(World world) {
        this.world = world;
    }

    @Given("a user does not yet exist")
    public void aUserDoesNotYetExist() {
        world.throwIfUserProfileExists().throwIfUserCredentialsExists();
        world.userProfile = userLifecycleController.buildNewUserProfile();
    }

    @Given("a user with no MFA exists")
    @Given("a user exists")
    public void aUserExists() {
        aUserDoesNotYetExist();
        userLifecycleController.putUserProfileToDynamodb(world.userProfile);
        world.userCredentials =
                userLifecycleController.buildNewUserCredentialsAndPutToDynamodb(world.userProfile);
    }

    @ParameterType("sms|app")
    public String mfaMethod(String mfaMethodType) {
        return mfaMethodType;
    }

    @Given("a user with {mfaMethod} MFA exists")
    public void aUserWithOtpExists(String mfaMethod) {
        aUserExists();
        switch (mfaMethod) {
            case "sms":
                userLifecycleController.updateUserMfaSms(world.userProfile);
                break;
            case "app":
                userLifecycleController.updateUserMfaApp(world.userCredentials);
                break;
            default:
                throw new RuntimeException("Invalid MFA Method");
        }
    }

    @And("the user has not yet accepted the latest terms and conditions")
    public void theUserHasNotYetAcceptedTheLatestTermsAndConditions() {
        userLifecycleController.updateUserTermsAndConditions(
                world.userProfile, userLifecycleController.buildOldTermsAndConditions());
    }

    @After
    public void theUserIsDeleted() {
        if (world.userProfile != null) {
            System.out.printf(
                    "Deleting user profile with email %s%n", world.userProfile.getEmail());
            userLifecycleController.deleteUserProfileFromDynamodb(world.userProfile);
        }
        if (world.userCredentials != null) {
            System.out.printf(
                    "Deleting user credentials with email %s%n", world.userCredentials.getEmail());
            userLifecycleController.deleteUserCredentialsFromDynamodb(world.userCredentials);
        }
    }
}

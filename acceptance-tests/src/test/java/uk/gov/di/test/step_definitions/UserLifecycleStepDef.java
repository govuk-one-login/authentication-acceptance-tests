package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import uk.gov.di.test.entity.MFAMethodType;
import uk.gov.di.test.services.UserLifecycleService;

import static uk.gov.di.test.services.UserLifecycleService.generateValidPassword;
import static uk.gov.di.test.utils.Constants.TOP_100K_PASSWORD;

public class UserLifecycleStepDef {
    private final World world;

    public static final UserLifecycleService userLifecycleService =
            UserLifecycleService.getInstance();

    public UserLifecycleStepDef(World world) {
        this.world = world;
    }

    @Given("a user does not yet exist")
    public void aUserDoesNotYetExist() {
        world.throwIfUserProfileExists().throwIfUserCredentialsExists();
        world.userProfile = userLifecycleService.buildNewUserProfile();
        world.setUserPassword(generateValidPassword());
    }

    @Given("a Migrated User does not yet exist")
    public void aMigratedUserDoesNotYetExist() {
        world.throwIfUserProfileExists().throwIfUserCredentialsExists();
        world.userProfile = userLifecycleService.buildNewMigratedUserProfile();
        world.setUserPassword(generateValidPassword());
    }

    @Given("a Migrated User with an Auth App Default MFA")
    public void aMigratedUserExists() {
        aMigratedUserDoesNotYetExist();
        userLifecycleService.putUserProfileToDynamodb(world.userProfile);
        world.userCredentials =
                userLifecycleService.createUserCredentials(
                        world.userProfile, world.getUserPassword(), MFAMethodType.AUTH_APP);
    }

    @Given("a Migrated User with a Default MFA of SMS")
    public void aMigratedUserWithSMSExists() {
        aMigratedUserDoesNotYetExist();
        userLifecycleService.putUserProfileToDynamodb(world.userProfile);
        world.userCredentials =
                userLifecycleService.createUserCredentials(
                        world.userProfile, world.getUserPassword(), MFAMethodType.SMS);
    }

    @Given("a Migrated User with a Default MFA of {string}")
    public void aMigratedUserWithMFAMethodExists(String mfaMethodType) {
        aMigratedUserDoesNotYetExist();
        userLifecycleService.putUserProfileToDynamodb(world.userProfile);
        world.userCredentials =
                userLifecycleService.createUserCredentials(
                        world.userProfile,
                        world.getUserPassword(),
                        MFAMethodType.fromString(mfaMethodType));
    }

    @Given("a User exists")
    @Given("a user exists")
    public void aUserExists() {
        aUserDoesNotYetExist();
        userLifecycleService.putUserProfileToDynamodb(world.userProfile);
        world.userCredentials =
                userLifecycleService.buildNewUserCredentialsAndPutToDynamodb(
                        world.userProfile, world.getUserPassword());
    }

    @ParameterType("SMS|App|no")
    public String mfaMethod(String mfaMethodType) {
        return mfaMethodType;
    }

    @Given("a user with {string} MFA exists")
    @Given("a user with {mfaMethod} MFA exists")
    public void aUserExists(String mfaMethod) {
        aUserExists();
        switch (mfaMethod) {
            case "no":
                break;
            case "SMS":
                userLifecycleService.updateUserMfaSms(world.userProfile);
                break;
            case "App":
                world.userProfile.setPhoneNumber(null);
                world.userProfile.setPhoneNumberVerified(false);
                userLifecycleService.putUserProfileToDynamodb(world.userProfile);
                userLifecycleService.updateUserMfaApp(world.userCredentials);
                break;
            default:
                throw new RuntimeException("Invalid MFA Method");
        }
    }

    @And("Another User with {mfaMethod} exists")
    public void anotherUserExists(String mfaMethod) {
        world.setOtherUserProfile(userLifecycleService.buildNewUserProfile());
        world.setOtherUserPassword(generateValidPassword());
        switch (mfaMethod) {
            case "no":
                break;
            case "SMS":
                userLifecycleService.updateUserMfaSms(world.getOtherUserProfile());
                break;
            case "App":
                userLifecycleService.updateUserMfaApp(world.getOtherUserCredentials());
                break;
            default:
                throw new RuntimeException("Invalid MFA Method");
        }
        userLifecycleService.putUserProfileToDynamodb(world.getOtherUserProfile());
        world.setOtherUserCredentials(
                userLifecycleService.buildNewUserCredentialsAndPutToDynamodb(
                        world.getOtherUserProfile(), world.getOtherUserPassword()));
    }

    @And("the user has not yet accepted the latest terms and conditions")
    public void theUserHasNotYetAcceptedTheLatestTermsAndConditions() {
        userLifecycleService.updateUserTermsAndConditions(
                world.userProfile, userLifecycleService.buildOldTermsAndConditions());
    }

    @And("the user's password is on the top 100k unacceptable password list")
    public void theUsersPasswordIsOnTheTop100kUnacceptablePasswordList() {
        userLifecycleService.changeUserPassword(
                TOP_100K_PASSWORD, world.userProfile, world.userCredentials);
        world.setUserPassword(TOP_100K_PASSWORD);
    }

    //    @After("@UI or @API")
    @After("@UI")
    public void theUserIsDeleted() {
        if (world.userProfile != null) {
            System.out.printf(
                    "Deleting user profile with email %s%n", world.userProfile.getEmail());
            userLifecycleService.deleteUserProfileFromDynamodb(world.userProfile);
        }
        if (world.userCredentials != null) {
            System.out.printf(
                    "Deleting user credentials with email %s%n", world.userCredentials.getEmail());
            userLifecycleService.deleteUserCredentialsFromDynamodb(world.userCredentials);
        }
    }
}

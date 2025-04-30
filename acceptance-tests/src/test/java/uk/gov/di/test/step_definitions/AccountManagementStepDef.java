package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.services.DynamoDbService;
import uk.gov.di.test.services.UserLifecycleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.services.ApiInteractionsService.authenticateUser;
import static uk.gov.di.test.services.ApiInteractionsService.authorizeUser;
import static uk.gov.di.test.services.ApiInteractionsService.getOtp;
import static uk.gov.di.test.services.ApiInteractionsService.sendOtpNotification;
import static uk.gov.di.test.services.ApiInteractionsService.updatePhoneNumber;

public class AccountManagementStepDef extends BasePage {
    private final World world;
    private static final UserLifecycleService userLifecycleService =
            UserLifecycleService.getInstance();

    public AccountManagementStepDef(World world) {
        this.world = world;
    }

    @Given("the User is Authenticated")
    public void theUserIsAuthenticated() {
        authorizeUser(world);
        authenticateUser(world);
    }

    @When("the User adds {string} as their SMS Backup MFA")
    public void userUpdatesDefaultMfaToSmsAndSystemSendsAnOTP(String newPhoneNumber) {
        world.setNewPhoneNumber(newPhoneNumber);
        sendOtpNotification(world);
    }

    @When("^the system sends an OTP to \".+\"$")
    public void theUserReceivesTheOTPCode() {
        var code = getOtp(world.userProfile.getEmail());
        world.setOtp(code);
    }

    @When("the User submits the OTP code to confirm the Phone Number change")
    public void theUserSubmitsTheOTPCodeToConfirmThePhoneNumberChange() {
        updatePhoneNumber(world);
    }

    @Then("the User's Phone Number is updated to {string}")
    public void theUserSPhoneNumberIsUpdatedTo(String newPhoneNumber) {
        var userProfile =
                DynamoDbService.getInstance().getUserProfile(world.userProfile.getEmail());
        assertEquals("07700900111", newPhoneNumber);
    }

    @After("@Test")
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

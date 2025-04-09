package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.services.DynamoDbService;
import uk.gov.di.test.services.UserLifecycleService;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.services.ApiInteractionsService.authenticate;
import static uk.gov.di.test.services.ApiInteractionsService.authorizeApiGatewayUse;
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
    public void theUserIsAuthenticated() throws ParseException, JOSEException {
        authorizeApiGatewayUse(world);
        authenticate(world);
    }

    @When("the User requests an OTP to change their Phone Number to {string}")
    public void theUserRequestsAnOTP(String newPhoneNumber) {
        world.setNewPhoneNumber(newPhoneNumber);
        sendOtpNotification(world);
    }

    @When("the User receives the OTP code")
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

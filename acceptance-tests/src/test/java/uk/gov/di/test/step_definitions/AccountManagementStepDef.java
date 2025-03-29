package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.di.test.pages.BasePage;

import java.text.ParseException;

import static uk.gov.di.test.services.ApiInteractionsService.authenticate;
import static uk.gov.di.test.services.ApiInteractionsService.authorizeApiGatewayUse;
import static uk.gov.di.test.services.ApiInteractionsService.sendOtpNotification;
import static uk.gov.di.test.services.ApiInteractionsService.updatePhoneNumber;

public class AccountManagementStepDef extends BasePage {
    private static final Logger LOG = LogManager.getLogger(AccountManagementStepDef.class);
    private final World world;

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
        LOG.warn("The User receives the OTP code is not yet implemented");
    }

    @When("the User submits the OTP code to confirm the Phone Number change")
    public void theUserSubmitsTheOTPCodeToConfirmThePhoneNumberChange() {
        world.setOtp("1111");
        updatePhoneNumber(world);
    }

    @Then("the User's Phone Number is updated to {string}")
    public void theUserSPhoneNumberIsUpdatedTo(String newPhoneNumber) {
        LOG.warn(
                "The Users new Phone Number check, called with {}, is not yet implemented",
                newPhoneNumber);
    }
}

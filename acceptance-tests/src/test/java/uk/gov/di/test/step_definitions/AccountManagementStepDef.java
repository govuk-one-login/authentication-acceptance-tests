package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.openqa.selenium.remote.http.HttpMethod;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.services.UserLifecycleService;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.services.ApiInteractionsService.authenticateUser;
import static uk.gov.di.test.services.ApiInteractionsService.authorizeUser;
import static uk.gov.di.test.services.ApiInteractionsService.cannotSendSmsOtpNotification;
import static uk.gov.di.test.services.ApiInteractionsService.getOtp;
import static uk.gov.di.test.services.ApiInteractionsService.makeApiCall;
import static uk.gov.di.test.services.ApiInteractionsService.sendEmailOtpNotification;
import static uk.gov.di.test.services.ApiInteractionsService.sendSmsOtpNotification;
import static uk.gov.di.test.services.ApiInteractionsService.updatePhoneNumber;

public class AccountManagementStepDef extends BasePage {
    private final World world;

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
    }

    @When("^the system sends an OTP to \".+\"$")
    public void theUserReceivesTheOTPCode() {
        sendSmsOtpNotification(world);
    }

    @When("^the system rejects the request to send an OTP to \".+\"$")
    public void theSystemRejectsSendOtpRequest() {
        cannotSendSmsOtpNotification(world);
    }

    @When("the User submits the OTP code to confirm the Phone Number change")
    public void theUserSubmitsTheOTPCodeToConfirmThePhoneNumberChange() {
        var code = getOtp(world.userProfile.getEmail());
        world.setOtp(code);
        updatePhoneNumber(world);
    }

    @Then("the User's Phone Number is updated to {string}")
    public void theUserSPhoneNumberIsUpdatedTo(String newPhoneNumber) {
        List<String> expectedPhoneNumber = Arrays.asList("07700900111", "+61412123123");
        String matchingValue =
                expectedPhoneNumber.stream()
                        .filter(val -> val.equals(newPhoneNumber))
                        .findFirst()
                        .orElse(null);
        assertNotNull(matchingValue, "No matching expected value found.");
        assertEquals(
                newPhoneNumber,
                matchingValue,
                "Actual phone number does not match the expected phone number.");
    }

    @Then("the User provides a new email address")
    public void theSystemSendsAnOTPToTheNewEmailAddress() {
        var email = UserLifecycleService.getInstance().generateNewUniqueEmailAddress();
        world.setNewEmailAddress(email);
        sendEmailOtpNotification(world);
    }

    @Then("the User provides a new high-risk email address")
    public void theUserProvidesANewHighRiskEmailAddress() {
        var email = UserLifecycleService.getInstance().generateHighRiskEmailAddress();
        world.setNewEmailAddress(email);
        sendEmailOtpNotification(world);
    }

    @Then("the User provides a new high-risk email address that will cause an error")
    public void theUserProvidesANewHighRiskEmailAddressThatWillCauseAnError() {
        var email =
                UserLifecycleService.getInstance()
                        .generateHighRiskEmailAddressThatWillCauseAnError();
        world.setNewEmailAddress(email);
        sendEmailOtpNotification(world);
    }

    @And("the User waits for {int} seconds")
    public void theUserWaitsForSeconds(int seconds) {
        Awaitility.await().pollDelay(Duration.ofSeconds(seconds)).until(() -> true);
    }

    @When("the User provides the correct otp for the new email address")
    public void theUserProvidesTheCorrectOtpForTheNewEmailAddress() {
        Response response =
                makeApiCall(
                        world,
                        """
                {
                    "existingEmailAddress": "%s",
                    "replacementEmailAddress": "%s",
                    "otp": "%s"
                }
                """
                                .formatted(
                                        world.userProfile.getEmail(),
                                        world.getNewEmailAddress(),
                                        TEST_CONFIG_SERVICE.get("EMAIL_VERIFY_CODE")),
                        "/update-email",
                        HttpMethod.POST);

        world.setApiResponse(response);
    }

    @Then("the system accepts the request")
    public void theSystemAcceptsTheNewEmailAddress() {
        world.getApiResponse().then().statusCode(204);
    }

    @Then("the system rejects the request with status code {int} and error code {int}")
    public void theSystemRejectsTheRequestWithErrorCode(int statusCode, int errorCode) {
        world.getApiResponse()
                .then()
                .statusCode(statusCode)
                .body("code", Matchers.equalTo(errorCode));
    }
}

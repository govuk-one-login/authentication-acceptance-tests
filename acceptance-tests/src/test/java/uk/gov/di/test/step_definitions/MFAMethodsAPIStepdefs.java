package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static uk.gov.di.test.services.ApiInteractionsService.checkUserHasBackupMFA;
import static uk.gov.di.test.services.ApiInteractionsService.updateBackupPhoneno;

public class MFAMethodsAPIStepdefs {
    private static final Logger LOG = LogManager.getLogger(MFAMethodsAPIStepdefs.class);
    private final World world;

    public MFAMethodsAPIStepdefs(World world) {
        this.world = world;
    }

    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() {
        LOG.info("Retrieving request is made to the API not implemented yet");
    }

    @And("the user has no backup MFA method")
    public void theUserHasNoBackupMFAMethod() {
        checkUserHasBackupMFA(world);
    }

    @When("the User requests to add a backup MFA Phone Number {string}")
    public void theUserRequestsToAddABackupMFAPhoneNumber(String phoneNumber) {
        updateBackupPhoneno(world);
    }

    @Then("the User's back up MFA phoneNumber is updated to {string}")
    public void theUserSBackUpMFAPhoneNumberIsUpdatedTo(String phoneNumber) {}
}

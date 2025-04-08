package uk.gov.di.test.step_definitions;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static uk.gov.di.test.services.ApiInteractionsService.checkUserHasBackupMFA;
import static uk.gov.di.test.services.ApiInteractionsService.addBackupPhoneno;
import static uk.gov.di.test.services.ApiInteractionsService.addBackupAuthApp;
import static uk.gov.di.test.services.ApiInteractionsService.updateBackupPhoneno;
import static uk.gov.di.test.services.ApiInteractionsService.updateBackupAuthApp;
import static uk.gov.di.test.services.ApiInteractionsService.backupMFAPhoneAdded;

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
    public void theUserHasNoBackupMFAMethod() throws JsonProcessingException {
        checkUserHasBackupMFA(world);
    }

    @When("the User requests to add a backup MFA Phone Number {string}")
    public void theUserRequestsToAddABackupMFAPhoneNumber(String phoneNumber) {
        addBackupPhoneno(world);
    }

    @Then("the User's back up MFA phoneNumber is updated to {string}")
    public void theUserSBackUpMFAPhoneNumberIsUpdatedTo(String phoneNumber) throws JsonProcessingException {
        backupMFAPhoneAdded(world);
    }

    @When("the User requests to add a backup MFA Auth App")
    public void theUserRequestsToAddABackupMFAAuthApp() {
        addBackupAuthApp(world);
    }

    @Then("the User's back up MFA Auth App is updated")
    public void theUserSBackUpMFAAuthAppIsUpdated() {

    }

    @When("the User request to update back up MFA as phone number {string}")
    public void theUserRequestToUpdateBackUpMFAAsPhoneNumber(String phoneNumber) {
        updateBackupPhoneno(world);
    }

    @When("the User requests to update a backup MFA Auth App")
    public void theUserRequestsToUpdateABackupMFAAuthApp() {
        updateBackupPhoneno(world);
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static uk.gov.di.test.services.ApiInteractionsService.*;

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

    @And("the User does not have a Backup MFA method")
    public void theUserHasNoBackupMFAMethod() {
        checkUserHasBackupMFA(world);
    }

    @Then("the Users Default MFA is an Auth App")
    public void theUserHasADefaultAuthApp() {
        assertTrue(userHasAuthAppAsDefault(world));
    }

    @When("the User provides the correct otp")
    public void theUserRequestsToAddABackupMFAPhoneNumber() {
        String jsonResponse = addBackupSMS(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(200, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @When("the User request to update back up MFA as phone number {string}")
    public void theUserRequestToUpdateBackUpMFAAsPhoneNumber(String phoneNumber) {
        updateDefaultPhoneNumber(world);
    }

    @When("the User updates their Default MFA to an Auth App")
    public void theUserRequestsToUpdateBackupMFAAuthApp() {
        String jsonResponse = updateDefaultMfaToAuthApp(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            if (actualStatusCode != 200) {
                LOG.info("Error response: {}", payloadJson.get("body"));
            }
            assertEquals(200, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @When("the User cannot update their Default MFA to an Auth App")
    public void theUserCannotTheirDefaultToAuthApp() {
        String jsonResponse = updateDefaultMfaToAuthApp(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            if (actualStatusCode != 200) {
                LOG.info("Error response: {}", payloadJson.get("body"));
            }
            assertEquals(400, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @When("the User is prevented from updating the Default MFA to an Auth App")
    public void theUserIsPreventedFromUpdatingDefault() {
        String jsonResponse = updateDefaultMfaToAuthApp(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(400, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @Then("Phone Number is added as a verified Backup MFA Method")
    public void phoneNumberIsAddedAsAVerifiedBackupMFAMethod() {
        backupSMSMFAAdded(world);
        String jsonResponse = backupAuthMFAAdded(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(200, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @When("the User requests to add a backup MFA Auth App")
    public void theUserRequestsToAddABackupMFAAuthApp() {
        String jsonResponse = addBackupAuthApp(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(200, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @Then("the User's back up MFA Auth App is updated")
    public void theUserSBackUpMFAAuthAppIsUpdated() {
        backupAuthMFAAdded(world);
    }

    @When("the User updates their Default MFA to SMS of {string}")
    public void theUserRequestsToUpdateABackupMFAAuthApp(String phoneNumber) {
        // put new mfa in world
        world.setNewPhoneNumber(phoneNumber);
        sendOtpNotification(world);
    }

    @When("{string} is the new verified Default MFA")
    public void updateDefaultMfa(String phoneNumber) {
        updateDefaultPhoneNumber(world);
    }

    @When("the User requests to delete backup MFA Method")
    public void theUserRequestsToDeleteBackupMFAMethod() {
        deleteBackupMFA(world);
    }

    @Then("the User's backup MFA Method is deleted")
    public void theUserSBackupMFAMethodIsDeleted() {
        /* TODO document why this method is empty */
    }

    @And("the User switches their BACKUP and DEFAULT methods")
    public void theUserSwitchesTheirBACKUPAndDEFAULTMethods() {
        switchMFAMethods(world);
    }

    @When("the User cannot add an Auth App as Backup")
    public void theUserCannotToAddAuthAppAsBackupMFA() {
        String jsonResponse = addBackupAuthApp(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(400, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @Then("appropriate error is returned for invalid request to add Phone Number as SMS Backup MFA")
    public void appropriateErrorIsReturnedForInvalidRequestToAddPhoneNumberAsSMSBackupMFA() {
        String jsonResponse = addBackupSMSInvalidReq(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(400, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }

    @Then(
            "appropriate error is returned for invalid request Unauthorized user credentials for adding backup SMS method")
    public void
            appropriateErrorIsReturnedForInvalidRequestUnauthorizedUserCredentialsForAddingBackupSMSMethod() {
        String jsonResponse = addBackupSMS(world);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode payloadJson = objectMapper.readTree(jsonResponse);
            int actualStatusCode = payloadJson.get("statusCode").asInt();
            assertEquals(400, actualStatusCode);
        } catch (JsonProcessingException e) {
            fail("Error parsing JSON response: " + e.getMessage());
        }
    }
}

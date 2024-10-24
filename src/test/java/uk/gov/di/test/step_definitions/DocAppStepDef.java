package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.DocAppPage;
import uk.gov.di.test.utils.Driver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocAppStepDef extends BasePage {

    private String jsonPayLoad;
    public DocAppPage docAppPage = new DocAppPage();
    protected static final String DOC_APP_URL =
            TEST_CONFIG_SERVICE.getOrDefault(
                    "DOC_APP_URL", "https://doc-app-rp-build.build.stubs.account.gov.uk/");

    @Given("the doc app services are running")
    public void theDocAppServicesAreRunning() {
        // deliberately empty
    }

    @When("the user visits the doc app relying party")
    public void theUserVisitsTheDocAppRelyingParty() {
        Driver.get().get(DOC_APP_URL.toString());
    }

    @And("the user sends a valid json payload")
    public void theUserSendsAValidJsonPayload() {
        jsonPayLoad = "{\"test\" : \"example\"}";
        docAppPage.enterPayLoad(jsonPayLoad);
        docAppPage.clickSubmitButton();
    }

    @Then("the user is taken to the user information page")
    public void theUserIsTakenToTheUserInformationPage() {
        waitForPageLoad("Example - GOV.UK - User Info");
        assertTrue(
                Driver.get().getCurrentUrl().contains("/oidc/authorization-code/callback?code="));
        assertTrue(docAppPage.docAppCredentialsDisplayed());
        assertTrue(docAppPage.idTokenDisplayed());
    }

    @When("the user clicks the My Account link")
    public void theUserClicksTheMyAccountLink() {
        docAppPage.accountLinkClick();
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.DocAppPage;
import uk.gov.di.test.utils.SignIn;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocApp extends SignIn {

    private String jsonPayLoad;
    public DocAppPage docAppPage = new DocAppPage();
    protected static final String DOC_APP_URL =
            System.getenv()
                    .getOrDefault(
                            "DOC_APP_URL",
                            "https://di-auth-stub-relying-party-build-app.london.cloudapps.digital/");

    @Given("the doc app services are running")
    public void theDocAppServicesAreRunning() {}

    @When("the user visits the doc app relying party")
    public void theUserVisitsTheDocAppRelyingParty() {
        driver.get(DOC_APP_URL.toString());
    }

    @And("the user clicks the continue button")
    public void theUserClicksTheContinueButton() {
        docAppPage.continueButtonClick();
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
        assertTrue(driver.getCurrentUrl().contains("/oidc/authorization-code/callback?code="));
        assertTrue(docAppPage.docAppCredentialsDisplayed());
        assertTrue(docAppPage.idTokenDisplayed());
    }

    @When("the user clicks the My Account link")
    public void theUserClicksTheMyAccountLink() {
        docAppPage.accountLinkClick();
    }
}

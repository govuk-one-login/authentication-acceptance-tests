package uk.gov.di.test.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocAppStepDefinitions extends SignInStepDefinitions {

    private String jsonPayLoad;

    protected static final String DOC_APP_URL =
            System.getenv()
                    .getOrDefault(
                            "DOC_APP_URL",
                            "https://di-auth-stub-relying-party-build-app.london.cloudapps.digital/");

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @Given("the doc app services are running")
    public void theDocAppServicesAreRunning() {}

    @When("the user visits the doc app relying party")
    public void theUserVisitsTheDocAppRelyingParty() {
        driver.get(DOC_APP_URL.toString());
    }

    @And("the user clicks the continue button")
    public void theUserClicksTheContinueButton() {
        WebElement continueButton = driver.findElement(By.id("govuk-signin-button"));
        continueButton.click();
    }

    @And("the user sends a valid json payload")
    public void theUserSendsAValidJsonPayload() {
        jsonPayLoad = "{\"test\" : \"example\"}";
        WebElement payloadInputField = driver.findElement(By.id("jsonPayload"));
        payloadInputField.sendKeys(jsonPayLoad);
        WebElement submitButton = driver.findElement(By.name("submit"));
        submitButton.click();
    }

    @Then("the user is taken to the user information page")
    public void theUserIsTakenToTheUserInformationPage() {
        waitForPageLoad("Example - GOV.UK - User Info");
        assertTrue(driver.getCurrentUrl().contains("/oidc/authorization-code/callback?code="));
        WebElement docAppCredentials = driver.findElement(By.id("user-info-doc-app-credential"));
        assertTrue(docAppCredentials.isDisplayed());
        WebElement idToken = driver.findElement(By.id("user-info-phone-number"));
        assertTrue(idToken.isDisplayed());
    }

    @When("the user clicks the My Account link")
    public void theUserClicksTheMyAccountLink() {
        WebElement myAccountLink = driver.findElement(By.cssSelector("#navigation > li > a"));
        myAccountLink.click();
    }

    @Then("the user is taken to the sign in page")
    public void theUserIsTakenToTheSignInPage() {
        waitForPageLoad("Create a GOV.UK account or sign in");
        assertTrue(driver.getCurrentUrl().contains("/sign-in-or-create"));
    }
}

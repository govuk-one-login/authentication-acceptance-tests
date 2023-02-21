package uk.gov.di.test.step_definitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.LoginPage;
import uk.gov.di.test.rp.MicroRP;
import uk.gov.di.test.utils.SignIn;

import java.net.MalformedURLException;

import static uk.gov.di.test.utils.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;

public class NotLoggedIn extends SignIn {

    public LoginPage loginPage = new LoginPage();
    private final MicroRP microRP = new MicroRP();

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @AfterStep
    public void checkAccessibility() {
        Axe.thereAreNoAccessibilityViolations();
    }

    @Given("the not logged in services are running")
    public void theServicesAreRunning() {
        microRP.start();
    }

    @When("the not logged in user visits the stub relying party")
    public void theNotLoggedInUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the not logged in user clicks {string}")
    public void theNotLoggedInUserClicks(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @Then("the not logged in user is taken to the Identity Provider Login Page")
    public void theNotLoggedInUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("a not logged in user tries to sign in")
    public void aNotLoggedInUserTriesToSignIn() {
        driver.get("http://localhost:3031/");
    }
}

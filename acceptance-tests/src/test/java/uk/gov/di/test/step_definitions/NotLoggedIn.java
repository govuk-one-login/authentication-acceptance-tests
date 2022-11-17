package uk.gov.di.test.step_definitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.utils.SignIn;

import java.net.MalformedURLException;

import static uk.gov.di.test.utils.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;

public class NotLoggedIn extends SignIn {

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @AfterStep
    public void checkAccessibility() {
        Axe.thereAreNoAccessibilityViolations();
    }

    @Given("the not logged in services are running")
    public void theServicesAreRunning() {}

    @When("the not logged in user navigates to account root")
    public void theNotLoggedInUserNavigatesToAccountRoot() {
        driver.get(AM_URL.toString());
    }

    @Then("the not logged in user is taken to the Identity Provider Login Page")
    public void theNotLoggedInUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }
}

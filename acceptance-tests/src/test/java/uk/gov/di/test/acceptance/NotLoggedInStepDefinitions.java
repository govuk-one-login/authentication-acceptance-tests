package uk.gov.di.test.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.MalformedURLException;

import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;

public class NotLoggedInStepDefinitions extends SignInStepDefinitions {

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
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

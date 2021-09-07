package uk.gov.di.test.acceptance.accountmanagement;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.acceptance.SignInStepDefinitions;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class AccountManagementStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @After
    public void closeWebdriver() {
        super.closeWebdriver();
    }

    @Given("the account management services are running")
    public void theServicesAreRunning() {}

    @And("the existing account management user has valid credentials")
    public void theExistingAccountManagementUserHasValidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "password";
    }

    @When("the existing account management user navigates to account management")
    public void theExistingAccountManagementUserVisitsTheStubRelyingParty() {
        driver.get(AM_URL.toString());
    }

    @Then("the existing account management user is taken to the Identity Provider Login Page")
    public void theExistingAccountManagementUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoad("Sign in or create a GOV.UK account");
        assertEquals("/sign-in-or-create", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(IDP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals("Sign in or create a GOV.UK account - GOV.UK Account", driver.getTitle());
    }
}

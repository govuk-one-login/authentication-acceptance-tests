package uk.gov.di.test.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class LegalAndPolicyPagesStepDefinitions extends SignInStepDefinitions {

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @Given("the services are running")
    public void theServicesAreRunning() {}

    @When("the user visits the stub relying party")
    public void theExistingUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the user clicks {string}")
    public void theExistingUserClicks(String buttonName) {
        WebElement button = driver.findElement(By.id(buttonName));
        button.click();
    }

    @Then("the user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoad("Create a GOV.UK account or sign in");
        assertEquals("/sign-in-or-create", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(IDP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals("Create a GOV.UK account or sign in - GOV.UK account", driver.getTitle());
    }

    @And("the user clicks link {string}")
    public void theUserClicksLink(String linkText) {
        driver.findElement(By.partialLinkText(linkText)).click();
    }

    @Then("the user is taken to the page with title containing {string}")
    public void theUserIsTakenToThePageWithTitleContaining(String title) {
        waitForPageLoad(title);
    }

    @And("the user navigates back")
    public void theUserNavigatesBack() {
        driver.navigate().back();
    }
}

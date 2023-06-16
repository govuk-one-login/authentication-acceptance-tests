package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import uk.gov.di.test.pages.LoginPage;
import uk.gov.di.test.utils.SignIn;
import uk.gov.di.test.utils.SupportingPages;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class LegalAndPolicy extends SignIn {

    LoginPage loginPage = new LoginPage();

    @Given("the services are running")
    public void theServicesAreRunning() {}

    @When("the user visits the stub relying party")
    public void theExistingUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the user clicks {string}")
    public void theExistingUserClicks(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @Then("the user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoad("Create a GOV.UK One Login or sign in");
        assertEquals("/sign-in-or-create", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Create a GOV.UK One Login or sign in - GOV.UK One Login", driver.getTitle());
    }

    @And("the user clicks link {string}")
    public void theUserClicksLink(String linkText) {
        driver.findElement(By.partialLinkText(linkText)).click();
    }

    @Then("the user is taken to the accessibility statement page")
    public void theUserIsTakenToTheAccessibilityStatementPage() {
        checkPageLoadInTabAndClose(SupportingPages.ACCESSIBILITY_STATEMENT);
    }

    @Then("the user is taken to the GOV.UK cookies page")
    public void theUserIsTakenToTheGOVUKCookiesPage() {
        checkPageLoadInTabAndClose(SupportingPages.GOV_UK_ACCOUNTS_COOKIES);
    }

    @Then("the user is taken to the terms and conditions page")
    public void theUserIsTakenToTheTermsAndConditionsPage() {
        checkPageLoadInTabAndClose(SupportingPages.TERMS_AND_CONDITIONS);
    }

    @Then("the user is taken to the privacy notice page")
    public void theUserIsTakenToThePrivacyNoticePage() {
        checkPageLoadInTabAndClose(SupportingPages.PRIVACY_NOTICE);
    }

    private void checkPageLoadInTabAndClose(SupportingPages page) {
        String currentWindowHandle = driver.getWindowHandle();
        driver.getWindowHandles().stream()
                .filter(h -> !h.equals(currentWindowHandle))
                .findFirst()
                .map(w -> driver.switchTo().window(w))
                .ifPresent(
                        d -> {
                            waitForPageLoadThenValidate(page);
                            d.close();
                        });
        driver.switchTo().window(currentWindowHandle);
    }

    private void waitForPageLoadThenValidate(SupportingPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(driver.getCurrentUrl()).getPath());
    }
}

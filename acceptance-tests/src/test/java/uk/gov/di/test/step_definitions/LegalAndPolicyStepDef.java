package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.SupportingPages;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class LegalAndPolicyStepDef extends BasePage {

    @Given("the services are running")
    public void theServicesAreRunning() {
        // deliberately empty
    }

    @And("the user clicks link {string}")
    public void theUserClicksLink(String linkText) {
        Driver.get().findElement(By.partialLinkText(linkText)).click();
    }

    @Then("the user is taken to the accessibility statement page")
    public void theUserIsTakenToTheAccessibilityStatementPage() {
        checkPageLoadThenGoBackToSignIn(SupportingPages.ACCESSIBILITY_STATEMENT);
    }

    @Then("the user is taken to the GOV.UK cookies page")
    public void theUserIsTakenToTheGOVUKCookiesPage() {
        checkPageLoadThenGoBackToSignIn(SupportingPages.GOV_UK_ACCOUNTS_COOKIES);
    }

    @Then("the user is taken to the terms and conditions page")
    public void theUserIsTakenToTheTermsAndConditionsPage() {
        checkPageLoadThenGoBackToSignIn(SupportingPages.TERMS_AND_CONDITIONS);
    }

    @Then("the user is taken to the privacy notice page")
    public void theUserIsTakenToThePrivacyNoticePage() {
        checkPageLoadThenGoBackToSignIn(SupportingPages.PRIVACY_NOTICE);
    }

    private void waitForPageLoadThenValidate(SupportingPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(Driver.get().getCurrentUrl()).getPath());
    }

    private void checkPageLoadThenGoBackToSignIn(SupportingPages page) {
        waitForPageLoadThenValidate(page);
        Driver.get().navigate().back();
        waitForPageLoad("Create your GOV.UK One Login or sign in");
    }
}

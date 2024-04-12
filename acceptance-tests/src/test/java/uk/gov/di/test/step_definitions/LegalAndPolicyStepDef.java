package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.SupportingPages;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class LegalAndPolicyStepDef extends BasePage {

    @Given("the services are running")
    public void theServicesAreRunning() {}

    @And("the user clicks link {string}")
    public void theUserClicksLink(String linkText) throws MalformedURLException {
        Driver.get().findElement(By.partialLinkText(linkText)).click();
    }

    @Then("the user is taken to the accessibility statement page")
    public void theUserIsTakenToTheAccessibilityStatementPage() throws MalformedURLException {
        checkPageLoadInTabAndClose(SupportingPages.ACCESSIBILITY_STATEMENT);
    }

    @Then("the user is taken to the GOV.UK cookies page")
    public void theUserIsTakenToTheGOVUKCookiesPage() throws MalformedURLException {
        checkPageLoadInTabAndClose(SupportingPages.GOV_UK_ACCOUNTS_COOKIES);
    }

    @Then("the user is taken to the terms and conditions page")
    public void theUserIsTakenToTheTermsAndConditionsPage() throws MalformedURLException {
        checkPageLoadInTabAndClose(SupportingPages.TERMS_AND_CONDITIONS);
    }

    @Then("the user is taken to the privacy notice page")
    public void theUserIsTakenToThePrivacyNoticePage() throws MalformedURLException {
        checkPageLoadInTabAndClose(SupportingPages.PRIVACY_NOTICE);
    }

    private void waitForPageLoadThenValidate(SupportingPages page) throws MalformedURLException {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(Driver.get().getCurrentUrl()).getPath());
    }

    private void checkPageLoadInTabAndClose(SupportingPages page) throws MalformedURLException {
        String currentWindowHandle = Driver.get().getWindowHandle();
        Driver.get().getWindowHandles().stream()
                .filter(h -> !h.equals(currentWindowHandle))
                .findFirst()
                .map(
                        w -> {
                            try {
                                return Driver.get().switchTo().window(w);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                .ifPresent(
                        d -> {
                            try {
                                waitForPageLoadThenValidate(page);
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                            d.close();
                        });
        Driver.get().switchTo().window(currentWindowHandle);
    }
}

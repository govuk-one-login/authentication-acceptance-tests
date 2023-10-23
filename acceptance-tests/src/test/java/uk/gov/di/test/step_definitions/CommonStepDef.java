package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreateOrSignInPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonStepDef extends BasePage {
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();

    @Then("the user is taken to the {string} page")
    public void theUserIsTakenToThePage(String pageTitle) {
        waitForPageLoad(pageTitle);
    }

    @Then("the {string} error message is displayed")
    public void theErrorMessageIsDisplayed(String expectedErrorMessage) {
        waitForThisErrorMessage(expectedErrorMessage);
    }

    @When("the user switches to Welsh language")
    public void theUserSwitchesToWelshLanguage() {
        createOrSignInPage.switchLanguageTo("Welsh");
    }

    @When("the user selects link {string}")
    public void theUserSelectsLink(String linkText) {
        selectLinkByText(linkText);
    }

    @When("the user selects {string} link")
    public void theUserSelectsProblemsWithTheCode(String text) {
        selectLinkByText(text);
    }

    @Then("the link {string} is not available")
    public void theLinkIsNotAvailable(String linkText) {
        assertFalse(isLinkTextDisplayed(linkText));
    }

    @When("the user clicks the continue button")
    public void theUserClicksTheContinueButton() {
        findAndClickContinue();
    }

    @Then("the user is returned to the service")
    public void theUserIsReturnedToTheService() {}

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        assertTrue(isErrorSummaryDisplayed());
    }

    @Then("the user is not shown any error messages")
    public void theNewUserIsNotShownAnErrorMessage() {
        List<WebElement> errorFields = driver.findElements(By.id("code-error"));
        if (!errorFields.isEmpty()) {
            System.out.println("setup-authenticator-app error: " + errorFields.get(0));
        }
        assertTrue(errorFields.isEmpty());
    }

    @When("the user clicks the Back link")
    public void theNewUserClicksTheApplicationBackButton() {
        pressBack();
    }
}

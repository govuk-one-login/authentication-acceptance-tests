package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import uk.gov.di.test.pages.BasePage;

import static org.junit.Assert.assertEquals;

public class CommonStepDef extends BasePage {

    @Then("the user is taken to the {string} page")
    public void theUserIsTakenToThePage(String pageTitle) {
        waitForPageLoad(pageTitle);
    }

    @Then("the {string} error message is displayed")
    public void theErrorMessageIsDisplayed(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, getErrorSummaryText());
    }
}

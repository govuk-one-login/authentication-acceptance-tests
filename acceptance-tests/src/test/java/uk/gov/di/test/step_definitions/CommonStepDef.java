package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreateOrSignInPage;

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
}

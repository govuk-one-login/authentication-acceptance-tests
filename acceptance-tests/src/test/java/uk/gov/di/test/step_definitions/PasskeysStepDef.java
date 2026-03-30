package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreatePasskeysPage;

public class PasskeysStepDef extends BasePage {

    public CreatePasskeysPage createPasskeysPage;

    public PasskeysStepDef() {
        this.createPasskeysPage = new CreatePasskeysPage();
    }

    @When("the user chooses to skip passkey registration")
    public void theUserChoosesToSkipPasskeyRegistration() {
        createPasskeysPage.clickSkipButton();
    }

    @Then("the user is taken to the AMC stub page passkey create page")
    public void theUserIsTakenToTheAMCStubPagePasskeyCreatePage() {
        waitForThisText("AMC stub (passkey create)");
    }
}

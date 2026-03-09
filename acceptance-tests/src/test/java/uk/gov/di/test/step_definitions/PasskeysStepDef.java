package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import uk.gov.di.test.pages.CreatePasskeysPage;

public class PasskeysStepDef {

    public CreatePasskeysPage createPasskeysPage;

    public PasskeysStepDef() {
        this.createPasskeysPage = new CreatePasskeysPage();
    }

    @When("the user chooses to skip passkey registration")
    public void theUserChoosesToSkipPasskeyRegistration() {
        createPasskeysPage.clickSkipButton();
    }
}

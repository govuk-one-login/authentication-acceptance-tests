package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreatePasskeysPage;
import uk.gov.di.test.utils.Driver;

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

    @And("the user dismisses the passkey sign in page if present")
    public void theUserDismissesThePasskeySignInPageIfPresent() {
        waitForReadyStateComplete();
        if (Driver.get().getTitle().contains("Sign in with your face, fingerprint or passcode")) {
            selectLinkByText("Sign in another way");
        }
    }

    @And("the user dismisses the passkey registration page if present")
    public void theUserDismissesThePasskeyRegistrationPageIfPresent() {
        waitForReadyStateComplete();
        if (Driver.get().getCurrentUrl().contains("/create-passkey")) {
            createPasskeysPage.clickSkipButton();
        }
    }
}

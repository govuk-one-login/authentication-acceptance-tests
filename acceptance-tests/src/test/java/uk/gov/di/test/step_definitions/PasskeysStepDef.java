package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreatePasskeysPage;
import uk.gov.di.test.services.VirtualAuthenticatorLifecycleService;

public class PasskeysStepDef extends BasePage {

    public CreatePasskeysPage createPasskeysPage;
    public static final VirtualAuthenticatorLifecycleService virtualAuthenticatorLifecycleService =
            VirtualAuthenticatorLifecycleService.getInstance();

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

    @And("the user dismisses the passkey registration page if present")
    public void dismissPasskeyRegistrationPageIfPresent() {
        createPasskeysPage.dismissIfPresent();
    }

    @Given("the user will succeed verification")
    public void willSucceedVerification() {
        virtualAuthenticatorLifecycleService.enableUserVerification();
    }

    @Given("the user will fail verification")
    public void willFailVerification() {
        virtualAuthenticatorLifecycleService.disableUserVerification();
    }
}

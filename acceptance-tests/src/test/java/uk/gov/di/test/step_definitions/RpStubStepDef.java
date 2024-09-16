package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();

    @ParameterType("\\[((?:[\\w-_]+,?)+)\\]")
    public String[] rpStubOptions(String opts) {
        if (opts == null || opts.isEmpty() || opts.equalsIgnoreCase("default")) {
            return null;
        }
        return opts.split(",");
    }

    @When("the user comes from the stub relying party with default options")
    @When(
            "the user returns from the stub relying party with default options( before the lockout expires)")
    public void theUserVisitsTheStubRelyingParty() {
        rpStubPage.goToRpStub();
        rpStubPage.useDefaultOptionsAndContinue();
        setAnalyticsCookieTo(false);
    }

    @When("the user comes from the stub relying party with options: {rpStubOptions}")
    @When(
            "the user returns from the stub relying party with options: {rpStubOptions}( before the lockout expires)")
    public void theExistingUserVisitsTheStubRelyingParty(String[] rpStubOptions) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue(rpStubOptions);
        setAnalyticsCookieTo(false);
    }

    @When("the user comes from the stub relying party with option {word}")
    @When(
            "the user returns from the stub relying party with option {word}( before the lockout expires)")
    public void theExistingUserVisitsTheStubRelyingParty(String option) {
        theExistingUserVisitsTheStubRelyingParty(new String[] {option});
    }

    @Then("the user is forcibly logged out")
    public void theUserIsLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }
}

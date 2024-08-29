package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();
    // Dynamo dynamo = new Dynamo();
    // TestData testData = new TestData();

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        // testData.SetupUserData(userEmail);
        // dynamo.createUpdateStandardSmsUser(userEmail);
        // dynamo.createOrUpdateUser(userEmail);
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue(options);
        setAnalyticsCookieTo(false);
    }

    @When("the user signs back in with their new account")
    @When("the user is required to uplift")
    public void theUserSignsBackInWithTheirNewAccount() {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
    }

    @Then("the user is forcibly logged out")
    public void theUserIsLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }
}

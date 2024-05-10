package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.utils.Dynamo;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();
    Dynamo dynamo = new Dynamo();

    @When("the user {string} comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String userEmail, String options) {
        dynamo.createUpdateStandardSmsUser(userEmail);
        // dynamo.createOrUpdateUser(userEmail);
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue(options);
        setAnalyticsCookieTo(false);
    }
}

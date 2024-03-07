package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue(options);
        setAnalyticsCookieTo(false);
    }
}

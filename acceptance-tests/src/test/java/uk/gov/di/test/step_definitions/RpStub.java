package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.utils.SignIn;

public class RpStub extends SignIn {

    RpStubPage rpStubPage = new RpStubPage();

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsById(options);
        findAndClickContinue();
    }
}

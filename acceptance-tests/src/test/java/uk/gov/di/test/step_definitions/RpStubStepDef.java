package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.rp.MicroRP;

import static uk.gov.di.test.rp.MicroRP.JourneyType.*;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();

    MicroRP rp =
            new MicroRP(
                            System.getenv("RP_CLIENT_ID"),
                            System.getenv("OP_URL"),
                            System.getenv("RP_SIGNING_KEY"))
                    .start();

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue(options);
        setAnalyticsCookieTo(false);
    }

    @When("the user starts a low confidence journey")
    public void theUserStartsALowConfidenceJourney() {
        driver.get(rp.startJourneyUrl(LOW_CONFIDENCE));
        setAnalyticsCookieTo(false);
    }

    @When("the user starts a medium confidence journey")
    public void theUserStartsAMediumConfidenceJourney() {
        driver.get(rp.startJourneyUrl(MED_CONFIDENCE));
        setAnalyticsCookieTo(false);
    }
}

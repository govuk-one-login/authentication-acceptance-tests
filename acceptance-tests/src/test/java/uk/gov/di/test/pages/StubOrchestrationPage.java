package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class StubOrchestrationPage extends StubStartPage {

    @Override
    public By getReauthIdField() {
        return By.id("reauth-id-token");
    }

    @Override
    public void goToRpStub() {
        Driver.get().get(RP_URL.toString());
        waitForThisText("Orchestration stub");
    }

    @Override
    public boolean supportsLogout() {
        return false;
    }

    @Override
    public void waitForReturnToTheService() {
        waitForPageLoad("GOV.UK - The best place to find government services and information");
    }

    @Override
    public void uplift(String options) {
        selectLinkByText("Start again");
        selectRpOptionsById("authenticated-2");
        selectRpOptionsByIdAndContinue("authenticated-level");
    }

    @Override
    public void selectRpOptionsByIdAndContinue(String opts) {
        selectRpOptionsById(opts);
        findAndClickButtonByText("Submit");
    }
}

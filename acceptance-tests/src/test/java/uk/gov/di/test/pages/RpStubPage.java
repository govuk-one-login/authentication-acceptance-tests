package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class RpStubPage extends StubStartPage {

    @Override
    public By getReauthIdField() {
        return By.id("reauth-id-token");
    }

    @Override
    public void goToRpStub() {
        Driver.get().get(RP_URL.toString());
        waitForThisText("Request Object");
    }

    @Override
    public boolean supportsLogout() {
        return true;
    }

    @Override
    public void waitForReturnToTheService() {
        waitForPageLoad("Example - GOV.UK - User Info");
    }

    @Override
    public void uplift(String options) {
        goToRpStub();
        selectRpOptionsByIdAndContinue("2fa-on");
        setAnalyticsCookieTo(false);
    }

    @Override
    public void selectRpOptionsByIdAndContinue(String opts) {
        if (!opts.isEmpty() && !opts.equalsIgnoreCase("default")) {
            String[] ids = opts.split(",");
            for (String id : ids) {
                Driver.get().findElement(By.id(id)).click();
            }
        }
        findAndClickContinue();
    }

    public void reauthRequired(String idToken) {
        goToRpStub();
        enterReauthIdToken(idToken);
        selectRpOptionsByIdAndContinue("2fa-on,prompt-login,request-object");
        setAnalyticsCookieTo(false);
    }
}

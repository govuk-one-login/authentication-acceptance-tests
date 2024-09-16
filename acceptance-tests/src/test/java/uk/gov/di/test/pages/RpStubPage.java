package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class RpStubPage extends BasePage {
    By reauthIdTokenField = By.id("reauth-id-token");

    public void goToRpStub() {
        Driver.get().get(RP_URL.toString());
        waitForThisText("Request Object");
    }

    public void selectRpOptionsByIdAndContinue(String[] opts) {
        if (opts != null) {
            for (String id : opts) {
                Driver.get().findElement(By.id(id)).click();
            }
        }
        findAndClickContinue();
    }

    public void useDefaultOptionsAndContinue() {
        selectRpOptionsByIdAndContinue(null);
    }

    public void enterReauthIdToken(String token) {
        Driver.get().findElement(reauthIdTokenField).clear();
        Driver.get().findElement(reauthIdTokenField).sendKeys(token);
    }

    public void reauthRequired(String idToken) {
        goToRpStub();
        enterReauthIdToken(idToken);
        selectRpOptionsByIdAndContinue(new String[] {"2fa-on", "prompt-login", "request-object"});
        setAnalyticsCookieTo(false);
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class RpStubPage extends BasePage {
    By reauthIdTokenField = By.id("reauth-id-token");

    public void goToRpStub() throws MalformedURLException {
        // driver.get(RP_URL.toString());
        Driver.get().get(RP_URL.toString());
        waitForThisText("Request Object");
    }

    public void selectRpOptionsByIdAndContinue(String opts) throws MalformedURLException {
        if (!opts.isEmpty() && !opts.equalsIgnoreCase("default")) {
            String ids[] = opts.split(",");
            for (String id : ids) {
                Driver.get().findElement(By.id(id)).click();
            }
        }
        findAndClickContinue();
    }

    public void enterReauthIdToken(String token) throws MalformedURLException {
        Driver.get().findElement(reauthIdTokenField).clear();
        Driver.get().findElement(reauthIdTokenField).sendKeys(token);
    }

    public void reauthRequired(String idToken) throws MalformedURLException {
        goToRpStub();
        enterReauthIdToken(idToken);
        selectRpOptionsByIdAndContinue("2fa-on,prompt-login,request-object");
        setAnalyticsCookieTo(false);
    }
}

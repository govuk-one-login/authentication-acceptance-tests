package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class RpStubPage extends BasePage {
    By reauthIdTokenField = By.id("reauth-id-token");

    public void goToRpStub() {
        driver.get(RP_URL.toString());
        waitForThisText("Request Object");
    }

    public void selectRpOptionsByIdAndContinue(String opts) {
        if (!opts.isEmpty() && !opts.equalsIgnoreCase("default")) {
            String ids[] = opts.split(",");
            for (String id : ids) {
                driver.findElement(By.id(id)).click();
            }
        }
        findAndClickContinue();
    }

    public void enterReauthIdToken(String token) {
        driver.findElement(reauthIdTokenField).clear();
        driver.findElement(reauthIdTokenField).sendKeys(token);
    }

    public void reauthRequired(String idToken) {
        goToRpStub();
        enterReauthIdToken(idToken);
        selectRpOptionsByIdAndContinue("2fa-on,prompt-login,request-object");
        setAnalyticsCookieTo(false);
    }
}

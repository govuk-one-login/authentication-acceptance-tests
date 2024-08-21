package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public abstract class StubStartPage extends BasePage {
    public abstract void goToRpStub();

    public abstract By getReauthIdField();

    public abstract boolean supportsLogout();

    public abstract void selectRpOptionsByIdAndContinue(String opts);

    public abstract void waitForReturnToTheService();

    protected void selectRpOptionsById(String opts) {
        if (opts.contains("2fa-on")) {
            opts = opts.replace("2fa-on", "level");
        }
        if (opts.contains("2fa-off")) {
            opts = opts.replace("2fa-off", "level-2");
        }
        if (!opts.isEmpty() && !opts.equalsIgnoreCase("default")) {
            String[] ids = opts.split(",");
            for (String id : ids) {
                Driver.get().findElement(By.id(id)).click();
            }
        }
    }

    public abstract void uplift(String options);

    protected void enterReauthIdToken(String token) {
        Driver.get().findElement(getReauthIdField()).clear();
        Driver.get().findElement(getReauthIdField()).sendKeys(token);
    }

    public void reauthRequired(String idToken) {
        goToRpStub();
        enterReauthIdToken(idToken);
        selectRpOptionsByIdAndContinue("2fa-on,prompt-login,request-object");
        setAnalyticsCookieTo(false);
    }
}

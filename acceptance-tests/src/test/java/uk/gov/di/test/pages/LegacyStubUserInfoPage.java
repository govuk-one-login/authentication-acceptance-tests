package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class LegacyStubUserInfoPage extends StubUserInfoPage {

    By idTokenField = By.id("user-info-id-token");
    By accessTokenField = By.id("user-info-access-token");
    By logoutButton = By.name("logout");

    @Override
    public String getReauthCorrelationToken() {
        return Driver.get().findElement(idTokenField).getText();
    }

    @Override
    public String getAccessToken() {
        return Driver.get().findElement(accessTokenField).getText();
    }

    @Override
    public void logoutOfAccount() {
        Driver.get().findElement(logoutButton).click();
        Driver.get().manage().deleteAllCookies();
    }

    @Override
    public void waitForReturnToTheService() {
        waitForPageLoad("Example - GOV.UK - User Info");
    }
}

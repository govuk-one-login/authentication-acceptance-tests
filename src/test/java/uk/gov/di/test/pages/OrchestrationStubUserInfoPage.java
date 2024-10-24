package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class OrchestrationStubUserInfoPage extends StubUserInfoPage {
    By idTokenField = By.cssSelector("dl.govuk-summary-list > div:nth-child(1) > dd");
    By accessTokenField = By.id("user-info-access-token");
    private static final String TITLE = OrchestrationStubStartPage.TITLE;

    @Override
    public String getIdToken() {
        return Driver.get().findElement(idTokenField).getText();
    }

    @Override
    public String getAccessToken() {
        return Driver.get().findElement(accessTokenField).getText();
    }

    @Override
    public void logoutOfAccount() {
        Driver.get().manage().deleteAllCookies();
    }

    @Override
    public void waitForReturnToTheService() {
        waitForPageLoad(TITLE);
    }
}

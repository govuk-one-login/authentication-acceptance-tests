package uk.gov.di.test.pages;

import org.json.JSONObject;
import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class OrchestrationStubUserInfoPage extends StubUserInfoPage {
    By userInfoJsonSelector =
            By.cssSelector("dl.govuk-summary-list > div:nth-child(2) > dd > pretty-json");
    By accessTokenField = By.id("user-info-access-token");
    private static final String TITLE = OrchestrationStubStartPage.TITLE;

    @Override
    public String getReauthCorrelationToken() {
        JSONObject userInfo = getUserInfo();
        return userInfo.getString("rp_pairwise_id");
    }

    public JSONObject getUserInfo() {
        String rawUserInfoJson = Driver.get().findElement(userInfoJsonSelector).getText();
        return new JSONObject(rawUserInfoJson);
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

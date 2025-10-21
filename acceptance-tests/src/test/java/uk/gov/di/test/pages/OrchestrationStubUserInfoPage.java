package uk.gov.di.test.pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.Driver;

public class OrchestrationStubUserInfoPage extends StubUserInfoPage {
    By idTokenField = By.cssSelector("dl > div:nth-child(4) > dd > pretty-json");
    By accessTokenField = By.cssSelector("dl > div:nth-child(3) > dd");
    private static final String TITLE = OrchestrationStubStartPage.TITLE;

    @Override
    public String getIdToken() {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        driver ->
                                driver.findElement(idTokenField)
                                        .getText()
                                        .contains("rp_pairwise_id"));

        String userInfoRaw = Driver.get().findElement(idTokenField).getText();
        String jsonString = userInfoRaw.substring(userInfoRaw.lastIndexOf('{')).trim();
        JsonObject userInfo = JsonParser.parseString(jsonString).getAsJsonObject();
        return userInfo.get("rp_pairwise_id").getAsString();
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

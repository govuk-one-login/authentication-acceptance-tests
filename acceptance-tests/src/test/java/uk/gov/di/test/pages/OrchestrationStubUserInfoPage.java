package uk.gov.di.test.pages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class OrchestrationStubUserInfoPage extends StubUserInfoPage {
    By idTokenField = By.cssSelector("dl > div:nth-child(4) > dd > pretty-json");
    By accessTokenField = By.cssSelector("dl > div:nth-child(3) > dd");
    By userInfoField = By.cssSelector("pretty-json");
    private static final String TITLE = OrchestrationStubStartPage.TITLE;

    @Override
    public String getIdToken() {
        try {
            String jsonText = Driver.get().findElement(userInfoField).getText();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonText);
            return root.get("rp_pairwise_id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract rp_pairwise_id from user info JSON", e);
        }
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

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class UserInformationPage extends BasePage {

    By idTokenField = By.id("user-info-id-token");
    By accessTokenField = By.id("user-info-access-token");
    By logoutButton = By.name("logout");

    public String getIdToken() {
        return Driver.get().findElement(idTokenField).getText();
    }

    public String getAccessToken() {
        return Driver.get().findElement(accessTokenField).getText();
    }

    public void logoutOfAccount() {
        Driver.get().findElement(logoutButton).click();
    }
}

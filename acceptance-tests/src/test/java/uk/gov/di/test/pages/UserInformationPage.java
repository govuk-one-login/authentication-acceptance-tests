package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class UserInformationPage extends BasePage {

    By idTokenField = By.id("user-info-id-token");
    By logoutButton = By.name("logout");

    public String getIdToken() throws MalformedURLException {
        return Driver.get().findElement(idTokenField).getText();
    }

    public void logoutOfAccount() throws MalformedURLException {
        Driver.get().findElement(logoutButton).click();
    }
}

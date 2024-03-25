package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class UserInformationPage extends BasePage {

    By idTokenField = By.id("user-info-id-token");
    By logoutButton = By.name("logout");

    public String getIdToken() {
        return driver.findElement(idTokenField).getText();
    }

    public void logoutOfAccount() {
        driver.findElement(logoutButton).click();
    }
}

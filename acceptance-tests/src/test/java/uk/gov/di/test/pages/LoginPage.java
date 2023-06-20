package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class LoginPage extends SignIn {

    By logout = By.name("logout");
    By emailErrorDescriptionDetails = By.className("govuk-error-summary");

    public void buttonClick(String name) {
        driver.findElement(By.id(name)).click();
    }

    public void logoutButtonClick() {
        driver.findElement(logout).click();
    }

    public Boolean emailErrorDescriptionDetailsIsDisplayed() {
        return driver.findElement(emailErrorDescriptionDetails).isDisplayed();
    }
}

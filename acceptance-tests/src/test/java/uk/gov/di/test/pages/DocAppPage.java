package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class DocAppPage extends SignIn {

    By myAccountLink = By.cssSelector("#navigation > li > a");

    By continueButton = By.id("govuk-signin-button");
    By payloadInputField = By.id("jsonPayload");
    By submitButton = By.name("submit");

    By docAppCredentials = By.id("user-info-doc-app-credential");

    By idToken = By.id("user-info-phone-number");

    public void continueButtonClick() {
        driver.findElement(continueButton).click();
    }

    public void accountLinkClick() {
        driver.findElement(myAccountLink).click();
    }

    public void enterPayLoad(String jsonPayLoad) {
        driver.findElement(payloadInputField).sendKeys(jsonPayLoad);
    }

    public void clickSubmitButton() {
        driver.findElement(submitButton).click();
    }

    public Boolean docAppCredentialsDisplayed() {
        return driver.findElement(docAppCredentials).isDisplayed();
    }

    public Boolean idTokenDisplayed() {
        return driver.findElement(idToken).isDisplayed();
    }
}

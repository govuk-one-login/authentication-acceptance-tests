package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class DocAppPage extends BasePage {

    By myAccountLink = By.cssSelector("#navigation > li > a");

    By continueButton = By.id("govuk-signin-button");
    By payloadInputField = By.id("jsonPayload");
    By submitButton = By.name("submit");

    By docAppCredentials = By.id("user-info-doc-app-credential");

    By idToken = By.id("user-info-phone-number");

    public void continueButtonClick() {
        Driver.getOrCreate().findElement(continueButton).click();
    }

    public void accountLinkClick() {
        Driver.getOrCreate().findElement(myAccountLink).click();
    }

    public void enterPayLoad(String jsonPayLoad) {
        Driver.getOrCreate().findElement(payloadInputField).sendKeys(jsonPayLoad);
    }

    public void clickSubmitButton() {
        Driver.getOrCreate().findElement(submitButton).click();
    }

    public Boolean docAppCredentialsDisplayed() {
        return Driver.getOrCreate().findElement(docAppCredentials).isDisplayed();
    }

    public Boolean idTokenDisplayed() {
        return Driver.getOrCreate().findElement(idToken).isDisplayed();
    }
}

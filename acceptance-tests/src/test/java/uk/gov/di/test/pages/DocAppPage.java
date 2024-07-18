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
        Driver.get().findElement(continueButton).click();
    }

    public void accountLinkClick() {
        Driver.get().findElement(myAccountLink).click();
    }

    public void enterPayLoad(String jsonPayLoad) {
        Driver.get().findElement(payloadInputField).sendKeys(jsonPayLoad);
    }

    public void clickSubmitButton() {
        Driver.get().findElement(submitButton).click();
    }

    public Boolean docAppCredentialsDisplayed() {
        return Driver.get().findElement(docAppCredentials).isDisplayed();
    }

    public Boolean idTokenDisplayed() {
        return Driver.get().findElement(idToken).isDisplayed();
    }
}

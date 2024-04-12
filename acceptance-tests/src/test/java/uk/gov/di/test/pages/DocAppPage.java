package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class DocAppPage extends BasePage {

    By myAccountLink = By.cssSelector("#navigation > li > a");

    By continueButton = By.id("govuk-signin-button");
    By payloadInputField = By.id("jsonPayload");
    By submitButton = By.name("submit");

    By docAppCredentials = By.id("user-info-doc-app-credential");

    By idToken = By.id("user-info-phone-number");

    public void continueButtonClick() throws MalformedURLException {
        Driver.get().findElement(continueButton).click();
    }

    public void accountLinkClick() throws MalformedURLException {
        Driver.get().findElement(myAccountLink).click();
    }

    public void enterPayLoad(String jsonPayLoad) throws MalformedURLException {
        Driver.get().findElement(payloadInputField).sendKeys(jsonPayLoad);
    }

    public void clickSubmitButton() throws MalformedURLException {
        Driver.get().findElement(submitButton).click();
    }

    public Boolean docAppCredentialsDisplayed() throws MalformedURLException {
        return Driver.get().findElement(docAppCredentials).isDisplayed();
    }

    public Boolean idTokenDisplayed() throws MalformedURLException {
        return Driver.get().findElement(idToken).isDisplayed();
    }
}

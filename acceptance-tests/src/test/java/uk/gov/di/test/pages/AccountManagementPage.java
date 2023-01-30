package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.utils.SignIn;

public class AccountManagementPage extends SignIn {

    By upperErrorMessage = By.cssSelector(".govuk-error-summary__body li a");
    By aWayToGetSecurityCodeText = By.xpath("//ul/li[2]");
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");
    By accountCreationLink = By.id("create-account-link");
    By emailAddressField = By.id("email");
    By summaryTitleError = By.id("error-summary-title");
    By acceptPolicyCookie = By.id("policy-cookies-accepted");
    By rejectPolicyCookie = By.id("policy-cookies-rejected");
    By savePolicyCookie = By.id("save-cookie-settings");
    By back = By.id("go-back-link");
    By phoneNumberField = By.id("phoneNumber");
    By internationalPhoneNumberField = By.id("internationalPhoneNumber");
    By iDoNotHaveUkMobilePhoneNumberCheckbox = By.id("hasInternationalPhoneNumber");

    public void linkClick(String href) {
        driver.findElement(By.xpath("//a[@href=\"" + href + "\"]")).click();
    }

    public WebElement summaryTitleError() {
        return driver.findElement(summaryTitleError);
    }

    public void enterCurrentPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void acceptPolicyCookie() {
        driver.findElement(acceptPolicyCookie).click();
    }

    public void rejectPolicyCookie() {
        driver.findElement(rejectPolicyCookie).click();
    }

    public void clickBack() {
        driver.findElement(back).click();
    }

    public void savePolicyCookie() {
        driver.findElement(savePolicyCookie).click();
    }

    public void enterEmailAddress(String emailAddress) {
        driver.findElement(emailAddressField).sendKeys(emailAddress);
    }

    public void enterPhoneNumber(String phoneNumber) {
        driver.findElement(phoneNumberField).clear();
        driver.findElement(phoneNumberField).sendKeys(phoneNumber);
    }

    public void clickAccountCreationLink() {
        driver.findElement(accountCreationLink).click();
    }

    public void enterNewPassword(String newPassword) {
        driver.findElement(passwordField).sendKeys(newPassword);
        driver.findElement(confirmPasswordField).sendKeys(newPassword);
    }

    public boolean isInternationalMobileNumberFieldDisplayed() {
        return driver.findElement(internationalPhoneNumberField).isDisplayed();
    }

    public void tickIDoNotHaveUkMobileNumber() {
        driver.findElement(iDoNotHaveUkMobilePhoneNumberCheckbox).click();
    }

    public boolean getStatusOfUKMobileNumberField() {
        return driver.findElement(phoneNumberField).isEnabled();
    }

    public String getAWayToGetSecurityCodeText() {
        return driver.findElement(aWayToGetSecurityCodeText).getText();
    }

    public String getUpperErrorMessageText() {
        return driver.findElement(upperErrorMessage).getText().trim();
    }

    public void enterInternationalMobilePhoneNumber(String internationalMobilePhoneNumber) {
        driver.findElement(internationalPhoneNumberField).clear();
        driver.findElement(internationalPhoneNumberField).sendKeys(internationalMobilePhoneNumber);
    }
}

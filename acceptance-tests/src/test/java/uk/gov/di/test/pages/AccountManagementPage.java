package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.utils.SignIn;

public class AccountManagementPage extends SignIn {

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
        driver.findElement(phoneNumberField).sendKeys(phoneNumber);
    }

    public void clickAccountCreationLink() {
        driver.findElement(accountCreationLink).click();
    }

    public void enterNewPassword(String newPassword) {
        driver.findElement(passwordField).sendKeys(newPassword);
        driver.findElement(confirmPasswordField).sendKeys(newPassword);
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class LoginPage extends SignIn {
    By signInLink = By.id("sign-in-link");
    By emailAddressField = By.id("email");
    By confirmPasswordField = By.id("confirm-password");

    By logout = By.name("logout");
    By termsAndConditions = By.name("termsAndConditionsResult");
    By passwordField = By.id("password");
    By problemWithTheCode = By.className("govuk-details__summary");
    By sendTheCodeAgainLink = By.cssSelector("#form-tracking > details > div > p > a");
    By getNewTheCodeAgainLink = By.cssSelector("#main-content > div > div > p:nth-child(3) > a");

    By securityCode = By.cssSelector("#main-content > div > div > form > button");
    By sixDigitSecurityCodeField = By.id("code");
    By emailDescriptionDetails = By.id("user-info-email");
    By emailErrorDescriptionDetails = By.className("govuk-error-summary");

    By continueButton = By.cssSelector("#main-content > div > div > form > button");
    By backButton = By.className("govuk-back-link");
    By forgottenMyPasswordLink = By.xpath("//a[contains(text(), \"I’ve forgotten my password\")]");

    public void buttonClick(String name) {
        driver.findElement(By.id(name)).click();
    }

    public void logoutButtonClick() {
        driver.findElement(logout).click();
    }

    public void termsAndConditionsButtonClick() {
        driver.findElement(termsAndConditions).click();
    }

    public void signInLinkClick() {
        driver.findElement(signInLink).click();
    }

    public void getNewTheCodeAgainLinkClick() {
        driver.findElement(getNewTheCodeAgainLink).click();
    }

    public void problemWithTheCodeClick() {
        driver.findElement(problemWithTheCode).click();
    }

    public void securityCodeClick() {
        driver.findElement(securityCode).click();
    }

    public void sendTheCodeAgainLinkClick() {
        driver.findElement(sendTheCodeAgainLink).click();
    }

    public void enterEmailAddress(String email) {
        driver.findElement(emailAddressField).clear();
        driver.findElement(emailAddressField).sendKeys(email);
    }

    public void enterSixDigitSecurityCode(String code) {
        driver.findElement(sixDigitSecurityCodeField).clear();
        driver.findElement(sixDigitSecurityCodeField).sendKeys(code);
    }

    public int getSixDigitSecurityCodeLength() {
        driver.findElement(sixDigitSecurityCodeField).clear();
        return driver.findElement(sixDigitSecurityCodeField).getAttribute("value").length();
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        driver.findElement(confirmPasswordField).sendKeys(password);
    }

    public String emailDescription() {
        return driver.findElement(emailDescriptionDetails).getText().trim();
    }

    public String continueButtonText() {
        return driver.findElement(continueButton).getText();
    }

    public String backButtonText() {
        return driver.findElement(backButton).getText();
    }

    public Boolean emailErrorDescriptionDetailsIsDisplayed() {
        return driver.findElement(emailErrorDescriptionDetails).isDisplayed();
    }

    public void clickForgottenMyPassword() {
        driver.findElement(forgottenMyPasswordLink).click();
    }
}

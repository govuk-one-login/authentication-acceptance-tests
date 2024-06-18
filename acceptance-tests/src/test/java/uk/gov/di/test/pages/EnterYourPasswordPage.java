package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class EnterYourPasswordPage extends BasePage {

    By passwordField = By.id("password");
    By forgottenPasswordLink = By.xpath("//a[@href='/reset-password-request']");

    public void enterPassword(String pw) {
        clearFieldAndEnter(passwordField, pw);
    }

    public void clickForgottenPasswordLink() {
        driver.findElement(forgottenPasswordLink).click();
    }

    public void enterPasswordAndContinue(String pw) {
        enterPassword(pw);
        findAndClickContinue();
    }

    public void enterIncorrectPasswordNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Enter your password");
            enterPasswordAndContinue("IncorrectPassword");
            System.out.println("Incorrect password entry count: " + (index + 1));
        }
    }

    public void enterCorrectPasswordAndContinue() {
        String correctPassword = System.getenv().get("TEST_USER_PASSWORD");
        enterPasswordAndContinue(correctPassword);
    }

    public void enterNewPasswordAndContinue() {
        enterPasswordAndContinue(NEW_VALID_PASSWORD);
    }
}

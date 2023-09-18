package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class CreateYourPasswordPage extends BasePage {
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");

    public void enterPassword(String pw) {
        clearFieldAndEnter(passwordField, pw);
    }

    public void enterConfirmPassword(String cpw) {
        clearFieldAndEnter(confirmPasswordField, cpw);
    }

    public void enterBothPasswordsAndContinue(String pw, String cpw) {
        enterPassword(pw);
        enterConfirmPassword(cpw);
        findAndClickContinue();
    }
}

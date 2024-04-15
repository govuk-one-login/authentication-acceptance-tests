package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class ResetYourPasswordPage extends BasePage {
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");

    public void enterPassword(String password) {
        clearFieldAndEnter(passwordField, password);
    }

    public void enterConfirmPassword(String password) {
        clearFieldAndEnter(confirmPasswordField, password);
    }

    public void enterPasswordResetDetailsAndContinue(String newPassword, String confirmPassword) {
        enterPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        findAndClickContinue();
    }
}

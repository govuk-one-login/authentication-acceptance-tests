package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import java.net.MalformedURLException;

public class ResetYourPasswordPage extends BasePage {
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");

    public void enterPassword(String password) throws MalformedURLException {
        clearFieldAndEnter(passwordField, password);
    }

    public void enterConfirmPassword(String password) throws MalformedURLException {
        clearFieldAndEnter(confirmPasswordField, password);
    }

    public void enterPasswordResetDetailsAndContinue(String newPassword, String confirmPassword)
            throws MalformedURLException {
        enterPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        findAndClickContinue();
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import java.net.MalformedURLException;

public class CreateYourPasswordPage extends BasePage {
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");

    public void enterPassword(String pw) throws MalformedURLException {
        clearFieldAndEnter(passwordField, pw);
    }

    public void enterConfirmPassword(String cpw) throws MalformedURLException {
        clearFieldAndEnter(confirmPasswordField, cpw);
    }

    public void enterBothPasswordsAndContinue(String pw, String cpw) throws MalformedURLException {
        enterPassword(pw);
        enterConfirmPassword(cpw);
        findAndClickContinue();
    }
}

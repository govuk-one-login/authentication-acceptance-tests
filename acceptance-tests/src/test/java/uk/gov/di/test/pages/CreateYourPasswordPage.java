package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class CreateYourPasswordPage extends SignIn {
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

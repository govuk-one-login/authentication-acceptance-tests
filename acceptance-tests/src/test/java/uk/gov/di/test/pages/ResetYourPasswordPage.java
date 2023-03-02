package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class ResetYourPasswordPage extends SignIn {
    By passwordField = By.id("password");
    By confirmPasswordField = By.id("confirm-password");

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        driver.findElement(confirmPasswordField).sendKeys(password);
    }

    public void enterPasswordResetDetails(String newPassword, String confirmPassword) {
        enterPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        findAndClickContinue();
    }
}

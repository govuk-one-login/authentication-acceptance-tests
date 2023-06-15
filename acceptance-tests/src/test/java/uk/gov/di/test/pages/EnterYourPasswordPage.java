package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class EnterYourPasswordPage extends SignIn {

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
}

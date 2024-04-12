package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class EnterYourPasswordPage extends BasePage {

    By passwordField = By.id("password");
    By forgottenPasswordLink = By.xpath("//a[@href='/reset-password-request']");

    public void enterPassword(String pw) throws MalformedURLException {
        clearFieldAndEnter(passwordField, pw);
    }

    public void clickForgottenPasswordLink() throws MalformedURLException {
        Driver.get().findElement(forgottenPasswordLink).click();
    }

    public void enterPasswordAndContinue(String pw) throws MalformedURLException {
        enterPassword(pw);
        findAndClickContinue();
    }

    public void enterIncorrectPasswordNumberOfTimes(Integer numberOfTimes)
            throws MalformedURLException {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Enter your password");
            enterPasswordAndContinue("IncorrectPassword");
            System.out.println("Incorrect password entry count: " + (index + 1));
        }
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class YouHaveAGOVUKOneLoginPage extends BasePage {

    By passwordField = By.id("password");

    public void enterPassword(String pw) {
        clearFieldAndEnter(passwordField, pw);
    }

    public void enterPasswordAndContinue(String pw) {
        enterPassword(pw);
        findAndClickContinue();
    }

    public void enterCorrectPasswordAndContinue() {
        String correctPassword = System.getenv("TEST_USER_PASSWORD");
        enterPasswordAndContinue(correctPassword);
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class EnterYourEmailAddressToSignInPage extends BasePage {
    By emailField = By.id("email");

    public void enterEmailAddress(String emailAddress) {
        clearFieldAndEnter(emailField, emailAddress);
    }

    public void enterEmailAddressAndContinue(String emailAddress) {
        enterEmailAddress(emailAddress);
        findAndClickContinue();
    }
}

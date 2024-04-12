package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import java.net.MalformedURLException;

public class EnterYourEmailAddressToSignInPage extends BasePage {
    By emailField = By.id("email");

    public void enterEmailAddress(String emailAddress) throws MalformedURLException {
        clearFieldAndEnter(emailField, emailAddress);
    }

    public void enterEmailAddressAndContinue(String emailAddress) throws MalformedURLException {
        enterEmailAddress(emailAddress);
        findAndClickContinue();
    }

    public void enterEmailAddressAndContinueWelsh(String emailAddress)
            throws MalformedURLException {
        enterEmailAddress(emailAddress);
        findAndClickContinueWelsh();
    }
}

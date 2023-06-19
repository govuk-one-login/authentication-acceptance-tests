package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class EnterYourEmailAddressToSignInPage extends SignIn {
    By emailField = By.id("email");

    public void enterEmailAddress(String emailAddress) {
        clearFieldAndEnter(emailField, emailAddress);
    }

    public void enterEmailAddressAndContinue(String emailAddress) {
        enterEmailAddress(emailAddress);
        findAndClickContinue();
    }
}

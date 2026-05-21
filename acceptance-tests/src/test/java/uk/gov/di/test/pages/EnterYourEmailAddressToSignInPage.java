package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class EnterYourEmailAddressToSignInPage extends BasePage {
    By emailField = By.id("email");

    public void enterEmailAddress(String emailAddress) {
        clearFieldAndEnter(emailField, emailAddress);
    }

    public void enterEmailAddressAndContinue(String emailAddress) {
        enterEmailAddress(emailAddress);
        findAndClickContinue();
        dismissPasskeySignInPageIfPresent();
    }

    public void enterEmailAddressAndContinueWelsh(String emailAddress) {
        enterEmailAddress(emailAddress);
        findAndClickContinueWelsh();
        dismissPasskeySignInPageIfPresent();
    }

    private void dismissPasskeySignInPageIfPresent() {
        waitForReadyStateComplete();
        if (Driver.get().getTitle().contains("Sign in with your face, fingerprint or passcode")) {
            selectLinkByText("Sign in another way");
        }
    }

    @Override
    public void waitForPage() {
        waitForPageLoad("Enter your email address to sign in");
    }
}

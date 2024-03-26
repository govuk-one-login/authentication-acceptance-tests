package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class ReenterYourSignInDetailsToContinuePage extends BasePage {
    By emailField = By.id("email");
    public static final String REAUTH_SIGN_IN_PAGE_HEADER =
            "Re-enter your sign-in details to continue";

    public void enterEmailAddressAndContinue(String emailAddress) {
        waitForPage();
        clearFieldAndEnter(emailField, emailAddress);
        findAndClickContinue();
    }

    public void enterWrongEmailAddressNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            enterEmailAddressAndContinue("different_email_address@gmail.com");
            System.out.println("Wrong email entry count: " + (index + 1));
        }
    }

    public void waitForPage() {
        waitForPageLoad(REAUTH_SIGN_IN_PAGE_HEADER);
    }
}

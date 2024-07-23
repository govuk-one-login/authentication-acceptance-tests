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

    public void enterSameIncorrectEmailAddressesNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            int count = index + 1;
            enterEmailAddressAndContinue(System.getenv().get("TEST_USER_REAUTH_SMS_9"));
            System.out.println("Wrong email entry count: " + count);
        }
    }

    public void enterDifferentIncorrectEmailAddressesNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            int count = index + 1;
            enterEmailAddressAndContinue(
                    "different_incorrect_email_address+" + count + "@gmail.com");
            System.out.println("Wrong email entry count: " + count);
        }
    }

    public void waitForPage() {
        waitForPageLoad(REAUTH_SIGN_IN_PAGE_HEADER);
    }
}

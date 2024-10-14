package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.services.UserLifecycleService;

public class ReenterYourSignInDetailsToContinuePage extends BasePage {
    UserLifecycleService userLifecycleService = UserLifecycleService.getInstance();

    By emailField = By.id("email");
    public static final String REAUTH_SIGN_IN_PAGE_HEADER =
            "Enter your sign in details for GOV.UK One Login again";

    public void enterEmailAddressAndContinue(String emailAddress) {
        waitForPage();
        clearFieldAndEnter(emailField, emailAddress);
        findAndClickContinue();
    }

    public void enterSameIncorrectEmailAddressesNumberOfTimes(Integer numberOfTimes) {
        throw new RuntimeException("Need to implement new-style user flows for this");
        //        for (int index = 0; index < numberOfTimes; index++) {
        //            int count = index + 1;
        //            enterEmailAddressAndContinue(System.getenv().get("TEST_USER_REAUTH_SMS_9"));
        //            System.out.println("Wrong email entry count: " + count);
        //        }
    }

    public void enterDifferentIncorrectEmailAddressesNumberOfTimes(Integer numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            enterEmailAddressAndContinue(userLifecycleService.generateNewUniqueEmailAddress());
            System.out.println("Wrong email entry count: " + i);
        }
    }

    @Override
    public void waitForPage() {
        waitForPageLoad(REAUTH_SIGN_IN_PAGE_HEADER);
    }
}

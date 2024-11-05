package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.services.UserLifecycleService;
import uk.gov.di.test.step_definitions.World;

public class ReenterYourSignInDetailsToContinuePage extends BasePage {
    UserLifecycleService userLifecycleService = UserLifecycleService.getInstance();
    World world;

    public ReenterYourSignInDetailsToContinuePage(World world) {
        this.world = world;
    }

    By emailField = By.id("email");
    public static final String REAUTH_SIGN_IN_PAGE_HEADER =
            "Enter your sign in details for GOV.UK One Login again";

    public void enterEmailAddressAndContinue(String emailAddress) {
        waitForPage();
        clearFieldAndEnter(emailField, emailAddress);
        findAndClickContinue();
    }

    public void enterAnotherUsersEmailAddressNumberOfTime(int numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            enterEmailAddressAndContinue(world.getOtherUserProfile().getEmail());
            System.out.println("Wrong email entry count: " + i);
        }
    }

    public void enterSameIncorrectEmailAddressesNumberOfTimes(Integer numberOfTimes) {
        for (int i = 1; i <= numberOfTimes; i++) {
            enterEmailAddressAndContinue(world.getUserEmailAddress());
            System.out.println("Wrong email entry count: " + i);
        }
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

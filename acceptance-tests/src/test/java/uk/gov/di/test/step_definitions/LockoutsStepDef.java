package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.LockoutPage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.StubUserInfoPage;
import uk.gov.di.test.utils.Driver;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class LockoutsStepDef extends BasePage {
    public EnterYourPasswordPage enterYourPasswordPage;
    public CrossPageFlows crossPageFlows;
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public LockoutPage lockoutPage = new LockoutPage();
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();

    public LockoutsStepDef(World world) {
        this.enterYourPasswordPage = new EnterYourPasswordPage(world);
        this.crossPageFlows = new CrossPageFlows(world);
    }

    // Meta Given step for clarity in test cases
    @Given("the lockout has not yet expired")
    public void theLockoutHasNotYetExpired() {
        // intentionally empty
    }

    // Error Screen Parsing

    @Then("the non blocking {string} lockout screen is displayed")
    @Then("the {string} lockout screen is displayed")
    public void theLockoutScreenIsDisplayed(String titleText) {
        waitForPageLoad(titleText);
    }

    @And("the lockout reason is {string}")
    public void theLockoutReasonIs(String input) {
        assertTrue(lockoutPage.getLockoutScreenText().contains(String.format("because %s", input)));
    }

    @ParameterType("hour|hours|minute|minutes|second|seconds")
    public String timeUnit(String timeUnit) {
        return timeUnit;
    }

    @And("the lockout duration is {int} {timeUnit}")
    public void theLockoutDurationIs(int time, String timeUnit) {
        assertTrue(
                lockoutPage
                        .getLockoutScreenText()
                        .contains(String.format("%s %s", time, timeUnit)));
    }

    @Then("no lockout is triggered and the user remains on the {string} page")
    public void noLockoutIsTriggered(String pageTitle) {
        assertTrue(Objects.requireNonNull(Driver.get().getTitle()).contains(pageTitle));
    }

    // Utility

    @When("the user selects to get a new code")
    public void whenTheUserSelectsToGetANewCode() {
        selectLinkByText("get a new code");
    }

    @Then("the user is able to complete account creation")
    public void theUserIsAbleToCompleteAccountCreation() {
        waitForPageLoad("Get security code");
        crossPageFlows.completeAccountCreationAfterNewEmailCode();
    }

    // USER BLOCKED FOR TOO MANY INCORRECT PASSWORDS CAN RESET THEIR PASSWORD AND BLOCK IS LIFTED

    @Given("the user {string} is on the blocked page for entering too many incorrect passwords")
    public void theUserIsOnBlockedPageForEnteringTooManyIncorrectPasswords(String emailAddress) {
        throw new RuntimeException("Need to implement new-style user flows for this");
        //        rpStubPage.goToRpStub();
        //        rpStubPage.useDefaultOptionsAndContinue();
        //        setAnalyticsCookieTo(false);
        //        waitForPageLoad("Create your GOV.UK One Login or sign in");
        //        createOrSignInPage.clickSignInButton();
        //        waitForPageLoad("Enter your email address to sign in");
        //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
        //                System.getenv().get(emailAddress));
        //        waitForPageLoad("Enter your password");
        //        enterYourPasswordPage.enterIncorrectPasswordNumberOfTimes(6);
        //        waitForPageLoad("You entered the wrong password too many times");
    }

    @Then("the user resets their password")
    public void theUserResetsTheirPassword() {
        selectLinkByText("reset your password");
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("Reset your password");
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                NEW_VALID_PASSWORD, NEW_VALID_PASSWORD);
        stubUserInfoPage.waitForReturnToTheService();
        stubUserInfoPage.logoutOfAccount();
    }

    @And("the block is lifted and the user {string} can login")
    public void theBlockIsLiftedAndTheUserCanLogin(String emailAddress) {
        throw new RuntimeException("Need to implement new-style user flows for this");
    }
    
    @And("the {string} lockout Text is displayed")
    public void theLockoutTextIsDisplayed(String lockoutText) {
        assertEquals(lockoutText, getLockoutText());
    }

    @And("the {string} retry Text is displayed")
    public void theRetryTextIsDisplayed(String reTryTxt) {
        String actualFullText = (getRetryText());
        String delimiter = "\n";
        String actualText = Arrays.toString(actualFullText.split(delimiter));
        assertEquals(reTryTxt, actualText);
    }

    @And("the {string} lockout Text for SMS is displayed")
    public void theLockoutTextForSMSIsDisplayed(String lockoutText) {
        assertEquals(lockoutText, getLockoutTexts());
    }

    @And("the {string} message regarding what can you do is displayed")
    public void messageRegardingWhatCanYouDoIsDisplayed(String waitThenTryAgainText) {
        var actualText =
                lockoutPage.getStrategicAppWaitAndThenTryAgainTextMessage(waitThenTryAgainText);
        assertEquals(waitThenTryAgainText, actualText);
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.FinishCreatingYourAccountPage;
import uk.gov.di.test.pages.LockoutPage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.StubOrchestrationPage;
import uk.gov.di.test.pages.StubStartPage;
import uk.gov.di.test.pages.UserInformationPage;
import uk.gov.di.test.pages.YouHaveAGOVUKOneLoginPage;
import uk.gov.di.test.utils.Driver;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class LockoutsStepDef extends BasePage {
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    public ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    public FinishCreatingYourAccountPage finishCreatingYourAccountPage =
            new FinishCreatingYourAccountPage();
    public YouHaveAGOVUKOneLoginPage youHaveAGOVUKOneLoginPage = new YouHaveAGOVUKOneLoginPage();
    public CrossPageFlows crossPageFlows = new CrossPageFlows();
    public LockoutPage lockoutPage = new LockoutPage();
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public UserInformationPage userInformationPage = new UserInformationPage();
    public StubStartPage rpStubPage =
            USE_STUB_ORCH ? new StubOrchestrationPage() : new RpStubPage();

    @When(
            "the user {string} with a lockout for wrong email security codes reattempts to change their password during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many email security codes reattempts to change their password during the lockout period")
    public void theUserWithWrongEmailCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @When(
            "the user {string} with a lockout for wrong sms security codes reattempts to change their password during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security codes reattempts to change their password during the lockout period")
    public void theUserWithWrongSMSCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
    }

    @When(
            "the user {string} with a lockout for too many incorrect auth app codes reattempts to change their password during the lockout period")
    public void theUserWithWrongAuthAppCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
    }

    @When(
            "the user {string} with a lockout for requesting too many email security codes attempts to change the way they get security codes during the lockout period")
    @When(
            "the user {string} with a lockout for too many incorrect email security codes attempts to change the way they get security codes during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManyEmailSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
        checkYourPhonePage.changeHowToGetSecurityCodes();
    }

    @When(
            "the user {string} with a lockout for requesting too many sms security codes attempts to change the way they get security codes during the lockout period")
    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to change the way they get security codes during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManySmsSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
        checkYourPhonePage.changeHowToGetSecurityCodes();
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterValidUkPhoneNumberAndContinue();
    }

    @When(
            "the user {string} with a lockout for too many incorrect passwords attempts to sign in during the lockout period")
    public void theUserWithALockoutForTooManyIncorrectPasswordsAttemptsToSignInDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
    }

    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to sign in during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security code resends attempts to sign in during the lockout period")
    public void theUserWithALockoutForTooManyIncorrectSmsCodesAttemptsToSignInDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
    }

    @When(
            "the user {string} with a lockout for too many incorrect auth app codes reattempts to sign in during the lockout period")
    public void
            theUserWithALockoutForTooManyIncorrectAuthAppCodesAttemptsToSignInDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
    }

    @When(
            "the user {string} with a lockout for requesting too many email security codes attempts to create account during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManyEmailSecurityCodesAttemptsToCreateAccountDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressPage.enterEmailAddressAndContinue(System.getenv().get(emailAddress));
    }

    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to create account during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security codes attempts to create account during the lockout period")
    public void
            theUserWithALockoutForTooManyIncorrectSmsCodesAttemptsToCreateAccountDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressPage.enterEmailAddressAndContinue(System.getenv().get(emailAddress));
        youHaveAGOVUKOneLoginPage.enterCorrectPasswordAndContinue();
        finishCreatingYourAccountPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterValidUkPhoneNumberAndContinue();
    }

    @Then("the 2hr You entered the wrong security code too many times screen is displayed")
    public void theLockoutScreenForWrongSecurityCodesIsDisplayed() {
        waitForPageLoad("You entered the wrong security code too many times");
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    // YOU ENTERED WRONG SECURITY CODES
    @Then("the 15min You entered the wrong security code too many times screen is displayed")
    public void the15minLockoutScreenForWrongSecurityCodesIsDisplayed() {
        waitForPageLoad("You entered the wrong security code too many times");
        assertTrue(lockoutPage.getLockoutScreenText().contains("15 minutes"));
    }

    @Then("the non blocking You entered the wrong security code too many times page is displayed")
    public void theNonBlockingYouEnteredTheWrongSecurityCodeTooManyTimesPageIsDisplayed() {
        waitForPageLoad("You entered the wrong security code too many times");
    }

    // YOU CANNOT GET A NEW SECURITY CODE
    @Then("the 15min You cannot get a new security code at the moment screen is displayed")
    public void the15minCannotSignInLockoutScreenIsDisplayed() {
        waitForPageLoad("You cannot get a new security code at the moment");
        assertTrue(lockoutPage.getLockoutScreenText().contains("wrong security code"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("15 minutes"));
    }

    // YOU ASKED TO RESEND SECURITY CODES
    @Then("the 2hr You asked to resend the security code too many times screen is displayed")
    public void theLockoutScreenForRequestingTooManySecurityCodesIsDisplayed() {
        waitForPageLoad("You asked to resend the security code too many times");
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    // CANNOT CREATE
    @Then(
            "the 2hr You cannot create a GOV.UK One Login at the moment screen for requesting too many security code resends is displayed")
    public void
            theCannotCreateAccountLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
        waitForPageLoad("You cannot create a GOV.UK One Login at the moment");
        assertTrue(lockoutPage.getLockoutScreenText().contains("resend the security code"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    // CANNOT SIGN IN
    @Then("the 2hr You cannot sign in at the moment screen for wrong password is displayed")
    public void the2hrCannotSignInScreenForTooManyIncorrectPasswordsIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(lockoutPage.getLockoutScreenText().contains("wrong password"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    @Then(
            "the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed")
    public void theCannotSignInLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(lockoutPage.getLockoutScreenText().contains("resend the security code"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    @Then("the 2hr You cannot sign in at the moment screen for wrong security codes is displayed")
    public void theCannotSignInLockoutScreenIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(lockoutPage.getLockoutScreenText().contains("wrong security code"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
    }

    @Then("no lockout is triggered and the user remains on the {string} page")
    public void noLockoutIsTriggered(String pageTitle) {
        assertTrue(Driver.get().getTitle().contains(pageTitle));
    }

    @Then("the 2hr You entered the wrong password too many times screen is displayed")
    public void the2hrLockoutScreenForTooManyIncorrectPasswordsDuringSignInIsDisplayed() {
        waitForPageLoad("You entered the wrong password too many times");
        assertTrue(lockoutPage.getLockoutScreenText().contains("2 hours"));
        assertTrue(lockoutPage.getLockoutScreenText().contains("wrong password"));
    }

    @When("the user selects to get a new code")
    public void whenTheUserSelectsToGetANewCode() {
        selectLinkByText("get a new code");
    }

    @Then("the user is able to complete account creation")
    public void theUserIsAbleToCompleteAccountCreation() {
        waitForPageLoad("Get security code");
        crossPageFlows.completeAccountCreationAfterNewEmailCode();
    }

    @Given("the user {string} is on the blocked page for entering too many incorrect passwords")
    public void theUserIsOnBlockedPageForEnteringTooManyIncorrectPasswords(String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.enterIncorrectPasswordNumberOfTimes(6);
        waitForPageLoad("You entered the wrong password too many times");
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
        rpStubPage.waitForReturnToTheService();
        userInformationPage.logoutOfAccount();
    }

    @And("the block is lifted and the user {string} can login")
    public void theBlockIsLiftedAndTheUserCanLogin(String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.enterNewPasswordAndContinue();
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        rpStubPage.waitForReturnToTheService();
        userInformationPage.logoutOfAccount();
    }
}

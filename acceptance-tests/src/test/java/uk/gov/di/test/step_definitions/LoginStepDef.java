package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.pages.TermsAndConditionsPage;
import uk.gov.di.test.pages.YouAskedToResendTheSecurityCodeTooManyTimesPage;
import uk.gov.di.test.pages.YouveChangedHowYouGetSecurityCodesPage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.INVALID_EMAIL;
import static uk.gov.di.test.utils.Constants.INVALID_PASSWORD;
import static uk.gov.di.test.utils.Constants.MISMATCHING_PASSWORD_1;
import static uk.gov.di.test.utils.Constants.MISMATCHING_PASSWORD_2;
import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;
import static uk.gov.di.test.utils.Constants.TOP_100K_PASSWORD;

public class LoginStepDef extends BasePage {
    private String authAppSecretKey;
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public YouveChangedHowYouGetSecurityCodesPage youveChangedHowYouGetSecurityCodes =
            new YouveChangedHowYouGetSecurityCodesPage();
    public TermsAndConditionsPage termsAndConditionsPage = new TermsAndConditionsPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public YouAskedToResendTheSecurityCodeTooManyTimesPage
            youAskedToResendTheSecurityCodeTooManyTimesPage =
                    new YouAskedToResendTheSecurityCodeTooManyTimesPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    public SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage =
            new SetUpAnAuthenticatorAppPage();
    public CrossPageFlows crossPageFlows = new CrossPageFlows();

    public EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();
    public ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();

    RpStubPage rpStubPage = new RpStubPage();

    @When("the user enters their password which is on the top 100k password list")
    public void theUserEntersTheirPasswordWhichIsOnTheTop100kPasswordList() {
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TOP_100K_PASSWORD"));
    }

    @When("the user clicks the forgotten password link")
    public void theUserClicksTheForgottenPasswordLink() {
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @When("the user selects sign in")
    public void theUserSelectsSignIn() throws InterruptedException {
        Thread.sleep(2000);
        createOrSignInPage.clickSignInButton();
    }

    @And("user enters {string} email address")
    public void userEntersEmailAddress(String email) {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(System.getenv().get(email));
    }

    @When("user enters invalid email address")
    public void userEntersInvalidEmailAddress() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(INVALID_EMAIL);
    }

    @When("the user enters their password")
    public void theUserEntersTheirPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    }

    @When("the user enters their new password")
    public void theUserEntersTheirNewPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(NEW_VALID_PASSWORD);
    }

    @When("the user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        checkYourPhonePage.enterPhoneCodeAndContinue(System.getenv().get("TEST_USER_PHONE_CODE"));
    }

    @When("the user requests the phone otp code {int} times")
    @When("the user requests the phone otp code a further {int} times")
    public void theUserRequestsThePhoneOtpCodeTimes(Integer timesCodeIncorrect) {
        crossPageFlows.requestPhoneSecurityCodeResendNumberOfTimes(timesCodeIncorrect);
    }

    @When("the user clicks the get a new code link")
    public void theUserClicksTheGetANewCodeLink() {
        youAskedToResendTheSecurityCodeTooManyTimesPage.clickGetANewCodeLink();
    }

    @Then("the user is taken to the Identity Provider Welsh Login Page")
    public void theUserIsTakenToTheIdentityProviderWelshLoginPage() {
        assertEquals("Creu GOV.UK One Login neu fewngofnodi - GOV.UK One Login", driver.getTitle());
    }

    @Then("the user is taken to the Welsh enter your email page")
    public void theUserIsTakenToTheWelshEnterYourEmailPage() {
        assertEquals(
                "Rhowch eich cyfeiriad e-bost i fewngofnodi i’ch GOV.UK One Login - GOV.UK One Login",
                driver.getTitle());
        Assertions.assertNotEquals("Continue", enterYourEmailAddressPage.continueButtonText());
        Assertions.assertNotEquals("Back", enterYourEmailAddressPage.backButtonText());
    }

    @Then("the user is prompted for their password in Welsh")
    public void theUserIsPromptedForTheirPasswordInWelsh() {
        assertEquals("Rhowch eich cyfrinair - GOV.UK One Login", driver.getTitle());
    }

    @When("user enters {string} email address in Welsh")
    public void userEntersEmailAddressInWelsh(String email) {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinueWelsh(
                System.getenv().get(email));
    }

    @When("the user enters valid new password and correctly retypes it")
    public void theUserEntersValidNewPasswordAndCorrectlyRetypesIt() {
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                NEW_VALID_PASSWORD, NEW_VALID_PASSWORD);
    }

    @When("the user resets their password to be the same as their current password")
    public void theUserResetsTheirPasswordToBeTheSameAsTheirCurrentPassword() {
        String newPassword = System.getenv().get("TEST_USER_PASSWORD");
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(newPassword, newPassword);
    }

    @When("the user resets their password to an invalid one")
    public void theUserResetsTheirPasswordToAnInvalidOne() {
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                INVALID_PASSWORD, INVALID_PASSWORD);
    }

    @When("the user resets their password to one that is on the list of top 100k passwords")
    public void theUserResetsTheirPasswordToOneThatIsOnTheListOfTop100kPasswords() {
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                TOP_100K_PASSWORD, TOP_100K_PASSWORD);
    }

    @When("the user resets their password but enters mismatching new passwords")
    public void theUserResetsTheirPasswordButEntersMismatchingNewPasswords() {
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                MISMATCHING_PASSWORD_1, MISMATCHING_PASSWORD_2);
    }

    @And("confirmation that the user will get security codes via {string} is displayed")
    public void confirmationThatTheUserWillGetSecurityCodesViaIsDisplayed(
            String authenticationType) {

        switch (authenticationType.toLowerCase()) {
            case "text message":
                // Check the last four digits of the phone number appear in the page message
                String phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
                String lastFourDigitsOfPhone = phoneNumber.substring(phoneNumber.length() - 4);
                assertTrue(
                        youveChangedHowYouGetSecurityCodes
                                .getSecurityCodeMessageText()
                                .contains(lastFourDigitsOfPhone));
                break;
            case "auth app":
                assertTrue(
                        youveChangedHowYouGetSecurityCodes
                                .getSecurityCodeMessageText()
                                .contains("authenticator app"));
                break;
        }

        findAndClickContinue();
    }

    @When("the user adds the secret key on the screen to their auth app")
    public void theNewUserAddTheSecretKeyOnTheScreenToTheirAuthApp() {
        authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
        setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
        authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
        assertTrue(setUpAnAuthenticatorAppPage.getSecretFieldText().length() == 32);
    }

    @And("the user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        if (authAppSecretKey == null) {
            authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
        }
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
    }

    @And("the user requests the email OTP code be sent again a further {int} times")
    public void theUserRequestsTheEmailOTPCodeBeSentAgainAFurtherIntTimes(Integer requestCount) {
        crossPageFlows.requestEmailOTPCodeResendNumberOfTimes(requestCount);
    }

    @When("the user switches to {string} language")
    public void theUserSwitchesLanguage(String language) {
        createOrSignInPage.switchLanguageTo(language);
    }

    @When("the user enters an incorrect email OTP {int} times")
    public void theUserEntersAnIncorrectEmailOTPIntTimes(Integer attemptCount) {
        checkYourEmailPage.enterIncorrectEmailOTPNumberOfTimes(attemptCount);
    }

    @When("the user agrees to the updated terms and conditions")
    public void theUserAgreesToTheUpdatedTermsAndConditions() {
        termsAndConditionsPage.pressAgreeAndContinueButton();
    }

    @When("the user enters incorrect password")
    public void theUserEntersIncorrectPassword() {
        enterYourPasswordPage.enterIncorrectPasswordNumberOfTimes(1);
    }

    @When("the user enters an incorrect password a further {int} times")
    @When("the user enters an incorrect password {int} times")
    public void theUserEntersAnIncorrectPasswordAFurtherXTimes(Integer attemptCount) {
        enterYourPasswordPage.enterIncorrectPasswordNumberOfTimes(attemptCount);
    }

    @And("the user enters an incorrect phone security code {int} times")
    public void theUserEntersAnIncorrectPhoneSecurityCode(Integer attemptCount) {
        checkYourPhonePage.enterIncorrectPhoneSecurityCodeNumberOfTimes(attemptCount);
    }

    @When("the user enters an incorrect phone security code a further {int} times")
    public void theUserEntersAnIncorrectPhoneSecurityCodeAFurtherXTimes(Integer attemptCount) {
        checkYourPhonePage.enterIncorrectPhoneSecurityCodeNumberOfTimes(attemptCount);
    }

    @And("the user enters an incorrect auth app security code {int} times")
    public void theUserEntersAnIncorrectAuthAppSecurityCode(Integer attemptCount) {
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                .enterIncorrectAuthAppCodeNumberOfTimes(attemptCount);
    }

    @When("the user enters an incorrect auth app security code a further {int} times")
    public void theUserEntersIncorrectAuthAppCodeAFurtherXTimes(Integer attemptCount) {
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                .enterIncorrectAuthAppCodeNumberOfTimes(attemptCount);
    }

    //    @When(
    //            "the user {string} with a lockout for wrong email security codes reattempts to
    // change their password during the lockout period")
    //    @When(
    //            "the user {string} with a lockout for requesting too many email security codes
    // reattempts to change their password during the lockout period")
    //    public void
    // theUserWithWrongEmailCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
    //            String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //        enterYourPasswordPage.clickForgottenPasswordLink();
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for wrong sms security codes reattempts to
    // change their password during the lockout period")
    //    @When(
    //            "the user {string} with a lockout for requesting too many sms security codes
    // reattempts to change their password during the lockout period")
    //    public void
    // theUserWithWrongSMSCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
    //            String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //        enterYourPasswordPage.clickForgottenPasswordLink();
    //
    // checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for too many incorrect auth app codes reattempts
    // to change their password during the lockout period")
    //    public void
    // theUserWithWrongAuthAppCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
    //            String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //        enterYourPasswordPage.clickForgottenPasswordLink();
    //
    // checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    //        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
    //                .enterCorrectAuthAppCodeAndContinue(
    //                        System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET"));
    //    }
    //
    //    @Then("the 2hr lockout screen for too many wrong security codes is displayed")
    //    public void theLockoutScreenForWrongSecurityCodesIsDisplayed() {
    //        waitForPageLoad("You entered the wrong security code too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("You will not be able to sign in for 2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("the 15min lockout screen for too many wrong security codes is displayed")
    //    public void the15minLockoutScreenForWrongSecurityCodesIsDisplayed() {
    //        waitForPageLoad("You entered the wrong security code too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("You need to wait 15 minutes"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("the 15min cannot sign in lockout screen for wrong security codes is displayed")
    //    public void the15minCannotSignInLockoutScreenIsDisplayed() {
    //        waitForPageLoad("You cannot get a new security code at the moment");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(
    //                text.contains(
    //                        "This is because you entered the wrong security code too many
    // times"));
    //        assertTrue(text.contains("15 minutes"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("the 2hr cannot sign in lockout screen for wrong security codes is displayed")
    //    public void theCannotSignInLockoutScreenIsDisplayed() {
    //        waitForPageLoad("You cannot sign in at the moment");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(
    //                text.contains(
    //                        "This is because you entered the wrong security code too many
    // times"));
    //        assertTrue(text.contains("2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("the 2hr lockout screen for requesting too many security code resends is displayed")
    //    public void theLockoutScreenForRequestingTooManySecurityCodesIsDisplayed() {
    //        waitForPageLoad("You asked to resend the security code too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("You will not be able to continue for 2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then(
    //            "the 2hr lockout screen for requesting too many security code resends during sign
    // in is displayed")
    //    public void theLockoutScreenForRequestingTooManySecurityCodesDuringSignInIsDisplayed() {
    //        waitForPageLoad("You asked to resend the security code too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("You will not be able to sign in for 2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then(
    //            "the 2hr cannot sign in lockout screen for requesting too many security code
    // resends is displayed")
    //    public void
    // theCannotSignInLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
    //        waitForPageLoad("You cannot sign in at the moment");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(
    //                text.contains(
    //                        "This is because you asked to resend the security code too many
    // times"));
    //        assertTrue(text.contains("2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for requesting too many email security codes
    // attempts to change the way they get security codes during the lockout period")
    //    @When(
    //            "the user {string} with a lockout for too many incorrect email security codes
    // attempts to change the way they get security codes during the lockout period")
    //    public void
    //
    // theUserWithALockoutForRequestingTooManyEmailSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
    //                    String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //
    // enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    //        checkYourPhonePage.clickProblemsWithTheCodeLink();
    //        selectLinkByText("change how you get security codes");
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for requesting too many sms security codes
    // attempts to change the way they get security codes during the lockout period")
    //    @When(
    //            "the user {string} with a lockout for too many incorrect sms security codes
    // attempts to change the way they get security codes during the lockout period")
    //    public void
    //
    // theUserWithALockoutForRequestingTooManySmsSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
    //                    String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //
    // enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    //        checkYourPhonePage.clickProblemsWithTheCodeLink();
    //        selectLinkByText("change how you get security codes");
    //
    // checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    //        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
    //        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("07803507860");
    //    }
    //
    //    @Then(
    //            "the 2hr cannot create a GOV.UK One Login lockout screen for requesting too many
    // security code resends is displayed")
    //    public void
    //
    // theCannotCreateAccountLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
    //        waitForPageLoad("You cannot create a GOV.UK One Login at the moment");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(
    //                text.contains(
    //                        "This is because you asked to resend the security code too many
    // times"));
    //        assertTrue(text.contains("2 hours"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("no lockout is triggered and the user remains on the {string} page")
    //    public void noLockoutIsTriggered(String pageTitle) {
    //        assertTrue(driver.getTitle().contains(pageTitle));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @Then("the 2hr lockout screen for too many incorrect passwords is displayed")
    //    public void the2hrLockoutScreenForTooManyIncorrectPasswordsIsDisplayed() {
    //        waitForPageLoad("You entered the wrong password too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("2 hours"));
    //        assertTrue(text.contains("wrong password"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for too many incorrect passwords attempts to
    // sign in during the lockout period")
    //    public void
    // theUserWithALockoutForTooManyIncorrectPasswordsAttemptsToSignInDuringLockoutPeriod(
    //            String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //    }
    //
    //    @Then("the 2hr cannot sign in lockout screen for wrong password is displayed")
    //    public void the2hrCannotSignInScreenForTooManyIncorrectPasswordsIsDisplayed() {
    //        waitForPageLoad("You cannot sign in at the moment");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("2 hours"));
    //        assertTrue(text.contains("wrong password"));
    //        takeScreenshot(scenario);
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for too many incorrect sms security codes
    // attempts to sign in during the lockout period")
    //    @When(
    //            "the user {string} with a lockout for requesting too many sms security code
    // resends attempts to sign in during the lockout period")
    //    public void
    // theUserWithALockoutForTooManyIncorrectSmsCodesAttemptsToSignInDuringLockoutPeriod(
    //            String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //
    // enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    //    }
    //
    //    @When(
    //            "the user {string} with a lockout for too many incorrect auth app codes reattempts
    // to sign in during the lockout period")
    //    public void
    //
    // theUserWithALockoutForTooManyIncorrectAuthAppCodesAttemptsToSignInDuringLockoutPeriod(
    //                    String emailAddress) {
    //        rpStubPage.goToRpStub();
    //        rpStubPage.selectRpOptionsByIdAndContinue("default");
    //        setAnalyticsCookieTo(false);
    //        createOrSignInPage.clickSignInButton();
    //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
    //                System.getenv().get(emailAddress));
    //
    // enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    //    }
}

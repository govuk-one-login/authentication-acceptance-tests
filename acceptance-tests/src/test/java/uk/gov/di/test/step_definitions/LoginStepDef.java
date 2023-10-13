package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.CreateOrBasePagePage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToBasePagePage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.GetSecurityCodePage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.pages.TermsAndConditionsPage;
import uk.gov.di.test.pages.YouAskedToResendTheSecurityCodeTooManyTimesPage;
import uk.gov.di.test.pages.YouveChangedHowYouGetSecurityCodesPage;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_CODE_UPLIFT;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_PASSWORD;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.RESEND_SECURITY_CODE;
import static uk.gov.di.test.utils.Constants.*;

public class LoginStepDef extends BasePage {

    private String authAppSecretKey;
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public YouveChangedHowYouGetSecurityCodesPage youveChangedHowYouGetSecurityCodes =
            new YouveChangedHowYouGetSecurityCodesPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    public TermsAndConditionsPage termsAndConditionsPage = new TermsAndConditionsPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public GetSecurityCodePage getSecurityCodePage = new GetSecurityCodePage();
    public EnterYourEmailAddressToBasePagePage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToBasePagePage();
    public CreateOrBasePagePage createOrSignInPage = new CreateOrBasePagePage();
    public YouAskedToResendTheSecurityCodeTooManyTimesPage
            youAskedToResendTheSecurityCodeTooManyTimesPage =
                    new YouAskedToResendTheSecurityCodeTooManyTimesPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    public SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage =
            new SetUpAnAuthenticatorAppPage();

    @When("the user enters their password which is on the top 100k password list")
    public void theUserEntersTheirPasswordWhichIsOnTheTop100kPasswordList() {
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TOP_100K_PASSWORD"));
    }

    @When("the user clicks the forgotten password link")
    public void theUserClicksTheForgottenPasswordLink() {
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @When("the existing user selects sign in")
    public void theExistingUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @Then("the existing user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @And("user enters {string} email address")
    public void userEntersEmailAddress(String email) {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(System.getenv().get(email));
    }

    @When("user enters invalid email address")
    public void userEntersInvalidEmailAddress() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(INVALID_EMAIL);
    }

    @Then("the existing user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing auth app user enters their password")
    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    }

    @When("the existing user enters their new password")
    public void theExistingUserEntersTheirNewPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(NEW_VALID_PASSWORD);
    }

    @Then("the existing user is taken to the enter code uplifted page")
    public void theExistingUserIsTakenToTheEnterCodeUpliftedPage() {
        waitForPageLoadThenValidate(ENTER_CODE_UPLIFT);
    }

    @When("the new user enters the six digit security code from their phone")
    @When("the existing user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        checkYourPhonePage.enterPhoneCodeAndContinue(System.getenv().get("TEST_USER_PHONE_CODE"));
    }

    @Then("the existing user is returned to the service")
    public void theExistingUserIsReturnedToTheService() {}

    @When("the existing user requests the phone otp code {int} times")
    public void theExistingUserRequestsThePhoneOtpCodeTimes(int timesCodeIncorrect) {
        for (int i = 0; i < timesCodeIncorrect; i++) {
            checkYourPhonePage.clickProblemsWithTheCodeLink();
            checkYourPhonePage.clickSendTheCodeAgainLink();
            waitForPageLoadThenValidate(RESEND_SECURITY_CODE);
            getSecurityCodePage.pressGetSecurityCodeButton();
        }
    }

    @When("the existing user clicks the get a new code link")
    public void theExistingUserClicksTheGetANewCodeLink() {
        youAskedToResendTheSecurityCodeTooManyTimesPage.clickGetANewCodeLink();
    }

    @Then("the existing user is taken to the Identity Provider Welsh Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderWelshLoginPage() {
        assertEquals("Creu GOV.UK One Login neu fewngofnodi - GOV.UK One Login", driver.getTitle());
    }

    @Then("the existing user is taken to the Welsh enter your email page")
    public void theExistingUserIsTakenToTheWelshEnterYourEmailPage() {
        assertEquals(
                "Rhowch eich cyfeiriad e-bost i fewngofnodi i’ch GOV.UK One Login - GOV.UK One Login",
                driver.getTitle());
        Assertions.assertNotEquals("Continue", enterYourEmailAddressPage.continueButtonText());
        Assertions.assertNotEquals("Back", enterYourEmailAddressPage.backButtonText());
    }

    @Then("the existing user is prompted for their password in Welsh")
    public void theExistingUserIsPromptedForTheirPasswordInWelsh() {
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

    @When("the existing user adds the secret key on the screen to their auth app")
    public void theNewUserAddTheSecretKeyOnTheScreenToTheirAuthApp() {
        authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
        setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
        authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
        assertTrue(setUpAnAuthenticatorAppPage.getSecretFieldText().length() == 32);
    }

    @And("the existing user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        if (authAppSecretKey == null) {
            authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
        }
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
    }

    @Then("the link {string} is not available")
    public void theLinkIsNotAvailable(String linkText) {
        assertFalse(isLinkTextDisplayed(linkText));
    }

    @And("the user requests the email OTP code be sent again a further {int} times")
    public void theUserRequestsTheEmailOTPCodeBeSentAgainAFurtherIntTimes(Integer requestCount) {
        for (int index = 0; index < requestCount; index++) {
            checkYourEmailPage.waitForPage();
            checkYourEmailPage.requestResendOfEmailOTPCode();
            getSecurityCodePage.waitForPage();
            getSecurityCodePage.pressGetSecurityCodeButton();
            System.out.println(
                    "Code request count: "
                            + (index + 1)
                            + " ("
                            + (index + 2)
                            + " including code sent on initial entry to the Check Your Email page)");
        }
    }

    @When("the user selects {string} link")
    public void theUserSelectsProblemsWithTheCode(String text) {
        selectLinkByText(text);
    }

    @When("the user enters their mobile phone number")
    public void theUserEntersTheirMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
    }

    @And("the existing user selects create an account")
    public void theExistingUserSelectsCreateAnAccount() {
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
    }

    @When("the existing user switches to {string} language")
    public void theExistingUserSwitchesLanguage(String language) {
        createOrSignInPage.switchLanguageTo(language);
    }

    @When("the user enters an incorrect email OTP {int} times")
    public void theUserEntersAnIncorrectEmailOTPIntTimes(Integer attemptCount)
            throws InterruptedException {
        for (int index = 0; index < attemptCount; index++) {
            checkYourEmailPage.waitForPage();
            checkYourEmailPage.enterEmailCodeAndContinue(INCORRECT_EMAIL_OTP_CODE);
            System.out.println("Incorrect code entry count: " + (index + 1));
            Thread.sleep(2000);
        }
    }

    @When("the user selects link {string}")
    public void theUserSelectsLink(String linkText) {
        selectLinkByText(linkText);
    }

    @When("the user agrees to the updated terms and conditions")
    public void theUserAgreesToTheUpdatedTermsAndConditions() {
        termsAndConditionsPage.pressAgreeAndContinueButton();
    }
}

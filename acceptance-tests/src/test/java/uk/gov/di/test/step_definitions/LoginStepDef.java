package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import uk.gov.di.test.controllers.UserLifecycleController;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.ReenterYourSignInDetailsToContinuePage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.pages.TermsAndConditionsPage;
import uk.gov.di.test.pages.YouAskedToResendTheSecurityCodeTooManyTimesPage;
import uk.gov.di.test.pages.YouveChangedHowYouGetSecurityCodesPage;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.PasswordGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.*;

public class LoginStepDef extends BasePage {
    private final World world;
    private String authAppSecretKey;

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();

    public EnterYourPasswordPage enterYourPasswordPage;
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public CrossPageFlows crossPageFlows;
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public YouveChangedHowYouGetSecurityCodesPage youveChangedHowYouGetSecurityCodes =
            new YouveChangedHowYouGetSecurityCodesPage();
    public TermsAndConditionsPage termsAndConditionsPage = new TermsAndConditionsPage();
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
    public ReenterYourSignInDetailsToContinuePage reenterYourSignInDetailsToContinuePage =
            new ReenterYourSignInDetailsToContinuePage();
    public EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();

    public LoginStepDef(World world) {
        this.world = world;
        this.enterYourPasswordPage = new EnterYourPasswordPage(world);
        this.crossPageFlows = new CrossPageFlows(world);
    }

    @When("the user enters their password which is on the top 100k password list")
    public void theUserEntersTheirPasswordWhichIsOnTheTop100kPasswordList() {
        enterYourPasswordPage.enterPasswordAndContinue(TOP_100K_PASSWORD);
    }

    @When("the user clicks the forgotten password link")
    public void theUserClicksTheForgottenPasswordLink() {
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @When("the user selects sign in")
    public void theUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @And("the user enters their email address")
    @And("the user enters the email address")
    public void theUserEntersTheirEmailAddress() {
        enterYourEmailAddressPage.enterEmailAddressAndContinue(world.getUserEmailAddress());
    }

    @And("user enters the same email address {string} for reauth as they used for login")
    public void userEntersSameEmailAddressForReauth(String email) {
        throw new RuntimeException("Need to implement new-style user flows for this");
        //        reenterYourSignInDetailsToContinuePage.enterEmailAddressAndContinue(
        //                System.getenv().get(email));
    }

    @When("the user enters an invalid email address")
    @When("user enters invalid email address")
    public void userEntersInvalidEmailAddress() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(INVALID_EMAIL);
    }

    @When("the user enters the correct password")
    @When("the user enters their password")
    public void theUserEntersTheirPassword() {
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
    }

    @When("the user enters their new password")
    public void theUserEntersTheirNewPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(NEW_VALID_PASSWORD);
    }

    @When("the user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
    }

    @When("the user requests the phone otp code {int} times")
    @When("the user requests the phone otp code a further {int} times")
    public void theUserRequestsThePhoneOtpCodeTimes(Integer timesCodeIncorrect) {
        crossPageFlows.requestPhoneSecurityCodeResendNumberOfTimes(timesCodeIncorrect, false);
    }

    @When("the user requests the phone otp code a further {int} times during reauth")
    public void theUserRequestsThePhoneOtpCodeTimesAtReauth(Integer timesCodeIncorrect) {
        crossPageFlows.requestPhoneSecurityCodeResendNumberOfTimes(timesCodeIncorrect, true);
    }

    @When("the user clicks the get a new code link")
    public void theUserClicksTheGetANewCodeLink() {
        youAskedToResendTheSecurityCodeTooManyTimesPage.clickGetANewCodeLink();
    }

    @Then("the user is taken to the Identity Provider Welsh Login Page")
    public void theUserIsTakenToTheIdentityProviderWelshLoginPage() {
        assertEquals(
                "Creu eich GOV.UK One Login neu fewngofnodi - GOV.UK One Login",
                Driver.get().getTitle());
    }

    @Then("the user is taken to the Welsh enter your email page")
    public void theUserIsTakenToTheWelshEnterYourEmailPage() {
        assertEquals(
                "Rhowch eich cyfeiriad e-bost i fewngofnodi i’ch GOV.UK One Login - GOV.UK One Login",
                Driver.get().getTitle());
        Assertions.assertNotEquals("Continue", enterYourEmailAddressPage.continueButtonText());
        Assertions.assertNotEquals("Back", enterYourEmailAddressPage.backButtonText());
    }

    @Then("the user is prompted for their password in Welsh")
    public void theUserIsPromptedForTheirPasswordInWelsh() {
        assertEquals("Rhowch eich cyfrinair - GOV.UK One Login", Driver.get().getTitle());
    }

    @When("the user enters their email address in Welsh")
    public void theUserEntersTheirEmailInWelsh() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinueWelsh(
                world.getUserEmailAddress());
    }

    @When("the user enters valid new password and correctly retypes it")
    public void theUserEntersValidNewPasswordAndCorrectlyRetypesIt() {
        String newPassword = UserLifecycleController.generateValidPassword();
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(newPassword, newPassword);
        world.setUserPassword(newPassword);
    }

    @When("the user resets their password to be the same as their current password")
    public void theUserResetsTheirPasswordToBeTheSameAsTheirCurrentPassword() {
        String currentPassword = world.getUserPassword();
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                currentPassword, currentPassword);
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
        String[] passwords = passwordGenerator.generatePasswords(2);
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(passwords[0], passwords[1]);
    }

    @And("confirmation that the user will get security codes via {string} is displayed")
    public void confirmationThatTheUserWillGetSecurityCodesViaIsDisplayed(
            String authenticationType) {
        String method = authenticationType.toLowerCase();
        switch (method) {
            case "text message":
                // Check the last four digits of the phone number appear in the page message
                String phoneNumber = world.getUserPhoneNumber();
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
            default:
                throw new RuntimeException("Invalid method type: " + method);
        }

        findAndClickContinue();
    }

    @When("the user adds the secret key on the screen to their auth app")
    public void theNewUserAddTheSecretKeyOnTheScreenToTheirAuthApp() {
        authAppSecretKey =
                secretsManagerController.getSecretValue("test_user_pw_reset_auth_app_secret");
        setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
        authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
        assertEquals(32, setUpAnAuthenticatorAppPage.getSecretFieldText().length());
    }

    @And("the user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        if (authAppSecretKey == null) {
            authAppSecretKey =
                    secretsManagerController.getSecretValue("test_user_pw_reset_auth_app_secret");
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

    @When("the user enters an incorrect email address at reauth that is known to One Login")
    public void theUserEntersADifferentEmailAddressThanTheyLoggedInWith() {
        reenterYourSignInDetailsToContinuePage.enterSameIncorrectEmailAddressesNumberOfTimes(1);
    }

    @When("the user enters the same incorrect email address for reauth a further {int} times")
    public void theUserEntersDifferentEmailAddressXTimes(Integer attemptCount) {
        reenterYourSignInDetailsToContinuePage.enterSameIncorrectEmailAddressesNumberOfTimes(
                attemptCount);
    }

    @When(
            "the user enters {int} different email addresses at reauth that are not known to One Login")
    public void theUserEntersNDifferentEmailAddressesForReauth(Integer attemptCount) {
        reenterYourSignInDetailsToContinuePage.enterDifferentIncorrectEmailAddressesNumberOfTimes(
                attemptCount);
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

    @When("the user enters a blank password")
    public void theUserEntersABlankPassword() {
        enterYourPasswordPage.enterPasswordAndContinue("");
    }

    @And("the user enters an incorrect phone security code")
    public void theUserEntersAnIncorrectPhoneSecurityCode() {
        checkYourPhonePage.enterIncorrectPhoneSecurityCodeNumberOfTimes(1);
    }

    @When("the user enters an incorrect phone security code {int} times")
    public void theUserEntersAnIncorrectPhoneSecurityCode(Integer attemptCount) {
        checkYourPhonePage.enterIncorrectPhoneSecurityCodeNumberOfTimes(attemptCount);
    }

    @When("the user enters an incorrect phone security code a further {int} times")
    public void theUserEntersAnIncorrectPhoneSecurityCodeAFurtherXTimes(Integer attemptCount) {
        checkYourPhonePage.enterIncorrectPhoneSecurityCodeNumberOfTimes(attemptCount);
    }

    @And("the user enters an incorrect auth app security code")
    public void theUserEntersAnIncorrectAuthAppSecurityCode() {
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                .enterIncorrectAuthAppCodeNumberOfTimes(1);
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

    @Then("the sms user changes how they get security codes")
    public void theSmsUserCanChangeHowTheyGetSecurityCodes() {
        crossPageFlows.smsUserChangeHowGetSecurityCodesToAuthApp();
    }

    @Then("the auth app user changes how they get security codes")
    public void theAuthAppUserCanChangeHowTheyGetSecurityCodes() {
        crossPageFlows.authAppUserChangeHowGetSecurityCodesToSms();
    }

    @When("the sms user changes their password during reauthentication")
    public void theUserChangesTheirPasswordDuringReauth() {
        crossPageFlows.smsUserChangesPassword();
    }

    @And("the {string} user {string} is not blocked from signing back in")
    public void theUserIsAbleToSignBackInWithoutBeingBlocked(String userType, String emailAddress) {
        crossPageFlows.successfulSignIn(userType, emailAddress);
    }

    @And("the owner of the incorrect email address is not blocked from signing in")
    public void theUserIsAbleToSignBackInWithoutBeingBlocked() {
        crossPageFlows.successfulSignIn("sms", "TEST_USER_REAUTH_SMS_9");
    }

    @And("the {string} user {string} is not blocked from reauthenticating")
    public void theUserIsAbleToReauthWithoutBeingBlocked(String userType, String emailAddress) {
        crossPageFlows.successfulReauth(userType, emailAddress);
    }

    @And("the owner of the incorrect email address is not blocked from reauthenticating")
    public void theUserIsAbleToReauthWithoutBeingBlocked() {
        crossPageFlows.successfulReauth("sms", "TEST_USER_REAUTH_SMS_9");
    }
}

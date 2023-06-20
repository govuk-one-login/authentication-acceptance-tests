package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.GetSecurityCodePage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.TermsAndConditionsPage;
import uk.gov.di.test.pages.YouAskedToResendTheSecurityCodeTooManyTimesPage;
import uk.gov.di.test.utils.SignIn;

import java.net.URI;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CANNOT_GET_NEW_SECURITY_CODE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_CODE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_CODE_UPLIFT;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_PASSWORD;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.RESEND_SECURITY_CODE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.RESEND_SECURITY_CODE_TOO_MANY_TIMES;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;
import static uk.gov.di.test.utils.Constants.INVALID_PASSWORD;
import static uk.gov.di.test.utils.Constants.MISMATCHING_PASSWORD_1;
import static uk.gov.di.test.utils.Constants.MISMATCHING_PASSWORD_2;
import static uk.gov.di.test.utils.Constants.TOP_100K_PASSWORD;

public class Login extends SignIn {

    private String emailAddress;
    private String password;
    private String sixDigitCodePhone;
    private String phoneNumber;
    private String sixDigitCodeEmail;

    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public TermsAndConditionsPage termsAndConditionsPage = new TermsAndConditionsPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public GetSecurityCodePage getSecurityCodePage = new GetSecurityCodePage();
    public RpStubPage rpStubPage = new RpStubPage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public YouAskedToResendTheSecurityCodeTooManyTimesPage
            youAskedToResendTheSecurityCodeTooManyTimesPage =
                    new YouAskedToResendTheSecurityCodeTooManyTimesPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();

    @And("the existing user has valid credentials")
    public void theExistingUserHasValidCredentials() {
        emailAddress = SignIn.TEST_USER_EMAIL;
        password = SignIn.TEST_USER_PASSWORD;
        sixDigitCodePhone = SignIn.TEST_USER_PHONE_CODE;
    }

    @Given("the existing user has valid credentials and wants to reset their password")
    public void theExistingUserHasValidCredentialsAndWantsToResetTheirPassword() {
        emailAddress = System.getenv().get("RESET_PW_USER_EMAIL");
        password = System.getenv().get("RESET_PW_CURRENT_PW");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
    }

    @Given("the existing user has a password which is on the top 100k passwords list")
    public void theExistingUserHasAPasswordWhichIsOnTheTop100lPasswordsList() {
        emailAddress = System.getenv().get("REQ_RESET_PW_USER_EMAIL");
        password = System.getenv().get("REQ_RESET_PW_CURRENT_PW");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
    }

    @When("the user enters their password which is on the top 100k password list")
    public void theUserEntersTheirPasswordWhichIsOnTheTop100kPasswordList() {
        enterYourPasswordPage.enterPasswordAndContinue(password);
    }

    @When("the user clicks the forgotten password link")
    public void theUserClicksTheForgottenPasswordLink() {
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @Given("the existing user has a phone code that does not work")
    public void theExistingUserHasAPhoneCodeThatDoesNotWork() {
        emailAddress = System.getenv().get("IPN4_EXISTING_USER_EMAIL");
        password = System.getenv().get("IPN4_EXISTING_USER_PASSWORD");
    }

    @When("the existing user visits the stub relying party")
    public void theExistingUserVisitsTheStubRelyingParty() {
        rpStubPage.goToRpStub();
    }

    @And("the existing user clicks {string}")
    public void theExistingUserClicks(String options) {
        rpStubPage.selectRpOptionsById(options);
    }

    @Then("the existing user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("the existing user selects sign in")
    public void theExistingUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @Then("the existing user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the existing user enters their email address")
    public void theExistingUserEntersEmailAddress() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(emailAddress);
    }

    @Then("the existing user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(password);
    }

    @Then("the existing user is taken to the enter code page")
    public void theExistingUserIsTakenToTheEnterCodePage() {
        waitForPageLoadThenValidate(ENTER_CODE);
    }

    @Then("the existing user is taken to the enter code uplifted page")
    public void theExistingUserIsTakenToTheEnterCodeUpliftedPage() {
        waitForPageLoadThenValidate(ENTER_CODE_UPLIFT);
    }

    @When("the existing user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        checkYourPhonePage.enterPhoneCodeAndContinue(sixDigitCodePhone);
    }

    @Then("the existing user is returned to the service")
    public void theExistingUserIsReturnedToTheService() {}

    @Then("the existing user is taken to the you have signed out page")
    public void theExistingUserIsTakenToTheYouHaveSignedOutPage() {
        waitForPageLoad("Signed out");
        assertEquals("/signed-out", URI.create(driver.getCurrentUrl()).getPath());
    }

    @When("the existing user requests the phone otp code {int} times")
    public void theExistingUserRequestsThePhoneOtpCodeTimes(int timesCodeIncorrect) {
        for (int i = 0; i < timesCodeIncorrect; i++) {
            checkYourPhonePage.clickProblemsWithTheCodeLink();
            checkYourPhonePage.clickSendTheCodeAgainLink();
            waitForPageLoadThenValidate(RESEND_SECURITY_CODE);
            getSecurityCodePage.pressGetSecurityCodeButton();
        }
    }

    @Then(
            "the existing user is taken to the you asked to resend the security code too many times page")
    public void theExistingUserIsTakenToTheYouAskedToResendTheSecurityCodeTooManyTimesPage() {
        waitForPageLoadThenValidate(RESEND_SECURITY_CODE_TOO_MANY_TIMES);
    }

    @When("the existing user clicks the get a new code link")
    public void theExistingUserClicksTheGetANewCodeLink() {
        youAskedToResendTheSecurityCodeTooManyTimesPage.clickGetANewCodeLink();
    }

    @Then("the existing user is taken to the you cannot get a new security code page")
    public void theExistingUserIsTakenToTheYouCannotGetANewSecurityCodePage() {
        waitForPageLoadThenValidate(CANNOT_GET_NEW_SECURITY_CODE);
    }

    @And("the existing resend code user has valid credentials")
    public void theExistingResendCodeUserHasValidCredentials() {
        emailAddress = System.getenv().get("RESEND_CODE_TEST_USER_EMAIL");
        password = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_PASSWORD");
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

    @Then("the existing user is taken to the Welsh enter code page")
    public void theExistingUserIsTakenToTheWelshEnterCodePage() {
        assertEquals("Gwiriwch eich ffôn - GOV.UK One Login", driver.getTitle());
    }

    @When("the existing user enters their password in Welsh")
    public void theExistingUserEntersTheirPasswordInWelsh() {
        enterYourPasswordPage.enterPasswordAndContinue(password);
    }

    @When("the existing user enters their email address in Welsh")
    public void theExistingUserEntersTheirEmailAddressInWelsh() {
        enterYourEmailAddressToSignInPage.enterEmailAddress(emailAddress);
        findAndClickContinueWelsh();
    }

    @When("the user enters valid new password and correctly retypes it")
    public void theUserEntersValidNewPasswordAndCorrectlyRetypesIt() {
        String newPassword = UUID.randomUUID() + "a1";
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(newPassword, newPassword);
    }

    @When("the user resets their password to be the same as their current password")
    public void theUserResetsTheirPasswordToBeTheSameAsTheirCurrentPassword() {
        String newPassword = System.getenv().get("RESET_PW_CURRENT_PW");
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

    @When("the user enters the six digit security code from their email")
    public void theUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        checkYourEmailPage.enterEmailCode(sixDigitCodeEmail);
        findAndClickContinue();
    }

    @And("the existing user selects create an account")
    public void theExistingUserSelectsCreateAnAccount() {
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
    }

    @When("the existing user switches to {string} language")
    public void theExistingUserSwitchesLanguage(String language) {
        createOrSignInPage.switchLanguageTo(language);
    }

    @When("the user does not agree to the updated terms and conditions")
    public void theUserDoesNotAgreeToTheUpdatedTermsAndConditions() {
        termsAndConditionsPage.clickIDoNotAgreeLink();
    }

    @When("the user agrees to the updated terms and conditions")
    public void theUserAgreesToTheUpdatedTermsAndConditions() {
        termsAndConditionsPage.pressAgreeAndContinueButton();
    }

    @Given("the existing user has outdated terms and conditions")
    public void theExistingUserHasOutdatedTermsAndConditions() {
        emailAddress = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
    }
}

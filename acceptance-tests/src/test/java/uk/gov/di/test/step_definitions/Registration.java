package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.pages.*;
import uk.gov.di.test.utils.AuthAppStub;
import uk.gov.di.test.utils.SignIn;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.*;

public class Registration extends SignIn {

    private String emailAddress;
    private String password;
    private String phoneNumber;
    private String sixDigitCodeEmail;
    private String sixDigitCodePhone;
    private String tcEmailAddress;
    private String tcPassword;
    private String authAppSecretKey;
    private String internationalPhoneNumber;

    LoginPage loginPage = new LoginPage();
    RegistrationPage registrationPage = new RegistrationPage();
    NoGovUkOneLoginFoundPage noGovUkOneLoginFoundPage = new NoGovUkOneLoginFoundPage();
    EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();


    @And("the new user has an invalid email format")
    public void theNewUserHasInvalidEmail() {
        emailAddress = "joe.bloggs";
        password = "password";
    }

    @Given("a new user has an invalid UK mobile phone number")
    public void aNewUserHasAnInvalidUkMobilePhoneNumber() {
        emailAddress = System.getenv().get("IPN1_NEW_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
    }

    @Given("a new user has an invalid international mobile phone number")
    public void aNewUserHasAnInvalidInternationalMobilePhoneNumber() {
        emailAddress = System.getenv().get("IPN2_NEW_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
    }

    @Given("a new user has a valid international mobile phone number")
    public void aNewUserHasAValidInternationalMobilePhoneNumber() {
        emailAddress = System.getenv().get("IPN3_NEW_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
    }

    @When("the new user has an invalid password")
    public void theUserHasInvalidPassword() {
        password = "password";
    }

    @When("the new user has a weak password")
    public void theUserHasAWeakPassword() {
        password = "password1";
    }

    @When("the new user has a short digit only password")
    public void theNewUserHasAShortDigitOnlyPassword() {
        password = "44445555";
    }

    @When("the new user has a sequence of numbers password")
    public void theNewUserHasASequenceOfNumbersPassword() {
        password = "12345678";
    }

    @And("a new user has valid credentials")
    public void theNewUserHasValidCredential() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
        tcEmailAddress = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_EMAIL");
        tcPassword = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_PASSWORD");
        internationalPhoneNumber = System.getenv().get("TEST_USER_INTERNATIONAL_PHONE_NUMBER");
    }

    @And("the auth app user has valid credentials")
    public void theAuthAppUserHasValidCredentials() {
        emailAddress = System.getenv().get("TEST_USER_AUTH_APP_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
    }

    @And("the new user clears cookies")
    public void theNewUserClearsCookies() {
        driver.manage().deleteAllCookies();
    }

    @When("the new user has a valid email address")
    public void theNewUserHasValidEmailAddress() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
    }

    @When("the new user visits the stub relying party")
    public void theNewUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the new user clicks {string}")
    public void theNewUserClicks(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @Then("the new user is taken to the Identity Provider Login Page")
    public void theNewUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("the user selects create an account")
    public void theNewUserSelectsCreateAnAccount() {
        // accountManagementPage.clickAccountCreationLink();
        noGovUkOneLoginFoundPage.clickCreateGovUkOneLoginButton();
    }

    @Then("the new user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_CREATE);
    }

    @When("the new user selects sign in")
    public void theNewUserSelectsSignIn() {
        loginPage.signInButtonClick();
    }

    @Then("the new user is taken to the sign in to your account page")
    public void theNewUserIsTakenToTheSignInToYourAccountPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the new user enters their email address")
    public void theNewUserEntersEmailAddress() {
        loginPage.enterEmailAddress(emailAddress);
        findAndClickContinue();
    }

    @Then("the new user is taken to the account not found page")
    public void theNewUserIsTakenToTheAccountNotFoundPage() {
        waitForPageLoadThenValidate(ACCOUNT_NOT_FOUND);
    }

    @Then("the new user is asked to check their email")
    public void theNewUserIsAskedToCheckTheirEmail() {
        waitForPageLoadThenValidate(CHECK_YOUR_EMAIL);
    }

    @When("the new user enters the six digit security code incorrectly {int} times")
    public void theNewUserEntersTheSixDigitSecurityCodeIncorrectlyNTimes(int timesCodeIncorrect) {
        for (int i = 0; i < timesCodeIncorrect; i++) {
            loginPage.enterSixDigitSecurityCode(getRandomInvalidCode());
            findAndClickContinue();
            theUserIsShownAnErrorMessageOnTheEnterEmailPage();
        }
    }

    @When("the new user enters the six digit security code from their email")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        if (DEBUG_MODE) {
            new WebDriverWait(driver, Duration.of(1, MINUTES))
                    .until(
                            (ExpectedCondition<Boolean>)
                                    driver -> loginPage.getSixDigitSecurityCodeLength() == 6);
        } else {
            loginPage.enterSixDigitSecurityCode(sixDigitCodeEmail);
        }
        findAndClickContinue();
    }

    @Then("the new user is taken to the security code invalid page")
    public void theNewUserIsTakenToTheSecurityCodeInvalidPage() {
        waitForPageLoadThenValidate(SECURITY_CODE_INVALID);
    }

    @Then("the new user is taken to the resend email code page")
    public void theNewUserIsTakenToTheResendEmailCodePage() {
        waitForPageLoadThenValidate(RESEND_EMAIL_CODE);
    }

    @Then("the new user is taken to the create your password page")
    public void theNewUserIsAskedToCreateAPassword() {
        waitForPageLoadThenValidate(CREATE_PASSWORD);
    }

    @When("the new user creates a password")
    public void theNewUserCreatesAValidPassword() {
        loginPage.enterPassword(password);
        loginPage.enterConfirmPassword(password);
        findAndClickContinue();
    }

    @Then("the new user is taken to the get security codes page")
    public void theNewUserIsTakenToTheGetSecurityCodesPage() {
        waitForPageLoadThenValidate(GET_SECURITY_CODES);
    }

    @Then("the new user is taken to the finish creating your account get security codes page")
    public void theNewUserIsTakenToTheFinishCreatingYourAccountGetSecurityCodesPage() {
        waitForPageLoadThenValidate(FINISH_CREATING_YOUR_ACCOUNT_GET_SECURITY_CODES);
    }

    @When("the new user chooses text message security codes")
    public void theNewUserChoosesTextMessageSecurityCodes() {
        registrationPage.radioTextMessageSecurityCodesClick();
        findAndClickContinue();
    }

    @When("the new user chooses {string} to get security codes")
    public void theNewUserChoosesHowToGetSecurityCodes(String mfaMethod) {
        loginPage.buttonClick(mfaMethod);
        findAndClickContinue();
    }

    @Then("the new user is taken to the setup authenticator app page")
    public void theNewUserIsTakenToTheSetupAuthenticatorAppPage() {
        waitForPageLoadThenValidate(SETUP_AUTHENTICATOR_APP);
    }

    @When("the new user adds the secret key on the screen to their auth app")
    public void theNewUserAddTheSecretKeyOnTheScreenToTheirAuthApp() {
        registrationPage.iCannotScanQrCodeClick();
        authAppSecretKey = registrationPage.getSecretFieldText();
        assertTrue(registrationPage.getSecretFieldText().length() == 32);
    }

    @And("the user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        AuthAppStub authAppStub = new AuthAppStub();
        String securityCode = authAppStub.getAuthAppOneTimeCode(authAppSecretKey);
        if (securityCode.length() != 6) {
            System.out.println("Auth App Security Code: " + securityCode);
        }
        assertTrue(securityCode.length() == 6);
        loginPage.enterSixDigitSecurityCode(securityCode);
        findAndClickContinue();
    }

    @When("the existing auth app user selects sign in")
    public void theExistingAuthAppUserSelectsSignIn() {
        loginPage.signInButtonClick();
    }

    @Then("the existing auth app user is taken to the enter your email page")
    public void theExistingAuthAppUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the existing auth app user enters their email address")
    public void theExistingAuthAppUserEntersEmailAddress() {
        loginPage.enterEmailAddress(emailAddress);
        findAndClickContinue();
    }

    @Then("the existing auth app user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing auth app user enters their password")
    public void theExistingUserEntersTheirPassword() {
        loginPage.enterPassword(password);
        findAndClickContinue();
    }

    @Then("the existing user is taken to the enter authenticator app code page")
    public void theNewUserIsTakenToTheEnterAuthenticatorAppCodePage() {
        waitForPageLoadThenValidate(ENTER_AUTHENTICATOR_APP_CODE);
    }

    @Then("the existing user is taken to the enter authenticator app code uplifted page")
    public void theNewUserIsTakenToTheEnterAuthenticatorAppCodeUpliftedPage() {
        waitForPageLoadThenValidate(ENTER_AUTHENTICATOR_APP_CODE_UPLIFT);
    }

    @Then("the new user is taken to the enter phone number page")
    public void theNewUserIsTakenToTheEnterPhoneNumberPage() {
        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
    }

    @Then("the new user is taken to the finish creating your account page")
    public void theNewUserIsTakenToTheFinishCreatingYourAccountPage() {
        waitForPageLoadThenValidate(FINISH_CREATING_YOUR_ACCOUNT);
    }

    @When("the new user enters their mobile phone number")
    public void theNewUserEntersTheirMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber(phoneNumber);
        findAndClickContinue();
    }

    @Then("the new user is taken to the check your phone page")
    public void theNewUserIsTakenToTheCheckYourPhonePage() {
        waitForPageLoadThenValidate(CHECK_YOUR_PHONE);
    }

    @When("the new user enters the six digit security code from their phone")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        if (DEBUG_MODE) {
            new WebDriverWait(driver, Duration.of(1, MINUTES))
                    .until(
                            (ExpectedCondition<Boolean>)
                                    driver -> loginPage.getSixDigitSecurityCodeLength() == 6);
        } else {
            loginPage.enterSixDigitSecurityCode(sixDigitCodePhone);
        }
        findAndClickContinue();
    }

    @Then("the new user is taken to the account created page")
    public void theNewUserIsTakenToTheSuccessPage() {
        waitForPageLoadThenValidate(ACCOUNT_CREATED);
    }

    @When("the new user clicks the continue button")
    public void theNewUserClicksTheGoBackToGovUkButton() {
        registrationPage.goBackClick();
    }

    @Then("the new user is taken the the share info page")
    public void theNewUsereIsTakenTheTheShareInfoPage() {
        waitForPageLoadThenValidate(SHARE_INFO);
    }

    @When("the new user agrees to share their info")
    public void theNewUserAgreesToShareTheirInfo() {
        registrationPage.shareInfoAcceptClick();
        findAndClickContinue();
    }

    @Then("the user is returned to the service")
    public void theUserIsReturnedToTheService() {}

    @Then("the new user is taken to the Service User Info page")
    public void theNewUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        assertEquals(emailAddress, loginPage.emailDescription());
    }

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        assertTrue(loginPage.emailErrorDescriptionDetailsIsDisplayed());
    }

    @Then("the new user is not shown an error message in field {string}")
    public void theNewUserIsNotShownAnErrorMessage(String errorFieldId) {
        List<WebElement> errorFields = driver.findElements(By.id(errorFieldId));
        if (!errorFields.isEmpty()) {
            System.out.println("setup-authenticator-app error: " + errorFields.get(0));
        }
        assertTrue(errorFields.isEmpty());
    }

    @When("the new user clicks ")
    public void theNewUserClicksByName(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        loginPage.logoutButtonClick();
    }

    @When("the new user clicks AgreeTermsANDConditions")
    public void theUserClicksAgreeTermsAndConditions() {
        loginPage.termsAndConditionsButtonClick();
    }

    @When("the new user clicks the application Back button")
    public void theNewUserClicksTheApplicationBackButton() {
        pressBack();
    }

    @Then("the new user is taken to the signed out page")
    public void theNewUsereIsTakenToTheSignedOutPage() {
        waitForPageLoad("Signed out");
        assertEquals("/signed-out", URI.create(driver.getCurrentUrl()).getPath());
    }

    private String getRandomInvalidCode() {
        String randomCode = "";
        do {
            randomCode = String.format("%06d", new Random().nextInt(999999));
        } while (randomCode.equals(sixDigitCodeEmail));
        return randomCode;
    }

    @And("the new user enters their password")
    public void theNewUserEntersTheirPassword() {
        loginPage.enterPassword(password);
        findAndClickContinue();
    }

    @And("the new email code lock user has valid credentials")
    public void theNewEmailCodeLockUserHasValidCredentials() {
        emailAddress = System.getenv().get("EMAIL_CODE_LOCK_TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
    }

    @And("the new phone code lock user has valid credentials")
    public void theNewPhoneCodeLockUserHasValidCredentials() {
        emailAddress = System.getenv().get("PHONE_CODE_LOCK_TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
    }

    @When("the new user enters an incorrect email code")
    @When("the new user enters an incorrect email code one more time")
    public void theNewUserEntersAnIncorrectEmailCodeOneMoreTime() {
        loginPage.enterSixDigitSecurityCode(getRandomInvalidCode());
        findAndClickContinue();
    }

    @When("the user enters an incorrect phone code one more time")
    @When("the user enters an incorrect phone code")
    public void theNewUserEntersAnIncorrectPhoneCodeOneMoreTime() {
        loginPage.enterSixDigitSecurityCode(getRandomInvalidCode());
        findAndClickContinue();
    }

    @And("the new user enters their t&c email address")
    public void theNewUserEntersTheirTCEmailAddress() {
        loginPage.enterEmailAddress(tcEmailAddress);
        findAndClickContinue();
    }

    @And("the new user enters their t&c password")
    public void theNewUserEntersTheirTCPassword() {
        loginPage.enterPassword(tcPassword);
        findAndClickContinue();
    }

    @When("the new user does not agree to share their info")
    public void theNewUserDoesNotAgreeToShareTheirInfo() {
        registrationPage.shareInfoRejectClick();
        findAndClickContinue();
    }

    @When("the user clicks the delete your GOV.UK account button")
    public void theUserClicksTheDeleteYourGOVUKAccountButton() {
        registrationPage.deleteAccountButtonClick();
    }

    @When("the new user enters their mobile phone number using an international dialling code")
    public void theNewUserEntersTheirMobilePhoneNumberUsingAnInternationalDiallingCode() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber(internationalPhoneNumber);
        findAndClickContinue();
    }

    @When("the new user clicks the sign in to a service button")
    public void theNewUserClicksTheSignInToAServiceButton() {
        registrationPage.signinToServiceButtonClick();
    }

    @Then("the new user is taken to the sign in to a service page")
    public void theNewUserIsTakenToTheSignInToAServicePage() {
        waitForPageLoad("Sign in to a service - GOV.UK");
        assertEquals("/sign-in", URI.create(driver.getCurrentUrl()).getPath());
    }

    @And("a new user has different valid credentials")
    public void aNewUserHasDifferentValidCredentials() {
        emailAddress = System.getenv().get("TEST_USER_NEW_EMAIL");
    }

    @When("the new user submits a blank UK phone number")
    public void theNewUserSubmitsABlankUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber("");
        findAndClickContinue();
    }

    @When("the new user submits an international phone number in the UK phone number field")
    public void theNewUserSubmitsAnInternationalPhoneNumberInTheUKPhoneNumberField() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber("+61412123123");
        findAndClickContinue();
    }

    @When("the new user submits an incorrectly formatted UK phone number")
    public void theNewUserSubmitsAnIncorrectlyFormattedUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber("070000000000000");
        findAndClickContinue();
    }

    @When("the new user submits a UK phone number containing non-digit characters")
    public void theNewUserSubmitsAUKPhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber("0780312*a45");
        findAndClickContinue();
    }

    @Then("the {string} error message is displayed")
    public void theErrorMessageIsDisplayed(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, getUpperErrorMessageText());
    }

    @When("the new user ticks I do not have a UK mobile number")
    public void theNewUserTicksIDoNotHaveAUKMobileNumber() {
        enterYourMobilePhoneNumberPage.tickIDoNotHaveUkMobileNumber();
    }

    @Then("the International mobile number field is displayed")
    public void theInternationalMobileNumberFieldIsDisplayed() {
        assertTrue(enterYourMobilePhoneNumberPage.isInternationalMobileNumberFieldDisplayed());
    }

    @And("the UK mobile phone number field is disabled")
    public void theUkMobilePhoneNumberFieldIsDisabled() {
        assertEquals(false, enterYourMobilePhoneNumberPage.getStatusOfUKMobileNumberField());
    }

    @When("the new user submits a blank international mobile phone number")
    public void theNewUserSubmitsABlankInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumber("");
        findAndClickContinue();
    }

    @When("the new user submits an incorrectly formatted international mobile phone number")
    public void theNewUserSubmitsAnIncorrectlyFormattedInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumber(
                "+123456789123456789123456");
        findAndClickContinue();
    }

    @When(
            "the new user submits an international mobile phone number containing non-digit characters")
    public void theNewUserSubmitsAnInternationalMobilePhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumber("/3383838383");
        findAndClickContinue();
    }

    @When("the new user enters a valid international mobile phone number")
    public void theNewUserEntersAValidInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumber("+61412123123");
        findAndClickContinue();
    }
}

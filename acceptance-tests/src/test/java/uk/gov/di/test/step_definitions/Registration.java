package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.NoGovUkOneLoginFoundPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.utils.SignIn;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ACCOUNT_CREATED;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ACCOUNT_NOT_FOUND;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CHECK_YOUR_EMAIL;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CHECK_YOUR_PHONE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CREATE_PASSWORD;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_EMAIL_CREATE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_PHONE_NUMBER;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.GET_SECURITY_CODES;

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

    NoGovUkOneLoginFoundPage noGovUkOneLoginFoundPage = new NoGovUkOneLoginFoundPage();
    EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    RpStubPage rpStubPage = new RpStubPage();
    CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage = new SetUpAnAuthenticatorAppPage();
    EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();

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

    @Given("a user has a valid international mobile phone number")
    public void aUserHasAValidInternationalMobilePhoneNumber() {
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

    @When("the new user has a valid email address")
    public void theNewUserHasValidEmailAddress() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
    }

    @When("the user selects create an account")
    public void theUserSelectsCreateAnAccount() {
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
    }

    @When("the new user selects create an account")
    public void theNewUserSelectsCreateAnAccount() {
        noGovUkOneLoginFoundPage.clickCreateGovUkOneLoginButton();
    }

    @Then("the new user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_CREATE);
    }

    @When("the new user selects sign in")
    public void theNewUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @Then("the new user is taken to the sign in to your account page")
    public void theNewUserIsTakenToTheSignInToYourAccountPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the new user enters their email address")
    public void theNewUserEntersEmailAddress() {
        enterYourEmailAddressPage.enterEmailAddressAndContinue(emailAddress);
    }

    @Then("the new user is taken to the account not found page")
    public void theNewUserIsTakenToTheAccountNotFoundPage() {
        waitForPageLoadThenValidate(ACCOUNT_NOT_FOUND);
    }

    @Then("the new user is asked to check their email")
    public void theNewUserIsAskedToCheckTheirEmail() {
        waitForPageLoadThenValidate(CHECK_YOUR_EMAIL);
    }

    @When("the new user enters the six digit security code from their email")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        checkYourEmailPage.enterEmailCodeAndContinue(sixDigitCodeEmail);
    }

    @Then("the new user is taken to the create your password page")
    public void theNewUserIsAskedToCreateAPassword() {
        waitForPageLoadThenValidate(CREATE_PASSWORD);
    }

    @When("the new user creates a password")
    public void theNewUserCreatesAValidPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(password, password);
    }

    @Then("the new user is taken to the get security codes page")
    public void theNewUserIsTakenToTheGetSecurityCodesPage() {
        waitForPageLoadThenValidate(GET_SECURITY_CODES);
    }

    @When("the new user chooses {string} to get security codes")
    public void theNewUserChoosesHowToGetSecurityCodes(String mfaMethod) {
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue(mfaMethod);
    }

    @When("the new user adds the secret key on the screen to their auth app")
    public void theNewUserAddTheSecretKeyOnTheScreenToTheirAuthApp() {
        setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
        authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
        assertTrue(setUpAnAuthenticatorAppPage.getSecretFieldText().length() == 32);
    }

    @And("the user enters the security code from the auth app to set it up")
    public void theNewUserEntersTheSecurityCodeFromTheAuthAppToSetItUp() {
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCode(authAppSecretKey);
        findAndClickContinue();
    }

    @And("the user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage.enterCorrectAuthAppCode(
                authAppSecretKey);
        findAndClickContinue();
    }

    @When("the existing auth app user selects sign in")
    public void theExistingAuthAppUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @When("the existing auth app user enters their email address")
    public void theExistingAuthAppUserEntersEmailAddress() {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(emailAddress);
    }

    @When("the existing auth app user enters their password")
    public void theExistingUserEntersTheirPassword() {
        enterYourPasswordPage.enterPasswordAndContinue(password);
    }

    @Then("the new user is taken to the enter phone number page")
    public void theNewUserIsTakenToTheEnterPhoneNumberPage() {
        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
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
        checkYourPhonePage.enterPhoneCodeAndContinue(sixDigitCodePhone);
    }

    @Then("the new user is taken to the account created page")
    public void theNewUserIsTakenToTheSuccessPage() {
        waitForPageLoadThenValidate(ACCOUNT_CREATED);
    }

    @When("the new user clicks the continue button")
    public void theNewUserClicksTheContinueButton() {
        findAndClickContinue();
    }

    @Then("the user is returned to the service")
    public void theUserIsReturnedToTheService() {}

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        assertTrue(isErrorSummaryDisplayed());
    }

    @Then("the user is not shown any error messages")
    public void theNewUserIsNotShownAnErrorMessage() {
        List<WebElement> errorFields = driver.findElements(By.id("code-error"));
        if (!errorFields.isEmpty()) {
            System.out.println("setup-authenticator-app error: " + errorFields.get(0));
        }
        assertTrue(errorFields.isEmpty());
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        findAndClickButtonByText("Log out");
    }

    @When("the user clicks the Back link")
    public void theNewUserClicksTheApplicationBackButton() {
        pressBack();
    }

    @Then("the new user is taken to the signed out page")
    public void theNewUsereIsTakenToTheSignedOutPage() {
        waitForPageLoad("Signed out");
        assertEquals("/signed-out", URI.create(driver.getCurrentUrl()).getPath());
    }

    @When("the new user enters their mobile phone number using an international dialling code")
    public void theNewUserEntersTheirMobilePhoneNumberUsingAnInternationalDiallingCode() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumber(internationalPhoneNumber);
        findAndClickContinue();
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

    @When("the new user ticks I do not have a UK mobile number")
    public void theNewUserTicksIDoNotHaveAUKMobileNumber() {
        enterYourMobilePhoneNumberPage.tickIDoNotHaveUkMobileNumber();
    }

    @Then("the International mobile number field is displayed")
    public void theInternationalMobileNumberFieldIsDisplayed() {
        assertTrue(enterYourMobilePhoneNumberPage.isInternationalMobileNumberFieldDisplayed());
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

    @Given(
            "a user has selected text message as their auth method and has moved on to the next page")
    public void aUserHasSelectedAnAuthMethodAndHasMovedOnToTheNextPage() {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsById("");
        findAndClickContinue();
        waitForPageLoad("Create a GOV.UK One Login or sign in");
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        waitForPageLoad("Enter your email address");
        enterYourEmailAddressPage.enterEmailAddressAndContinue(
                System.getenv().get("TEST_USER_STATE_PRESERVATION_EMAIL1"));
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
        waitForPageLoad("Create your password");
        createYourPasswordPage.enterBothPasswordsAndContinue("new-password1", "new-password1");
        waitForPageLoad("Choose how to get security codes");
        assertFalse(chooseHowToGetSecurityCodesPage.getTextMessageRadioButtonStatus());
        assertFalse(chooseHowToGetSecurityCodesPage.getAuthAppRadioButtonStatus());
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("Text message");
        waitForPageLoad("Enter your mobile phone number");
    }

    @Given("a user has selected auth app as their auth method and has moved on to the next page")
    public void aUserHasSelectedAuthAppAsTheirAuthMethodAndHasMovedOnToTheNextPage() {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsById("");
        findAndClickContinue();
        waitForPageLoad("Create a GOV.UK One Login or sign in");
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        waitForPageLoad("Enter your email address");
        enterYourEmailAddressPage.enterEmailAddressAndContinue(
                System.getenv().get("TEST_USER_STATE_PRESERVATION_EMAIL2"));
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
        waitForPageLoad("Create your password");
        createYourPasswordPage.enterBothPasswordsAndContinue("new-password1", "new-password1");
        waitForPageLoad("Choose how to get security codes");
        assertFalse(chooseHowToGetSecurityCodesPage.getTextMessageRadioButtonStatus());
        assertFalse(chooseHowToGetSecurityCodesPage.getAuthAppRadioButtonStatus());
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("Auth app");
        waitForPageLoad("Set up an authenticator app");
    }

    @And("their previously chosen text message auth method remains selected")
    public void theirPreviouslySelectedTextMessageAuthMethodRemainsSelected() {
        waitForPageLoad("Choose how to get security codes");
        assertTrue(chooseHowToGetSecurityCodesPage.getTextMessageRadioButtonStatus());
    }

    @And("their previously chosen auth app auth method remains selected")
    public void theirPreviouslySelectedAuthAppAuthMethodRemainsSelected() {
        waitForPageLoad("Choose how to get security codes");
        assertTrue(chooseHowToGetSecurityCodesPage.getAuthAppRadioButtonStatus());
    }
}

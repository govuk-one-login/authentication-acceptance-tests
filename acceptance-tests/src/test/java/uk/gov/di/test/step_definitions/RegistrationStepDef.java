package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.NoGovUkOneLoginFoundPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ACCOUNT_CREATED;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CHECK_YOUR_EMAIL;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CHECK_YOUR_PHONE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.CREATE_PASSWORD;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_EMAIL_CREATE;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.ENTER_PHONE_NUMBER;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.GET_SECURITY_CODES;
import static uk.gov.di.test.utils.Constants.*;

public class RegistrationStepDef extends BasePage {

    private String authAppSecretKey;

    NoGovUkOneLoginFoundPage noGovUkOneLoginFoundPage = new NoGovUkOneLoginFoundPage();
    EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    RpStubPage rpStubPage = new RpStubPage();
    CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage = new SetUpAnAuthenticatorAppPage();
    EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();

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

    @Then("the new user is asked to check their email")
    public void theNewUserIsAskedToCheckTheirEmail() {
        waitForPageLoadThenValidate(CHECK_YOUR_EMAIL);
    }

    @When("the user enters the six digit security code from their email")
    @When("the new user enters the six digit security code from their email")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    }

    @Then("the new user is taken to the create your password page")
    public void theNewUserIsAskedToCreateAPassword() {
        waitForPageLoadThenValidate(CREATE_PASSWORD);
    }

    @And("the new user creates a password")
    public void theNewUserCreatesAPassword() {
        String passwordVal = System.getenv().get("TEST_USER_PASSWORD");
        createYourPasswordPage.enterBothPasswordsAndContinue(passwordVal, passwordVal);
    }

    @And("the new user creates and enters an invalid password")
    public void theNewUserCreatesAndEntersAnInvalidPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(INVALID_PASSWORD, INVALID_PASSWORD);
    }

    @And("the new user creates and enters a weak password")
    public void theNewUserCreatesAndEntersAWeakPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(WEAK_PASSWORD, WEAK_PASSWORD);
    }

    @And("the new user creates and enters short digit only password")
    public void theNewUserCreatesAndEntersShortDigitOnlyPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(
                SHORT_DIGIT_PASSWORD, SHORT_DIGIT_PASSWORD);
    }

    @And("the new user creates and enters a sequence of numbers password")
    public void theNewUserCreatesAndEntersASequenceOfNumbersPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(
                SEQUENCE_NUMBER_PASSWORD, SEQUENCE_NUMBER_PASSWORD);
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
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
    }

    @And("the user enters the security code from the auth app")
    public void theNewUserEntersTheSecurityCodeFromTheAuthApp() {
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                .enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
    }

    @When("the existing auth app user selects sign in")
    public void theExistingAuthAppUserSelectsSignIn() {
        createOrSignInPage.clickSignInButton();
    }

    @Then("the new user is taken to the enter phone number page")
    public void theNewUserIsTakenToTheEnterPhoneNumberPage() {
        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
    }

    @When("the new user enters their mobile phone number")
    public void theNewUserEntersTheirMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
    }

    @Then("the new user is taken to the check your phone page")
    public void theNewUserIsTakenToTheCheckYourPhonePage() {
        waitForPageLoadThenValidate(CHECK_YOUR_PHONE);
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
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_INTERNATIONAL_PHONE_NUMBER"));
    }

    @When("the new user submits a blank UK phone number")
    public void theNewUserSubmitsABlankUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("");
    }

    @When("the new user submits an international phone number in the UK phone number field")
    public void theNewUserSubmitsAnInternationalPhoneNumberInTheUKPhoneNumberField() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("+61412123123");
    }

    @When("the new user submits an incorrectly formatted UK phone number")
    public void theNewUserSubmitsAnIncorrectlyFormattedUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("070000000000000");
    }

    @When("the new user submits a UK phone number containing non-digit characters")
    public void theNewUserSubmitsAUKPhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("0780312*a45");
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
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue("");
    }

    @When("the new user submits an incorrectly formatted international mobile phone number")
    public void theNewUserSubmitsAnIncorrectlyFormattedInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                "+123456789123456789123456");
    }

    @When(
            "the new user submits an international mobile phone number containing non-digit characters")
    public void theNewUserSubmitsAnInternationalMobilePhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                "/3383838383");
    }

    @When("the new user enters a valid international mobile phone number")
    public void theNewUserEntersAValidInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                "+61412123123");
    }

    @Given(
            "a user has selected text message as their auth method and has moved on to the next page")
    public void aUserHasSelectedAnAuthMethodAndHasMovedOnToTheNextPage() {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
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
        rpStubPage.selectRpOptionsByIdAndContinue("");
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

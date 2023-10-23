package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.RpStubPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.*;

public class RegistrationStepDef extends BasePage {

    EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    RpStubPage rpStubPage = new RpStubPage();
    CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();

    @When("the user selects create an account")
    public void theUserSelectsCreateAnAccount() {
        findAndClickButtonByText("Create a GOV.UK One Login");
    }

    @When("the user enters the six digit security code from their email")
    public void theUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    }

    @And("the user creates a password")
    public void theUserCreatesAPassword() {
        String passwordVal = System.getenv().get("TEST_USER_PASSWORD");
        createYourPasswordPage.enterBothPasswordsAndContinue(passwordVal, passwordVal);
    }

    @And("the user creates and enters an invalid password")
    public void theUserCreatesAndEntersAnInvalidPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(INVALID_PASSWORD, INVALID_PASSWORD);
    }

    @And("the user creates and enters a weak password")
    public void theUserCreatesAndEntersAWeakPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(WEAK_PASSWORD, WEAK_PASSWORD);
    }

    @And("the user creates and enters short digit only password")
    public void theUserCreatesAndEntersShortDigitOnlyPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(
                SHORT_DIGIT_PASSWORD, SHORT_DIGIT_PASSWORD);
    }

    @And("the user creates and enters a sequence of numbers password")
    public void theUserCreatesAndEntersASequenceOfNumbersPassword() {
        createYourPasswordPage.enterBothPasswordsAndContinue(
                SEQUENCE_NUMBER_PASSWORD, SEQUENCE_NUMBER_PASSWORD);
    }

    @When("the user chooses {string} to get security codes")
    public void theUserChoosesHowToGetSecurityCodes(String mfaMethod) {
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue(mfaMethod);
    }

    @When("the user enters their mobile phone number")
    public void theUserEntersTheirMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        findAndClickButtonByText("Log out");
    }

    @When("the user enters their mobile phone number using an international dialling code")
    public void theUserEntersTheirMobilePhoneNumberUsingAnInternationalDiallingCode() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_INTERNATIONAL_PHONE_NUMBER"));
    }

    @When("the user submits a blank UK phone number")
    public void theUserSubmitsABlankUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("");
    }

    @When("the user submits an international phone number in the UK phone number field")
    public void theUserSubmitsAnInternationalPhoneNumberInTheUKPhoneNumberField() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("+61412123123");
    }

    @When("the user submits an incorrectly formatted UK phone number")
    public void theUserSubmitsAnIncorrectlyFormattedUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("070000000000000");
    }

    @When("the user submits a UK phone number containing non-digit characters")
    public void theUserSubmitsAUKPhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("0780312*a45");
    }

    @When("the user ticks I do not have a UK mobile number")
    public void theUserTicksIDoNotHaveAUKMobileNumber() {
        enterYourMobilePhoneNumberPage.tickIDoNotHaveUkMobileNumber();
    }

    @Then("the International mobile number field is displayed")
    public void theInternationalMobileNumberFieldIsDisplayed() {
        assertTrue(enterYourMobilePhoneNumberPage.isInternationalMobileNumberFieldDisplayed());
    }

    @When("the user submits a blank international mobile phone number")
    public void theUserSubmitsABlankInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue("");
    }

    @When("the user submits an incorrectly formatted international mobile phone number")
    public void theUserSubmitsAnIncorrectlyFormattedInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                "+123456789123456789123456");
    }

    @When("the user submits an international mobile phone number containing non-digit characters")
    public void theUserSubmitsAnInternationalMobilePhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                "/3383838383");
    }

    @When("the user enters a valid international mobile phone number")
    public void theUserEntersAValidInternationalMobilePhoneNumber() {
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

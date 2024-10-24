package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.NoGovUkOneLoginFoundPage;
import uk.gov.di.test.pages.StubUserInfoPage;

import static org.junit.Assert.assertTrue;
import static uk.gov.di.test.utils.Constants.INTERNATIONAL_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT;
import static uk.gov.di.test.utils.Constants.INTERNATIONAL_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS;
import static uk.gov.di.test.utils.Constants.INTERNATIONAL_MOBILE_PHONE_NUMBER_VALID;
import static uk.gov.di.test.utils.Constants.INVALID_PASSWORD;
import static uk.gov.di.test.utils.Constants.SEQUENCE_NUMBER_PASSWORD;
import static uk.gov.di.test.utils.Constants.SHORT_DIGIT_PASSWORD;
import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT;
import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS;
import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER_WITH_INTERNATIONAL_DIALING_CODE;
import static uk.gov.di.test.utils.Constants.WEAK_PASSWORD;

public class RegistrationStepDef extends BasePage {
    public CrossPageFlows crossPageFlows;
    private final World world;

    EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    NoGovUkOneLoginFoundPage noGovUkOneLoginFoundPage = new NoGovUkOneLoginFoundPage();
    StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();

    public RegistrationStepDef(World world) {
        this.world = world;
        this.crossPageFlows = new CrossPageFlows(world);
    }

    @When("the user selects create an account")
    public void theUserSelectsCreateAnAccount() {
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
    }

    @When("the user chooses to create an account")
    public void theUserChoosesToCreateAnAccount() {
        noGovUkOneLoginFoundPage.clickCreateGovUkOneLoginButton();
    }

    @When("the user enters the six digit security code from their email")
    public void theUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
    }

    @And("the user creates a password")
    public void theUserCreatesAPassword() {
        String userPassword = world.getUserPassword();
        createYourPasswordPage.enterBothPasswordsAndContinue(userPassword, userPassword);
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

    @ParameterType("text message|auth app")
    public String authMethod(String intervention) {
        return intervention;
    }

    @When("the user chooses {authMethod} to get security codes")
    public void theUserChoosesHowToGetSecurityCodes(String mfaMethod) {
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue(mfaMethod);
    }

    @When("the user chooses {authMethod} to get security codes and progress to set it up")
    public void theUserToGetSecurityCodesAndProgressToSetItUp(String userType) {
        crossPageFlows.setUpAuthenticationBy(userType);
    }

    @When("the user enters their mobile phone number")
    public void theUserEntersTheirMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterValidUkPhoneNumberAndContinue();
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        stubUserInfoPage.logoutOfAccount();
    }

    @When("the user enters their mobile phone number using an international dialling code")
    public void theUserEntersTheirMobilePhoneNumberUsingAnInternationalDiallingCode() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                UK_MOBILE_PHONE_NUMBER_WITH_INTERNATIONAL_DIALING_CODE);
    }

    @When("the user submits a blank UK phone number")
    public void theUserSubmitsABlankUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("");
    }

    @When("the user submits an international phone number in the UK phone number field")
    public void theUserSubmitsAnInternationalPhoneNumberInTheUKPhoneNumberField() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                INTERNATIONAL_MOBILE_PHONE_NUMBER_VALID);
    }

    @When("the user submits an incorrectly formatted UK phone number")
    public void theUserSubmitsAnIncorrectlyFormattedUKPhoneNumber() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                UK_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT);
    }

    @When("the user submits a UK phone number containing non-digit characters")
    public void theUserSubmitsAUKPhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                UK_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS);
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
                INTERNATIONAL_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT);
    }

    @When("the user submits an international mobile phone number containing non-digit characters")
    public void theUserSubmitsAnInternationalMobilePhoneNumberContainingNonDigitCharacters() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                INTERNATIONAL_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS);
    }

    @When("the user enters a valid international mobile phone number")
    public void theUserEntersAValidInternationalMobilePhoneNumber() {
        enterYourMobilePhoneNumberPage.enterInternationalMobilePhoneNumberAndContinue(
                INTERNATIONAL_MOBILE_PHONE_NUMBER_VALID);
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

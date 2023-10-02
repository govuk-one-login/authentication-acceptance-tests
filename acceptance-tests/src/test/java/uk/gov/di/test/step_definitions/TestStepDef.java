package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.*;

public class TestStepDef extends BasePage {
    EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    EnterYourEmailAddressToBasePagePage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToBasePagePage();
    CreateOrBasePagePage createOrSignInPage = new CreateOrBasePagePage();
    RpStubPage rpStubPage = new RpStubPage();
    CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    EnterASecurityCodeToContinuePage enterASecurityCodeToContinuePage =
            new EnterASecurityCodeToContinuePage();
    SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage = new SetUpAnAuthenticatorAppPage();
    EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();

    @And("user selects the sign in button")
    public void userSelectsTheSignInButton() {
        createOrSignInPage.clickSignInButton();
    }

    @And("user enters {string} email address")
    public void userEntersEmailAddress(String email) {
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(System.getenv().get(email));
    }

    @And("user enters {string} password")
    public void userEntersPassword(String password) {
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get(password));
    }

    @Then("{string} page is displayed")
    public void pageIsDisplayed(String pageTitle) {
        rpStubPage.waitForPageLoad(pageTitle);
    }

    @Then("the link {string} is available")
    public void theLinkIsAvailable(String linkText) {
        assertTrue(isLinkTextDisplayed(linkText));
    }

    @When("the user enters {string} as the six digit security code from their email")
    public void theUserEntersAsTheSixDigitSecurityCodeFromTheirEmail(String sixDigitCodeEmail) {
        checkYourEmailPage.enterEmailCode(System.getenv().get(sixDigitCodeEmail));
        findAndClickContinue();
    }

    @When("the existing user enters {string} as the six digit security code from their phone")
    public void theExistingUserEntersAsTheSixDigitSecurityCodeFromTheirPhone(
            String sixDigitCodeEmail) {
        checkYourPhonePage.enterPhoneCodeAndContinue(System.getenv().get(sixDigitCodeEmail));
    }

    @And("the users last {int} digits of {string} SMS number is displayed")
    public void theUsersLastDigitsOfSMSNumberIsDisplayed(int digitNo, String userPhoneNumber) {
        var phoneNumber = System.getenv().get(userPhoneNumber);
        String expectedLastThreeDigitsOfPhoneNo =
                phoneNumber.substring(phoneNumber.length() - digitNo);

        assertEquals(
                expectedLastThreeDigitsOfPhoneNo,
                enterASecurityCodeToContinuePage.getThreeDigitsNumber());
    }

    @When("the user enters their {string} code from the auth app")
    public void theUserEntersTheirCodeFromTheAuthApp(String authAppSecretKey) {
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCode(System.getenv().get(authAppSecretKey));
        findAndClickContinue();
    }
}

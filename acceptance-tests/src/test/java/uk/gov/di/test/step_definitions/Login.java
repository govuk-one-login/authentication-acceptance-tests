package uk.gov.di.test.step_definitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.pages.LoginPage;
import uk.gov.di.test.utils.SignIn;
import uk.gov.di.test.utils.StepData;

import java.net.MalformedURLException;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.*;

public class Login extends SignIn {
    private StepData stepData;

    public Login(StepData stepData) {
        this.stepData = stepData;
    }
    // private String emailAddress;
    // private String password;
    // private String sixDigitCodePhone;

    public LoginPage loginPage = new LoginPage();

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @AfterStep
    public void checkAccessibility() {
        Axe.thereAreNoAccessibilityViolations();
    }

    @And("the existing user has valid credentials")
    public void theExistingUserHasValidCredentials() {
        stepData.emailAddress = SignIn.TEST_USER_EMAIL;
        stepData.password = SignIn.TEST_USER_PASSWORD;
        stepData.sixDigitCodePhone = SignIn.TEST_USER_PHONE_CODE;
    }

    @Given("the existing user has a phone code that does not work")
    public void theExistingUserHasAPhoneCodeThatDoesNotWork() {
        stepData.emailAddress = System.getenv().get("IPN4_EXISTING_USER_EMAIL");
        stepData.password = System.getenv().get("IPN4_EXISTING_USER_PASSWORD");
    }

    @And("the existing user has invalid credentials")
    public void theExistingUserHasInvalidCredentials() {
        stepData.emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        stepData.password = "wrong-password";
    }

    @When("the existing user visits the stub relying party")
    public void theExistingUserVisitsTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the existing user clicks {string}")
    public void theExistingUserClicks(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @Then("the existing user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("the existing user selects sign in")
    public void theExistingUserSelectsSignIn() {
        loginPage.signInLinkClick();
    }

    @Then("the existing user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the existing user enters their email address")
    public void theExistingUserEntersEmailAddress() {
        loginPage.enterEmailAddress(stepData.emailAddress);
        findAndClickContinue();
    }

    @Then("the existing user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        loginPage.enterPassword(stepData.password);
        findAndClickContinue();
    }

    @Then("the existing user is taken to the enter code page")
    public void theExistingUserIsTakenToTheEnterCodePage() {
        waitForPageLoadThenValidate(ENTER_CODE);
    }

    @When("the existing user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        if (DEBUG_MODE) {
            new WebDriverWait(driver, Duration.of(1, MINUTES))
                    .until(
                            (ExpectedCondition<Boolean>)
                                    driver -> loginPage.getSixDigitSecurityCodeLength() == 6);
        } else {
            loginPage.enterSixDigitSecurityCode(stepData.sixDigitCodePhone);
        }
        findAndClickContinue();
    }
    //
    //    @Then("the existing user is returned to the service")
    //    public void theExistingUserIsReturnedToTheService() {}
    //
    //    @Then("the existing user is taken to the Service User Info page")
    //    public void theExistingUserIsTakenToTheServiceUserInfoPage() {
    //        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
    //        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
    //        assertEquals(stepData.emailAddress, loginPage.emailDescription());
    //    }
    //
    //    @Then("the existing user is shown an error message")
    //    public void theExistingUserIsShownAnErrorMessageOnTheEnterEmailPage() {
    //        assertTrue(loginPage.emailErrorDescriptionDetailsIsDisplayed());
    //    }
    //
    //    @Then("the existing user is taken to the enter phone number page")
    //    public void theExistingUserIsTakenToTheEnterPhoneNumberPage() {
    //        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
    //    }
    //
    //    @Then("the existing user is taken to the you have signed out page")
    //    public void theExistingUserIsTakenToTheYouHaveSignedOutPage() {
    //        waitForPageLoad("Signed out");
    //        assertEquals("/signed-out", URI.create(driver.getCurrentUrl()).getPath());
    //    }
    //
    //    @And("the existing user clicks by name {string}")
    //    public void theExistingUserClicksByName(String buttonName) {
    //        loginPage.buttonClick(buttonName);
    //    }
    //
    //    @When("the existing user requests the phone otp code {int} times")
    //    public void theExistingUserRequestsThePhoneOtpCodeTimes(int timesCodeIncorrect) {
    //        for (int i = 0; i < timesCodeIncorrect; i++) {
    //            loginPage.problemWithTheCodeClick();
    //            loginPage.sendTheCodeAgainLinkClick();
    //            waitForPageLoadThenValidate(RESEND_SECURITY_CODE);
    //            loginPage.securityCodeClick();
    //        }
    //    }
    //
    //    @Then(
    //            "the existing user is taken to the you asked to resend the security code too many
    // times page")
    //    public void theExistingUserIsTakenToTheYouAskedToResendTheSecurityCodeTooManyTimesPage() {
    //        waitForPageLoadThenValidate(RESEND_SECURITY_CODE_TOO_MANY_TIMES);
    //    }
    //
    //    @When("the existing user clicks the get a new code link")
    //    public void theExistingUserClicksTheGetANewCodeLink() {
    //        loginPage.getNewTheCodeAgainLinkClick();
    //    }
    //
    //    @Then("the existing user is taken to the you cannot get a new security code page")
    //    public void theExistingUserIsTakenToTheYouCannotGetANewSecurityCodePage() {
    //        waitForPageLoadThenValidate(CANNOT_GET_NEW_SECURITY_CODE);
    //    }
    //
    //    @And("the existing resend code user has valid credentials")
    //    public void theExistingResendCodeUserHasValidCredentials() {
    //        stepData.emailAddress = System.getenv().get("RESEND_CODE_TEST_USER_EMAIL");
    //        stepData.password = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_PASSWORD");
    //    }
    //
    //    @Then("the existing user is taken to the Identity Provider Welsh Login Page")
    //    public void theExistingUserIsTakenToTheIdentityProviderWelshLoginPage() {
    //        assertEquals("Creu GOV.UK One Login neu fewngofnodi - GOV.UK One Login",
    // driver.getTitle());
    //    }
    //
    //    @Then("the existing user is taken to the Welsh enter your email page")
    //    public void theExistingUserIsTakenToTheWelshEnterYourEmailPage() {
    //        assertEquals(
    //                "Rhowch eich cyfeiriad e-bost i fewngofnodi i’ch GOV.UK One Login - GOV.UK One
    // Login",
    //                driver.getTitle());
    //        Assertions.assertNotEquals("Continue", loginPage.continueButtonText());
    //        Assertions.assertNotEquals("Back", loginPage.backButtonText());
    //    }
    //
    //    @Then("the existing user is prompted for their password in Welsh")
    //    public void theExistingUserIsPromptedForTheirPasswordInWelsh() {
    //        assertEquals("Rhowch eich cyfrinair - GOV.UK One Login", driver.getTitle());
    //    }
    //
    //    @Then("the existing user is taken to the Welsh enter code page")
    //    public void theExistingUserIsTakenToTheWelshEnterCodePage() {
    //        assertEquals("Gwiriwch eich ffôn - GOV.UK One Login", driver.getTitle());
    //    }
    //
    //    @When("the existing user enters their password in Welsh")
    //    public void theExistingUserEntersTheirPasswordInWelsh() {
    //        loginPage.enterPassword(stepData.password);
    //        findAndClickContinueWelsh();
    //    }
    //
    //    @When("the existing user enters their email address in Welsh")
    //    public void theExistingUserEntersTheirEmailAddressInWelsh() {
    //        loginPage.enterEmailAddress(stepData.emailAddress);
    //        findAndClickContinueWelsh();
    //    }
}

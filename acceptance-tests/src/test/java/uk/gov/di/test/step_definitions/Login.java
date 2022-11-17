package uk.gov.di.test.step_definitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.pages.LoginPage;
import uk.gov.di.test.utils.SignIn;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.AuthenticationJourneyPages.*;

public class Login extends SignIn {

    private String emailAddress;
    private String password;
    private String sixDigitCodePhone;

    public LoginPage loginPage = new LoginPage();

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
        driver.manage().deleteAllCookies();
    }
    @AfterStep
    public void checkAccessibility() {
        Axe.thereAreNoAccessibilityViolations();
    }

    @Given("the login services are running")
    public void theServicesAreRunning() {}

    @And("the existing user has valid credentials")
    public void theExistingUserHasValidCredentials() {
        emailAddress = SignIn.TEST_USER_EMAIL;
        password = SignIn.TEST_USER_PASSWORD;
        sixDigitCodePhone = SignIn.TEST_USER_PHONE_CODE;
    }

    @And("the existing user has invalid credentials")
    public void theExistingUserHasInvalidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "wrong-password";
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
        loginPage.enterEmailAddress(emailAddress);
        findAndClickContinue();
    }

    @Then("the existing user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        loginPage.enterPassword(password);
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
            loginPage.enterSixDigitSecurityCode(sixDigitCodePhone);
        }
        findAndClickContinue();
    }

    @Then("the existing user is returned to the service")
    public void theExistingUserIsReturnedToTheService() {}

    @Then("the existing user is taken to the Service User Info page")
    public void theExistingUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        assertEquals(emailAddress, loginPage.emailDescription());
    }

    @Then("the existing user is shown an error message")
    public void theExistingUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        assertTrue(loginPage.emailErrorDescriptionDetailsIsDisplayed());
    }

    @Then("the existing user is taken to the enter phone number page")
    public void theExistingUserIsTakenToTheEnterPhoneNumberPage() {
        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
    }

    @Then("the existing user is taken to the you have signed out page")
    public void theExistingUserIsTakenToTheYouHaveSignedOutPage() {
        waitForPageLoad("Signed out");
        assertEquals("/signed-out", URI.create(driver.getCurrentUrl()).getPath());
    }

    @And("the existing user clicks by name {string}")
    public void theExistingUserClicksByName(String buttonName) {
        loginPage.buttonClick(buttonName);
    }

    @When("the existing user requests the phone otp code {int} times")
    public void theExistingUserRequestsThePhoneOtpCodeTimes(int timesCodeIncorrect) {
        for (int i = 0; i < timesCodeIncorrect; i++) {
            loginPage.problemWithTheCodeClick();
            loginPage.sendTheCodeAgainLinkClick();
            waitForPageLoadThenValidate(RESEND_SECURITY_CODE);
            loginPage.securityCodeClick();
        }
    }

    @Then(
            "the existing user is taken to the you asked to resend the security code too many times page")
    public void theExistingUserIsTakenToTheYouAskedToResendTheSecurityCodeTooManyTimesPage() {
        waitForPageLoadThenValidate(RESEND_SECURITY_CODE_TOO_MANY_TIMES);
    }

    @When("the existing user clicks the get a new code link")
    public void theExistingUserClicksTheGetANewCodeLink() {
        loginPage.getNewTheCodeAgainLinkClick();
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
        assertEquals("Creu cyfrif GOV.UK neu fewngofnodi - Cyfrif GOV.UK", driver.getTitle());
    }

    @Then("the existing user is taken to the Welsh enter your email page")
    public void theExistingUserIsTakenToTheWelshEnterYourEmailPage() {
        assertEquals(
                "Rhowch eich cyfeiriad e-bost i fewngofnodi i'ch cyfrif GOV.UK - Cyfrif GOV.UK",
                driver.getTitle());
        Assertions.assertNotEquals("Continue", loginPage.continueButtonText());
        Assertions.assertNotEquals("Back", loginPage.backButtonText());
    }

    @Then("the existing user is prompted for their password in Welsh")
    public void theExistingUserIsPromptedForTheirPasswordInWelsh() {
        assertEquals("Rhowch eich cyfrinair - Cyfrif GOV.UK", driver.getTitle());
    }

    @Then("the existing user is taken to the Welsh enter code page")
    public void theExistingUserIsTakenToTheWelshEnterCodePage() {
        assertEquals("Gwiriwch eich ffôn - Cyfrif GOV.UK", driver.getTitle());
    }

    @When("the existing user enters their password in Welsh")
    public void theExistingUserEntersTheirPasswordInWelsh() {
        loginPage.enterPassword(password);
        findAndClickContinueWelsh();
    }

    @When("the existing user enters their email address in Welsh")
    public void theExistingUserEntersTheirEmailAddressInWelsh() {
        loginPage.enterEmailAddress(emailAddress);
        findAndClickContinueWelsh();
    }
}

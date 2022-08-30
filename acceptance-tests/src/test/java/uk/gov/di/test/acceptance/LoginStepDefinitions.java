package uk.gov.di.test.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CANNOT_GET_NEW_SECURITY_CODE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_CODE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER_WELSH;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_PASSWORD;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_PHONE_NUMBER;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.RESEND_SECURITY_CODE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.RESEND_SECURITY_CODE_TOO_MANY_TIMES;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SIGN_IN_OR_CREATE_WELSH;

public class LoginStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;
    private String sixDigitCodePhone;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @Given("the login services are running")
    public void theServicesAreRunning() {}

    @And("the existing user has valid credentials")
    public void theExistingUserHasValidCredentials() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
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
        WebElement button = driver.findElement(By.id(buttonName));
        button.click();
    }

    @Then("the existing user is taken to the Identity Provider Login Page")
    public void theExistingUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("the existing user selects sign in")
    public void theExistingUserSelectsSignIn() {
        WebElement link = driver.findElement(By.id("sign-in-link"));
        link.click();
    }

    @Then("the existing user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the existing user enters their email address")
    public void theExistingUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.sendKeys(emailAddress);
        findAndClickContinue();
    }

    @Then("the existing user is prompted for their password")
    public void theExistingUserIsPromptedForPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD);
    }

    @When("the existing user enters their password")
    public void theExistingUserEntersTheirPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        findAndClickContinue();
    }

    @Then("the existing user is taken to the enter code page")
    public void theExistingUserIsTakenToTheEnterCodePage() {
        waitForPageLoadThenValidate(ENTER_CODE);
    }

    @When("the existing user enters the six digit security code from their phone")
    public void theExistingUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        WebElement sixDigitSecurityCodeField = driver.findElement(By.id("code"));
        if (DEBUG_MODE) {
            new WebDriverWait(driver, Duration.of(1, MINUTES))
                    .until(
                            (ExpectedCondition<Boolean>)
                                    driver ->
                                            sixDigitSecurityCodeField.getAttribute("value").length()
                                                    == 6);
        } else {
            sixDigitSecurityCodeField.sendKeys(sixDigitCodePhone);
        }
        findAndClickContinue();
    }

    @Then("the existing user is returned to the service")
    public void theExistingUserIsReturnedToTheService() {}

    @Then("the existing user is taken to the Service User Info page")
    public void theExistingUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        WebElement emailDescriptionDetails = driver.findElement(By.id("user-info-email"));
        assertEquals(emailAddress, emailDescriptionDetails.getText().trim());
    }

    @Then("the existing user is shown an error message")
    public void theExistingUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        WebElement emailDescriptionDetails = driver.findElement(By.id("error-summary-title"));
        assertTrue(emailDescriptionDetails.isDisplayed());
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
        WebElement button = driver.findElement(By.name(buttonName));
        button.click();
    }

    @When("the existing user requests the phone otp code {int} times")
    public void theExistingUserRequestsThePhoneOtpCodeTimes(int timesCodeIncorrect) {
        for (int i = 0; i < timesCodeIncorrect; i++) {
            WebElement problemWithTheCode =
                    driver.findElement(By.className("govuk-details__summary"));
            problemWithTheCode.click();
            WebElement sendTheCodeAgainLink =
                    driver.findElement(By.cssSelector("#form-tracking > details > div > p > a"));
            sendTheCodeAgainLink.click();
            waitForPageLoadThenValidate(RESEND_SECURITY_CODE);
            WebElement getSecurityCodeButton =
                    driver.findElement(By.cssSelector("#main-content > div > div > form > button"));
            getSecurityCodeButton.click();
        }
    }

    @Then(
            "the existing user is taken to the you asked to resend the security code too many times page")
    public void theExistingUserIsTakenToTheYouAskedToResendTheSecurityCodeTooManyTimesPage() {
        waitForPageLoadThenValidate(RESEND_SECURITY_CODE_TOO_MANY_TIMES);
    }

    @When("the existing user clicks the get a new code link")
    public void theExistingUserClicksTheGetANewCodeLink() {
        WebElement getANewCodeLink =
                driver.findElement(
                        By.cssSelector("#main-content > div > div > p:nth-child(3) > a"));
        getANewCodeLink.click();
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
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE_WELSH);
    }

    @Then("the existing user is taken to the Welsh enter your email page")
    public void theExistingUserIsTakenToTheWelshEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER_WELSH);
    }
}

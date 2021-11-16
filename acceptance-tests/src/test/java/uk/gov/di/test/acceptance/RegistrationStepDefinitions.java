package uk.gov.di.test.acceptance;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ACCOUNT_CREATED;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ACCOUNT_NOT_FOUND;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CHECK_YOUR_EMAIL;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CHECK_YOUR_PHONE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CREATE_PASSWORD;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_PHONE_NUMBER;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SECURITY_CODE_INVALID;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SHARE_INFO;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;

public class RegistrationStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;
    private String phoneNumber;
    private String sixDigitCodeEmail;
    private String sixDigitCodePhone;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @Given("the registration services are running")
    public void theServicesAreRunning() {}

    @And("the new user has an invalid email format")
    public void theNewUserHasInvalidEmail() {
        emailAddress = "joe.bloggs";
        password = "password";
    }

    @When("the new user has an invalid password")
    public void theUserHasInvalidPassword() {
        password = "password";
    }

    @And("a new user has valid credentials")
    public void theNewUserHasValidCredential() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
        phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
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
        WebElement button = driver.findElement(By.id(buttonName));
        button.click();
    }

    @Then("the new user is taken to the Identity Provider Login Page")
    public void theNewUserIsTakenToTheIdentityProviderLoginPage() {
        waitForPageLoadThenValidate(SIGN_IN_OR_CREATE);
    }

    @When("the new user selects create an account")
    public void theNewUserSelectsCreateAnAccount() {
        WebElement radioCreateAccount = driver.findElement(By.id("create-account-true"));
        radioCreateAccount.click();
        findAndClickContinue();
    }

    @Then("the new user is taken to the enter your email page")
    public void theNewUserIsTakenToTheEnterYourEmailPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL);
    }

    @When("the new user selects sign in")
    public void theNewUserSelectsSignIn() {
        WebElement radioSignIn = driver.findElement(By.id("create-account-false"));
        radioSignIn.click();
        findAndClickContinue();
    }

    @Then("the new user is taken to the sign in to your account page")
    public void theNewUserIsTakenToTheSignInToYourAccountPage() {
        waitForPageLoadThenValidate(ENTER_EMAIL_EXISTING_USER);
    }

    @When("the new user enters their email address")
    public void theNewUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.clear();
        emailAddressField.sendKeys(emailAddress);
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
            WebElement sixDigitSecurityCodeField = driver.findElement(By.id("code"));
            sixDigitSecurityCodeField.clear();
            sixDigitSecurityCodeField.sendKeys(getRandomInvalidCode());
            findAndClickContinue();
            theNewUserIsShownAnErrorMessageOnTheEnterEmailPage();
        }
    }

    @When("the new user enters the six digit security code from their email")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        WebElement sixDigitSecurityCodeField = driver.findElement(By.id("code"));
        sixDigitSecurityCodeField.clear();
        sixDigitSecurityCodeField.sendKeys(sixDigitCodeEmail);
        findAndClickContinue();
    }

    @Then("the new user is taken to the security code invalid page")
    public void theNewUserIsTakenToTheSecurityCodeInvalidPage() {
        waitForPageLoadThenValidate(SECURITY_CODE_INVALID);
    }

    @Then("the new user is taken to the create your password page")
    public void theNewUserIsAskedToCreateAPassword() {
        waitForPageLoadThenValidate(CREATE_PASSWORD);
    }

    @When("the new user creates a password")
    public void theNewUserCreatesAValidPassword() {
        WebElement enterPasswordField = driver.findElement(By.id("password"));
        enterPasswordField.sendKeys(password);
        WebElement confirmPasswordField = driver.findElement(By.id("confirm-password"));
        confirmPasswordField.sendKeys(password);
        findAndClickContinue();
    }

    @Then("the new user is taken to the enter phone number page")
    public void theNewUserIsTakenToTheEnterPhoneNumberPage() {
        waitForPageLoadThenValidate(ENTER_PHONE_NUMBER);
    }

    @When("the new user enters their mobile phone number")
    public void theNewUserEntersTheirMobilePhoneNumber() {
        WebElement phoneNumberField = driver.findElement(By.id("phoneNumber"));
        phoneNumberField.sendKeys(phoneNumber);
        findAndClickContinue();
    }

    @Then("the new user is taken to the check your phone page")
    public void theNewUserIsTakenToTheCheckYourPhonePage() {
        waitForPageLoadThenValidate(CHECK_YOUR_PHONE);
    }

    @When("the new user enters the six digit security code from their phone")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirPhone() {
        WebElement sixDigitSecurityCodeField = driver.findElement(By.id("code"));
        sixDigitSecurityCodeField.sendKeys(sixDigitCodePhone);
        findAndClickContinue();
    }

    @Then("the new user is taken to the account created page")
    public void theNewUserIsTakenToTheSuccessPage() {
        waitForPageLoadThenValidate(ACCOUNT_CREATED);
    }

    @When("the new user clicks the go back to gov.uk link")
    public void theNewUserClicksTheGoBackToGovUkLink() {
        WebElement goBackLink =
                driver.findElement(
                        By.xpath(
                                "//a[text()[normalize-space() = 'Go back to GOV.UK to continue']]"));
        goBackLink.click();
    }

    @Then("the new user is taken the the share info page")
    public void theNewUsereIsTakenTheTheShareInfoPage() {
        waitForPageLoadThenValidate(SHARE_INFO);
    }

    @When("the new user agrees to share their info")
    public void theNewUserAgreesToShareTheirInfo() {
        WebElement radioShareInfoAccept = driver.findElement(By.id("share-info-accepted"));
        radioShareInfoAccept.click();
        findAndClickContinue();
    }

    @Then("the new user is returned to the service")
    public void theNewUserIsReturnedToTheService() {}

    @Then("the new user is taken to the Service User Info page")
    public void theNewUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(RP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals(RP_URL.getPort(), URI.create(driver.getCurrentUrl()).getPort());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        WebElement emailDescriptionDetails = driver.findElement(By.id("user-info-email"));
        assertEquals(emailAddress, emailDescriptionDetails.getText().trim());
    }

    @Then("the new user is shown an error message")
    public void theNewUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        WebElement emailDescriptionDetails = driver.findElement(By.id("error-summary-title"));
        assertTrue(emailDescriptionDetails.isDisplayed());
    }

    @When("the new user clicks by name {string}")
    public void theNewUserClicksByName(String buttonName) {
        WebElement button = driver.findElement(By.name(buttonName));
        button.click();
    }

    @When("the new user clicks link by href {string}")
    public void theNewUserClicksLinkByHref(String href) {
        driver.findElement(By.xpath("//a[@href=\"" + href + "\"]")).click();
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
}

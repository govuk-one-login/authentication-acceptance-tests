package uk.gov.di.test.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ACCOUNT_CREATED;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CHECK_YOUR_EMAIL;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CHECK_YOUR_PHONE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.CREATE_PASSWORD;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_PHONE_NUMBER;
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

    @After
    public void closeWebdriver() {
        super.closeWebdriver();
    }

    @Given("the registration services are running")
    public void theServicesAreRunning() {}

    @And("the new user has an invalid email format")
    public void theNewUserHasInvalidEmail() {
        emailAddress = "joe.bloggs";
        password = "password";
    }

    @And("a new user has an insecure password")
    public void theUserHasInvalidPassword() {
        emailAddress = "joe.bloggs+1@digital.cabinet-office.gov.uk";
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

    @When("the new user enters their email address")
    public void theNewUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.sendKeys(emailAddress);
        findAndClickContinue();
    }

    @Then("the new user is asked to check their email")
    public void theNewUserIsAskedToCheckTheirEmail() {
        waitForPageLoadThenValidate(CHECK_YOUR_EMAIL);
    }

    @When("the new user enters the six digit security code from their email")
    public void theNewUserEntersTheSixDigitSecurityCodeFromTheirEmail() {
        WebElement sixDigitSecurityCodeField = driver.findElement(By.id("code"));
        sixDigitSecurityCodeField.sendKeys(sixDigitCodeEmail);
        findAndClickContinue();
    }

    @Then("the new user is taken to the create your password page")
    public void theNewUserIsAskedToCreateAPassword() {
        waitForPageLoadThenValidate(CREATE_PASSWORD);
    }

    @When("the new user creates a valid password")
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
}

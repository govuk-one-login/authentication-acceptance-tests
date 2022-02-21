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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_CODE;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_EMAIL_EXISTING_USER;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.ENTER_PASSWORD;
import static uk.gov.di.test.acceptance.AuthenticationJourneyPages.SIGN_IN_OR_CREATE;

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
            new WebDriverWait(driver, 60)
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
}

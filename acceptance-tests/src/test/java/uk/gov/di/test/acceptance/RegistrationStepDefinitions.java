package uk.gov.di.test.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;

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
        String randomString = UUID.randomUUID().toString();
        emailAddress = "susan.bloggs+" + randomString + "@digital.cabinet-office.gov.uk";
        password = "passw0rd1";
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
        waitForPageLoad("Sign in or create a GOV.UK account");
        assertEquals("/sign-in-or-create", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(IDP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals("Sign in or create a GOV.UK account - GOV.UK Account", driver.getTitle());
    }

    @When("the new user enters their email address")
    public void theNewUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.sendKeys(emailAddress);
        WebElement continueButton =
                driver.findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    @Then("the new user is asked to check their email")
    public void theNewUserIsAskedToCheckTheirEmail() {
        waitForPageLoad("Check your email");
        assertEquals("/check-your-email", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Check your email - GOV.UK Account", driver.getTitle());
    }

    @Then("the new user is asked to create a password")
    public void theNewUserIsAskedToCreateAPassword() {
        assertEquals("/registration", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Create your GOV.UK account password", driver.getTitle());
    }

    @Then("the new user is asked again to create a password")
    public void theNewUserIsAskedAgainToCreateAPassword() {
        assertEquals("/registration/validate", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Create your GOV.UK account password", driver.getTitle());
    }

    @When("the new user registers their password")
    public void theNewUserEntersANewPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        WebElement passwordConfirmField = driver.findElement(By.id("password-confirm"));
        passwordConfirmField.sendKeys(password);
    }

    @Then("the new user is taken to the Success page")
    public void theNewUserIsTakenToTheSuccessPage() {
        assertEquals("/login/validate", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Sign-in to GOV.UK - Success", driver.getTitle());
    }

    @Then("the new user is taken to the successfully registered page")
    public void theNewUserIsTakenToTheSuccessfullyRegisteredPage() {
        assertEquals("/registration/validate", URI.create(driver.getCurrentUrl()).getPath());
        WebElement element = driver.findElement(By.id("successfully-created-account"));
        assertEquals("You have successfully created your GOV.UK Account", element.getText().trim());
    }

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

    private void waitForPageLoad(String titleContains) {
        new WebDriverWait(driver, DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.titleContains(titleContains));
    }
}

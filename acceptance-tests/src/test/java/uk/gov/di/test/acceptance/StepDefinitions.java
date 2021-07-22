package uk.gov.di.test.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {

    private static final String SELENIUM_URL = System.getenv().get("SELENIUM_URL");
    private static final URI IDP_URL = URI.create(
            System.getenv().getOrDefault("IDP_URL", "http://localhost:8080/")
    );
    private static final URI RP_URL = URI.create(
            System.getenv().getOrDefault("RP_URL","http://localhost:8081/")
    );

    private WebDriver driver;

    private String emailAddress;
    private String password;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(true);
        if (SELENIUM_URL == null) {
            driver = new FirefoxDriver(firefoxOptions);
        } else {
            driver = new RemoteWebDriver(new URL(SELENIUM_URL), firefoxOptions);
        }
    }

    @After
    public void closeWebdriver() {
        driver.quit();
    }

    @Given("the services are running")
    public void theServicesAreRunning() {
    }

    @And("the user has valid credentials")
    public void theUserHasValidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "password";
    }

    @And("the user has invalid credentials")
    public void theUserHasInvalidCredentials() {
        emailAddress = "joe.bloggs@digital.cabinet-office.gov.uk";
        password = "wrong-password";
    }

    @And("the user has an invalid email format")
    public void theUserHasInvalidEmail() {
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
        emailAddress = "susan.bloggs+"+randomString+"@digital.cabinet-office.gov.uk";
        password = "passw0rd1";
    }

    @When("the user visit the stub relying party")
    public void theUserVisitTheStubRelyingParty() {
        driver.get(RP_URL.toString());
    }

    @And("the user clicks {string}")
    public void theUserClick(String buttonName) {
        WebElement button = driver.findElement(By.id(buttonName));
        button.click();
    }

    @Then("the user is taken to the Identity Provider Login Page")
    public void theUserIsTakenToTheIdentityProviderLoginPage() {
        assertEquals("/enter-email", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(IDP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals("Sign in to or create your GOV.UK Account - GOV.UK Account", driver.getTitle());
    }

    @When("the user enters their email address")
    public void theUserEntersEmailAddress() {
        WebElement emailAddressField = driver.findElement(By.id("email"));
        emailAddressField.sendKeys(emailAddress);
        WebElement continueButton = driver.findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    @Then("the user is asked to check their email")
    public void theUserIsAskedToCheckTheirEmail() {
        new WebDriverWait(driver, 10).until(ExpectedConditions.titleContains("Check your email"));
        assertEquals("/check-your-email", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Check your email - GOV.UK Account", driver.getTitle());
    }

    @Then("the user is prompted for password")
    public void theUserIsPromptedForPassword() {
        assertEquals("/login", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Sign-in to GOV.UK - Password", driver.getTitle());
    }

    @Then("the user is asked to create a password")
    public void theUserIsAskedToCreateAPassword() {
        assertEquals("/registration", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Create your GOV.UK account password", driver.getTitle());
    }

    @Then("the user is asked again to create a password")
    public void theUserIsAskedAgainToCreateAPassword() {
        assertEquals("/registration/validate", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Create your GOV.UK account password", driver.getTitle());
    }

    @When("the user enters their password")
    public void theUserEntersTheirPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
    }

    @When("the user registers their password")
    public void theUserEntersANewPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        WebElement passwordConfirmField = driver.findElement(By.id("password-confirm"));
        passwordConfirmField.sendKeys(password);
    }

    @Then("the user is taken to the Success page")
    public void theUserIsTakenToTheSuccessPage() {
        assertEquals("/login/validate", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals("Sign-in to GOV.UK - Success", driver.getTitle());
    }

    @Then("the user is taken to the successfully registered page")
    public void theUserIsTakenToTheSuccessfullyRegisteredPage() {
        assertEquals("/registration/validate", URI.create(driver.getCurrentUrl()).getPath());
        WebElement element = driver.findElement(By.id("successfully-created-account"));
        assertEquals("You have successfully created your GOV.UK Account", element.getText().trim());
    }

    @Then("the user is taken to the Service User Info page")
    public void theUserIsTakenToTheServiceUserInfoPage() {
        assertEquals("/oidc/callback", URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(RP_URL.getHost(), URI.create(driver.getCurrentUrl()).getHost());
        assertEquals(RP_URL.getPort(), URI.create(driver.getCurrentUrl()).getPort());
        assertEquals("Example - GOV.UK - User Info", driver.getTitle());
        WebElement emailDescriptionDetails = driver.findElement(By.id("user-info-email"));
        assertEquals(emailAddress, emailDescriptionDetails.getText().trim());
    }

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        WebElement emailDescriptionDetails = driver.findElement(By.id("error-summary-title"));
        assertTrue(emailDescriptionDetails.isDisplayed());
    }
}

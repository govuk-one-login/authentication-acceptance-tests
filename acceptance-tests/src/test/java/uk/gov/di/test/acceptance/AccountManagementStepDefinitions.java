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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.acceptance.AccountJourneyPages.CHANGE_PASSWORD;
import static uk.gov.di.test.acceptance.AccountJourneyPages.ENTER_PASSWORD_CHANGE_PASSWORD;
import static uk.gov.di.test.acceptance.AccountJourneyPages.MANAGE_YOUR_ACCOUNT;
import static uk.gov.di.test.acceptance.AccountJourneyPages.PASSWORD_UPDATED_CONFIRMATION;

public class AccountManagementStepDefinitions extends SignInStepDefinitions {

    private String emailAddress;
    private String password;
    private String newPassword;

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @Given("the account management services are running")
    public void theServicesAreRunning() {}

    @And("the existing account management user has valid credentials")
    public void theExistingAccountManagementUserHasValidCredentials() {
        emailAddress = System.getenv().get("TEST_USER_EMAIL");
        password = System.getenv().get("TEST_USER_PASSWORD");
    }

    @When("the existing account management user navigates to account management")
    public void theExistingAccountManagementUserVisitsTheStubRelyingParty() {
        driver.get(AM_URL.toString());
    }

    @Then("the existing account management user is taken to the manage your account page")
    public void theExistingAccountManagementUserIsTakenToTheManageYourAccountPage() {
        waitForPageLoadThenValidate(MANAGE_YOUR_ACCOUNT);
    }

    @When("the existing account management user clicks link by href {string}")
    public void theUserClicksLinkByHref(String href) {
        driver.findElement(By.xpath("//a[@href=\"" + href + "\"]")).click();
    }

    @Then("the existing account management user is asked to enter their current password")
    public void theExistingAccountManagementUserIsAskedToEnterTheirCurrentPassword() {
        waitForPageLoadThenValidate(ENTER_PASSWORD_CHANGE_PASSWORD);
    }

    @When("the existing account management user enter their current password")
    public void theExistingAccountManagementUserEntersTheirCurrentPassword() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        findAndClickContinue();
    }

    @Then("the existing account management user is taken to the change password page")
    public void theExistingAccountManagementUserIsTakenToTheChangePassword() {
        waitForPageLoadThenValidate(CHANGE_PASSWORD);
    }

    @When("the existing account management user chooses a new password")
    public void theExistingAccountManagementUserChoosesANewPassword() {
        newPassword = UUID.randomUUID().toString();
    }

    @And("the existing account management user enters their new password")
    public void theExistingAccountManagementUserEntersTheirNewPassword() {
        WebElement enterPasswordField = driver.findElement(By.id("password"));
        enterPasswordField.sendKeys(newPassword);
        WebElement confirmPasswordField = driver.findElement(By.id("confirm-password"));
        confirmPasswordField.sendKeys(newPassword);
        findAndClickContinue();
    }

    @Then("the existing account management user is taken to password updated confirmation page")
    public void theExistingAccountManagementUserIsTakenToThePasswordUpdatedConfirmation() {
        waitForPageLoadThenValidate(PASSWORD_UPDATED_CONFIRMATION);
    }

    private void waitForPageLoadThenValidate(AccountJourneyPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(page.getFullTitle(), driver.getTitle());
    }
}

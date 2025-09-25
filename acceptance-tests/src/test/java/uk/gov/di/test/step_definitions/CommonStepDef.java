package uk.gov.di.test.step_definitions;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.StubStartPage;
import uk.gov.di.test.pages.StubUserInfoPage;
import uk.gov.di.test.utils.Driver;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static uk.gov.di.test.utils.Driver.getDriver;

public class CommonStepDef extends BasePage {
    public CrossPageFlows crossPageFlows;
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();
    StubStartPage stubStartPage = StubStartPage.getStubStartPage();
    private final World world;
    private String userInfoJson;

    public void setUserInfoJson(String json) {
        this.userInfoJson = json;
    }

    public CommonStepDef(World world) {
        this.crossPageFlows = new CrossPageFlows(world);
        this.world = world;
    }

    String idToken;

    @Then("the user email is not blocked to proceed with account creation")
    public void emailNotBlocked() {
        waitForPageLoad("Create your password");
    }

    @Then("the user email is accepted and taken to {string} page")
    @Then("the user is taken to the {string} page")
    public void theUserIsTakenToThePage(String pageTitle) {
        waitForPageLoad(pageTitle);
    }

    @Then("the {string} error message is displayed")
    public void theErrorMessageIsDisplayed(String expectedErrorMessage) {
        waitForThisErrorMessage(expectedErrorMessage);
    }

    @When("the user switches to Welsh language")
    public void theUserSwitchesToWelshLanguage() {
        createOrSignInPage.switchLanguageTo("Welsh");
    }

    @When("the user selects link {string}")
    public void theUserSelectsLink(String linkText) {
        selectLinkByText(linkText);
    }

    @When("the user selects {string} link")
    public void theUserSelectsTheLink(String text) {
        selectLinkByText(text);
    }

    @Then("the link {string} is not available")
    public void theLinkIsNotAvailable(String linkText) {
        assertFalse(isLinkTextDisplayedImmediately(linkText));
    }

    @Then("the link {string} is available")
    public void theLinkIsAvailable(String linkText) {
        assertTrue(isLinkTextDisplayedImmediately(linkText));
    }

    @When("the user clicks the continue button")
    public void theUserClicksTheContinueButton() {
        findAndClickContinue();
    }

    @Then("the user is returned to the service")
    @Then("the user is successfully reauthenticated and returned to the service")
    public void theUserIsReturnedToTheService() {
        stubUserInfoPage.waitForReturnToTheService();
    }

    @And("the user logs out")
    public void theUserLogsOut() {
        stubUserInfoPage.logoutOfAccount();
    }

    @And("the user's cookies are cleared")
    public void theUsersCookiesAreCleared() {
        Driver.get().manage().deleteAllCookies();
    }

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() {
        assertTrue(isErrorSummaryDisplayed());
    }

    @Then("the user is not shown any error messages")
    public void theNewUserIsNotShownAnErrorMessage() {
        switchDefaultTimeout("off");
        List<WebElement> errorFields = Driver.get().findElements(By.id("code-error"));
        switchDefaultTimeout("on");
        if (!errorFields.isEmpty()) {
            System.out.println("setup-authenticator-app error: " + errorFields.get(0));
        }
        assertTrue(errorFields.isEmpty());
    }

    @When("the user clicks the Back link")
    public void theNewUserClicksTheApplicationBackButton() {
        pressBack();
    }

    @Given("the user is signed in to their One Login account")
    @Given("the user is already signed in to their One Login account")
    @And("the user is not blocked from signing in")
    public void theUserIsAlreadySignedIn() {
        crossPageFlows.successfulSignIn();
    }

    @When("the other User attempts to use OneLogin")
    public void theOtherUserAttemptsToUseOneLogin() {
        world.userProfile = world.getOtherUserProfile();
        world.userCredentials = world.getOtherUserCredentials();
        world.userInterventions = world.getOtherUserInterventions();
        world.setUserPassword(world.getOtherUserPassword());
    }

    @When("the RP requires the user to reauthenticate")
    public void theRPRequiresTheUserToReauthenticate() {
        idToken = stubUserInfoPage.getIdToken();
        stubStartPage.reauthRequired(idToken);
    }

    @When("the user attempts to restart their reauthentication again")
    public void userAttemptsToRestartTheirReauthenticationAgain() {
        stubStartPage.reauthRequired(idToken);
    }

    @Given("the user is partially registered up to \"choose how to get security codes\" page")
    public void theUserIsPartiallyRegisteredUpToChooseHowToGetSecurityCodesPage() {
        crossPageFlows.createPartialRegisteredUpToChooseHowToGetSecurityCodesPage();
    }

    @When(
            "the user attempts to re-sign-in after partial registration and selects forgotten password link")
    public void theUserAttemptsToReSignInAfterPartialRegistrationAndSelectsForgottenPasswordLink() {
        crossPageFlows.selectForgottenPasswordLinkAndCompletePasswordChange();
    }

    @When("the user chooses {string} to get security codes and progress to set it up")
    public void theUserToGetSecurityCodesAndProgressToSetItUp(String userType) {
        crossPageFlows.setUpAuthenticationBy(userType);
    }

    @Then("the user is taken to the IPV stub page")
    public void theUserIsTakenToTheIpvStubPage() {
        waitForThisText("IPV stub");
    }

    @Then("{string} radio option selected")
    public void radioOptionSelected(String value) {
        selectRadioOptionWithText(value);
    }

    @Then("User is taken to {string}")
    public void userIsTakenTo(String pageTitle) {
        waitForPageLoad(pageTitle);
    }

    @Then("the user remain on the {string} page")
    @Then("the user email is blocked and taken to {string} page")
    @Then("the URL is present with suffix {string}")
    public void theUrlIsPresentWithSuffix(String expectedSuffix) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        Boolean urlContainsSuffix =
                wait.until(
                        driver ->
                                driver.getCurrentUrl() != null
                                        && driver.getCurrentUrl().contains(expectedSuffix));
        assertTrue(
                urlContainsSuffix,
                "Expected URL to contain suffix: "
                        + expectedSuffix
                        + " but was: "
                        + getDriver().getCurrentUrl());
    }

    @And("the user navigates to the previous page")
    @And("the user attempt to navigates to the previous page")
    public void theUserNavigatesToThePreviousPage() {
        WebDriver driver = getDriver();
        driver.navigate().back();
    }

    @Then("the {string} cookie has been set")
    public void theCookieHasBeenSet(String cookieName) {
        var cookie = Driver.get().manage().getCookieNamed(cookieName);
        assertNotNull(cookie);
    }

    @When("the user clicks the continue button without selecting any radio button option")
    public void theUserClicksTheContinueButtonWithoutSelectingAnyRadioButtonOption() {
        findAndClickContinue();
    }

    @And("the {string} is {word}")
    public void theClaimIs(String claimName, String expectedValue) throws Exception {
        if (userInfoJson == null) {
            throw new IllegalStateException(
                    "User info JSON has not been set before validating claims.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(userInfoJson);

        boolean actual = root.get(claimName).asBoolean();
        boolean expected = Boolean.parseBoolean(expectedValue);

        assertEquals("Expected '" + claimName + "' to be " + expected, expected, actual);
    }

    @And("the user info JSON is extracted from the stub page")
    public void extractUserInfoJsonFromStubPage() {
        WebDriver driver = Driver.get();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String json;

        try {
            WebElement element =
                    driver.findElement(
                            By.xpath(
                                    "//dt[normalize-space()='User Info' or normalize-space()='User Info response']/following-sibling::dd[1]"));
            json = element.getText();
            System.out.println("[INFO] Extracted JSON from dd next to label.");
        } catch (NoSuchElementException e) {
            System.out.println(
                    "[WARN] Could not find dd with label — trying pretty-json fallback...");

            List<WebElement> prettyJsonElements =
                    driver.findElements(By.cssSelector("#main-content pretty-json"));

            if (!prettyJsonElements.isEmpty()) {
                json =
                        (String)
                                js.executeScript(
                                        "const el = document.querySelector('#main-content pretty-json');"
                                                + "const shadow = el.shadowRoot;"
                                                + "return shadow.querySelector('.container').innerText;");
                System.out.println("[INFO] Extracted JSON from pretty-json component.");
            } else {
                System.out.println("===== PAGE SOURCE =====");
                System.out.println(driver.getPageSource());
                System.out.println("=======================");
                throw new RuntimeException(
                        "Could not find JSON in dd after 'User Info' or 'User Info response', or pretty-json.");
            }
        }

        setUserInfoJson(json);
    }

    @And("the user clicks on {string} link")
    public void TheUserClicksOnLink(String text) {
        selectLinkByText(text);
    }
}

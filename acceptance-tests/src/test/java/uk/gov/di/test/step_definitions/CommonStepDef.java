package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.StubStartPage;
import uk.gov.di.test.pages.StubUserInfoPage;
import uk.gov.di.test.utils.Driver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonStepDef extends BasePage {
    public CrossPageFlows crossPageFlows;
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();
    StubStartPage stubStartPage = StubStartPage.getStubStartPage();
    private final World world;

    public CommonStepDef(World world) {
        this.crossPageFlows = new CrossPageFlows(world);
        this.world = world;
    }

    String idToken;

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
    public void theUserSelectsProblemsWithTheCode(String text) {
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
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.UserInformationPage;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonStepDef extends BasePage {
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public RpStubPage rpStubPage = new RpStubPage();
    public UserInformationPage userInformationPage = new UserInformationPage();
    public CrossPageFlows crossPageFlows = new CrossPageFlows();

    @Then("the user is taken to the {string} page")
    public void theUserIsTakenToThePage(String pageTitle) throws MalformedURLException {
        waitForPageLoad(pageTitle);
    }

    @Then("the {string} error message is displayed")
    public void theErrorMessageIsDisplayed(String expectedErrorMessage)
            throws MalformedURLException {
        waitForThisErrorMessage(expectedErrorMessage);
    }

    @When("the user switches to Welsh language")
    public void theUserSwitchesToWelshLanguage() throws MalformedURLException {
        createOrSignInPage.switchLanguageTo("Welsh");
    }

    @When("the user selects link {string}")
    public void theUserSelectsLink(String linkText) throws MalformedURLException {
        selectLinkByText(linkText);
    }

    @When("the user selects {string} link")
    public void theUserSelectsProblemsWithTheCode(String text) throws MalformedURLException {
        selectLinkByText(text);
    }

    @Then("the link {string} is not available")
    public void theLinkIsNotAvailable(String linkText) throws MalformedURLException {
        assertFalse(isLinkTextDisplayedImmediately(linkText));
    }

    @Then("the link {string} is available")
    public void theLinkIsAvailable(String linkText) throws MalformedURLException {
        assertTrue(isLinkTextDisplayedImmediately(linkText));
    }

    @When("the user clicks the continue button")
    public void theUserClicksTheContinueButton() throws MalformedURLException {
        findAndClickContinue();
    }

    @Then("the user is returned to the service")
    @Then("the user is successfully reauthenticated and returned to the service")
    public void theUserIsReturnedToTheService() throws MalformedURLException {
        waitForPageLoad("Example - GOV.UK - User Info");
    }

    @And("the user logs out")
    public void theUserLogsOut() throws MalformedURLException {
        findAndClickButtonByText("Log out");
        waitForPageLoad("Signed out");
    }

    @Then("the user is shown an error message")
    public void theUserIsShownAnErrorMessageOnTheEnterEmailPage() throws MalformedURLException {
        assertTrue(isErrorSummaryDisplayed());
    }

    @Then("the user is not shown any error messages")
    public void theNewUserIsNotShownAnErrorMessage() throws MalformedURLException {
        switchDefaultTimeout("off");
        List<WebElement> errorFields = Driver.get().findElements(By.id("code-error"));
        switchDefaultTimeout("on");
        if (!errorFields.isEmpty()) {
            System.out.println("setup-authenticator-app error: " + errorFields.get(0));
        }
        assertTrue(errorFields.isEmpty());
    }

    @When("the user clicks the Back link")
    public void theNewUserClicksTheApplicationBackButton() throws MalformedURLException {
        pressBack();
    }

    @Given("the {string} user {string} is already signed in to their One Login account")
    public void theUserAlreadySignedIn(String userType, String emailAddress)
            throws MalformedURLException {
        crossPageFlows.successfulSignIn(userType, emailAddress);
    }

    @When("the RP requires the user to reauthenticate")
    public void theRPRequiresTheUserToReauthenticate() throws MalformedURLException {
        String idToken = userInformationPage.getIdToken();
        rpStubPage.reauthRequired(idToken);
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.FinishCreatingYourAccountPage;
import uk.gov.di.test.pages.LockoutPage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.UserInformationPage;
import uk.gov.di.test.pages.YouHaveAGOVUKOneLoginPage;
import uk.gov.di.test.utils.Driver;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class LockoutsStepDef extends BasePage {
    private final World world;

    public EnterYourPasswordPage enterYourPasswordPage;
    public CrossPageFlows crossPageFlows;
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    public ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    RpStubPage rpStubPage = new RpStubPage();
    public FinishCreatingYourAccountPage finishCreatingYourAccountPage =
            new FinishCreatingYourAccountPage();
    public YouHaveAGOVUKOneLoginPage youHaveAGOVUKOneLoginPage = new YouHaveAGOVUKOneLoginPage();
    public LockoutPage lockoutPage = new LockoutPage();
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public UserInformationPage userInformationPage = new UserInformationPage();

    public LockoutsStepDef(World world) {
        this.world = world;
        this.enterYourPasswordPage = new EnterYourPasswordPage(world);
        this.crossPageFlows = new CrossPageFlows(world);
    }

    // Error Screen Parsing

    @Then("the non blocking {string} lockout screen is displayed")
    @Then("the {string} lockout screen is displayed")
    public void theLockoutScreenIsDisplayed(String titleText) {
        waitForPageLoad(titleText);
    }

    @And("the lockout reason is {string}")
    public void theLockoutReasonIs(String input) {
        assertTrue(
                lockoutPage
                        .getLockoutScreenText()
                        .contains(String.format("because %s", input)));
    }

    @ParameterType("hour|hours|minute|minutes|second|seconds")
    public String timeUnit(String timeUnit) {
        return timeUnit;
    }

    @And("the lockout duration is {int} {timeUnit}")
    public void theLockoutDurationIs(int time, String timeUnit) {
        assertTrue(lockoutPage.getLockoutScreenText().contains(String.format("%s %s", time, timeUnit)));
    }

    @Then("no lockout is triggered and the user remains on the {string} page")
    public void noLockoutIsTriggered(String pageTitle) {
        assertTrue(Objects.requireNonNull(Driver.get().getTitle()).contains(pageTitle));
    }

    // Utility

    @When("the user selects to get a new code")
    public void whenTheUserSelectsToGetANewCode() {
        selectLinkByText("get a new code");
    }

    @Then("the user is able to complete account creation")
    public void theUserIsAbleToCompleteAccountCreation() {
        waitForPageLoad("Get security code");
        crossPageFlows.completeAccountCreationAfterNewEmailCode();
    }

    // USER BLOCKED FOR TOO MANY INCORRECT PASSWORDS CAN RESET THEIR PASSWORD AND BLOCK IS LIFTED

    @Given("the user {string} is on the blocked page for entering too many incorrect passwords")
    public void theUserIsOnBlockedPageForEnteringTooManyIncorrectPasswords(String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.useDefaultOptionsAndContinue();
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.enterIncorrectPasswordNumberOfTimes(6);
        waitForPageLoad("You entered the wrong password too many times");
    }

    @Then("the user resets their password")
    public void theUserResetsTheirPassword() {
        selectLinkByText("reset your password");
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("Reset your password");
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                NEW_VALID_PASSWORD, NEW_VALID_PASSWORD);
        waitForPageLoad("Example - GOV.UK - User Info");
    }

    @And("the block is lifted and the user {string} can login")
    public void theBlockIsLiftedAndTheUserCanLogin(String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.useDefaultOptionsAndContinue();
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.enterNewPasswordAndContinue();
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("Example - GOV.UK - User Info");
    }
}

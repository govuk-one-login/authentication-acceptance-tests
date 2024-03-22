package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.FinishCreatingYourAccountPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.YouHaveAGOVUKOneLoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockoutsStepDef extends BasePage {
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public EnterYourEmailAddressPage enterYourEmailAddressPage = new EnterYourEmailAddressPage();
    public EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();
    public ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    public CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    RpStubPage rpStubPage = new RpStubPage();
    public FinishCreatingYourAccountPage finishCreatingYourAccountPage =
            new FinishCreatingYourAccountPage();
    public YouHaveAGOVUKOneLoginPage youHaveAGOVUKOneLoginPage = new YouHaveAGOVUKOneLoginPage();

    @When(
            "the user {string} with a lockout for wrong email security codes reattempts to change their password during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many email security codes reattempts to change their password during the lockout period")
    public void theUserWithWrongEmailCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
    }

    @When(
            "the user {string} with a lockout for wrong sms security codes reattempts to change their password during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security codes reattempts to change their password during the lockout period")
    public void theUserWithWrongSMSCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
    }

    @When(
            "the user {string} with a lockout for too many incorrect auth app codes reattempts to change their password during the lockout period")
    public void theUserWithWrongAuthAppCodeBlockReattemptsToChangeTheirPasswordDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.clickForgottenPasswordLink();
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
        enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                .enterCorrectAuthAppCodeAndContinue(
                        System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET"));
    }

    @When(
            "the user {string} with a lockout for requesting too many email security codes attempts to change the way they get security codes during the lockout period")
    @When(
            "the user {string} with a lockout for too many incorrect email security codes attempts to change the way they get security codes during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManyEmailSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
        checkYourPhonePage.clickProblemsWithTheCodeLink();
        selectLinkByText("change how you get security codes");
    }

    @When(
            "the user {string} with a lockout for requesting too many sms security codes attempts to change the way they get security codes during the lockout period")
    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to change the way they get security codes during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManySmsSecurityCodesAttemptsToChangeHowTheyGetSecurityCodes(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
        checkYourPhonePage.clickProblemsWithTheCodeLink();
        selectLinkByText("change how you get security codes");
        checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue("07803507860");
    }

    @When(
            "the user {string} with a lockout for too many incorrect passwords attempts to sign in during the lockout period")
    public void theUserWithALockoutForTooManyIncorrectPasswordsAttemptsToSignInDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
    }

    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to sign in during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security code resends attempts to sign in during the lockout period")
    public void theUserWithALockoutForTooManyIncorrectSmsCodesAttemptsToSignInDuringLockoutPeriod(
            String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    }

    @When(
            "the user {string} with a lockout for too many incorrect auth app codes reattempts to sign in during the lockout period")
    public void
            theUserWithALockoutForTooManyIncorrectAuthAppCodesAttemptsToSignInDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(emailAddress));
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
    }

    @When(
            "the user {string} with a lockout for requesting too many email security codes attempts to create account during the lockout period")
    public void
            theUserWithALockoutForRequestingTooManyEmailSecurityCodesAttemptsToCreateAccountDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressPage.enterEmailAddressAndContinue(System.getenv().get(emailAddress));
    }

    @When(
            "the user {string} with a lockout for too many incorrect sms security codes attempts to create account during the lockout period")
    @When(
            "the user {string} with a lockout for requesting too many sms security codes attempts to create account during the lockout period")
    public void
            theUserWithALockoutForTooManyIncorrectSmsCodesAttemptsToCreateAccountDuringLockoutPeriod(
                    String emailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("default");
        setAnalyticsCookieTo(false);
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressPage.enterEmailAddressAndContinue(System.getenv().get(emailAddress));
        youHaveAGOVUKOneLoginPage.enterPasswordAndContinue(
                System.getenv().get("TEST_USER_PASSWORD"));
        finishCreatingYourAccountPage.selectAuthMethodAndContinue("text message");
        // checkYourEmailPage.enterEmailCodeAndContinue(System.getenv().get("TEST_USER_EMAIL_CODE"));
        // createYourPasswordPage.enterBothPasswordsAndContinue("new-password1", "new-password1");
        // chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
    }

    @Then("the 2hr You entered the wrong security code too many times screen is displayed")
    public void theLockoutScreenForWrongSecurityCodesIsDisplayed() {
        waitForPageLoad("You entered the wrong security code too many times");
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    // YOU ENTERED WRONG SECURITY CODES

    // @Then("the 15min lockout screen for too many wrong security codes is displayed")
    @Then("the 15min You entered the wrong security code too many times screen is displayed")
    public void the15minLockoutScreenForWrongSecurityCodesIsDisplayed() {
        waitForPageLoad("You entered the wrong security code too many times");
        // assertTrue(
        //        driver.findElement(By.xpath("//*[contains(text(), 'wrong security code')]"))
        //                .isDisplayed());
        assertTrue(
                driver.findElement(By.xpath("//*[contains(text(), '15 minutes')]")).isDisplayed());
        takeScreenshot(scenario);
        //        String text =
        //                driver.findElement(By.cssSelector("#main-content
        // .govuk-grid-column-two-thirds"))
        //                        .getText();
        //        assertTrue(text.contains("You need to wait 15 minutes"));
        //        takeScreenshot(scenario);
    }

    // YOU CANNOT GET A NEW SECURITY CODE

    // @Then("the 15min cannot sign in lockout screen for wrong security codes is displayed")
    @Then("the 15min You cannot get a new security code at the moment screen is displayed")
    public void the15minCannotSignInLockoutScreenIsDisplayed() {
        waitForPageLoad("You cannot get a new security code at the moment");
        assertTrue(
                driver.findElement(By.xpath("//*[contains(text(), 'wrong security code')]"))
                        .isDisplayed());
        assertTrue(
                driver.findElement(By.xpath("//*[contains(text(), '15 minutes')]")).isDisplayed());
        takeScreenshot(scenario);
        //        String text =
        //                driver.findElement(By.cssSelector("#main-content
        // .govuk-grid-column-two-thirds"))
        //                        .getText();
        //        assertTrue(
        //                text.contains(
        //                        "This is because you entered the wrong security code too many
        // times"));
        //        assertTrue(text.contains("15 minutes"));
    }

    // YOU ASKED TO RESEND SECURITY CODES

    // @Then("the 2hr lockout screen for requesting too many security code resends is displayed")
    @Then("the 2hr You asked to resend the security code too many times screen is displayed")
    public void theLockoutScreenForRequestingTooManySecurityCodesIsDisplayed() {
        waitForPageLoad("You asked to resend the security code too many times");
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    //    @Then(
    //            "the 2hr lockout screen for requesting too many security code resends during sign
    // in is displayed")
    //    public void theLockoutScreenForRequestingTooManySecurityCodesDuringSignInIsDisplayed() {
    //        waitForPageLoad("You asked to resend the security code too many times");
    //        String text =
    //                driver.findElement(By.cssSelector("#main-content
    // .govuk-grid-column-two-thirds"))
    //                        .getText();
    //        assertTrue(text.contains("2 hours"));
    //        takeScreenshot(scenario);
    //    }

    // CANNOT CREATE
    @Then(
            "the 2hr You cannot create a GOV.UK One Login at the moment screen for requesting too many security code resends is displayed")
    public void
            theCannotCreateAccountLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
        waitForPageLoad("You cannot create a GOV.UK One Login at the moment");
        assertTrue(getLockoutScreenText().contains("resend the security code"));
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    // CANNOT SIGN IN
    @Then("the 2hr You cannot sign in at the moment screen for wrong password is displayed")
    public void the2hrCannotSignInScreenForTooManyIncorrectPasswordsIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(getLockoutScreenText().contains("wrong password"));
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    @Then(
            "the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed")
    public void theCannotSignInLockoutScreenForRequestingTooManySecurityCodeResendsIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(getLockoutScreenText().contains("resend the security code"));
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    @Then("the 2hr You cannot sign in at the moment screen for wrong security codes is displayed")
    public void theCannotSignInLockoutScreenIsDisplayed() {
        waitForPageLoad("You cannot sign in at the moment");
        assertTrue(getLockoutScreenText().contains("wrong security code"));
        assertTrue(getLockoutScreenText().contains("2 hours"));
        takeScreenshot(scenario);
    }

    @Then("no lockout is triggered and the user remains on the {string} page")
    public void noLockoutIsTriggered(String pageTitle) {
        assertTrue(driver.getTitle().contains(pageTitle));
        takeScreenshot(scenario);
    }

    // @Then("the 2hr lockout screen for too many incorrect passwords during sign in is displayed")
    @Then("the 2hr You entered the wrong password too many times screen is displayed")
    public void the2hrLockoutScreenForTooManyIncorrectPasswordsDuringSignInIsDisplayed() {
        waitForPageLoad("You entered the wrong password too many times");
        assertTrue(getLockoutScreenText().contains("2 hours"));
        assertTrue(getLockoutScreenText().contains("wrong password"));
        takeScreenshot(scenario);
    }

    public String getLockoutScreenText() {
        return driver.findElement(By.cssSelector("#main-content .govuk-grid-column-two-thirds"))
                .getText();
    }
}

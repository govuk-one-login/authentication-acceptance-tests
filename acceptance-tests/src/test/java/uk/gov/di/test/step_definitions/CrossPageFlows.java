package uk.gov.di.test.step_definitions;

import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.ChooseHowToGetSecurityCodesPage;
import uk.gov.di.test.pages.CreateOrSignInPage;
import uk.gov.di.test.pages.CreateYourPasswordPage;
import uk.gov.di.test.pages.EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage;
import uk.gov.di.test.pages.EnterYourEmailAddressToSignInPage;
import uk.gov.di.test.pages.EnterYourMobilePhoneNumberPage;
import uk.gov.di.test.pages.EnterYourPasswordPage;
import uk.gov.di.test.pages.GetSecurityCodePage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.pages.UserInformationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class CrossPageFlows extends BasePage {
    private String authAppSecretKey;
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public RpStubPage rpStubPage = new RpStubPage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
    public EnterYourPasswordPage enterYourPasswordPage = new EnterYourPasswordPage();
    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage =
                    new EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage();
    public GetSecurityCodePage getSecurityCodePage = new GetSecurityCodePage();
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();
    public ChooseHowToGetSecurityCodesPage chooseHowToGetSecurityCodesPage =
            new ChooseHowToGetSecurityCodesPage();
    public SetUpAnAuthenticatorAppPage setUpAnAuthenticatorAppPage =
            new SetUpAnAuthenticatorAppPage();
    public EnterYourMobilePhoneNumberPage enterYourMobilePhoneNumberPage =
            new EnterYourMobilePhoneNumberPage();
    public ResetYourPasswordPage resetYourPasswordPage = new ResetYourPasswordPage();
    public CreateYourPasswordPage createYourPasswordPage = new CreateYourPasswordPage();
    public UserInformationPage userInformationPage = new UserInformationPage();

    public void requestPhoneSecurityCodeResendNumberOfTimes(Integer numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            checkYourPhonePage.clickProblemsWithTheCodeLink();
            checkYourPhonePage.clickSendTheCodeAgainLink();
            waitForPageLoad("Get security code");
            getSecurityCodePage.pressGetSecurityCodeButton();
            System.out.println(
                    "Code request count: "
                            + (i + 1)
                            + " ("
                            + (i + 2)
                            + " including code sent on initial entry to the Check Your Phone page)");
        }
    }

    public void requestEmailOTPCodeResendNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            checkYourEmailPage.waitForPage();
            checkYourEmailPage.requestResendOfEmailOTPCode();
            getSecurityCodePage.waitForPage();
            getSecurityCodePage.pressGetSecurityCodeButton();
            System.out.println(
                    "Code request count: "
                            + (index + 1)
                            + " ("
                            + (index + 2)
                            + " including code sent on initial entry to the Check Your Email page)");
        }
    }

    public void successfulSignIn(String userType, String userEmailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(userEmailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
        if (userType.equalsIgnoreCase("sms")) {
            // sms steps
            waitForPageLoad("Check your phone");
            checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        } else {
            // auth app steps
            waitForPageLoad("Enter the 6 digit security code shown in your authenticator app");
            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                    .enterCorrectAuthAppCodeAndContinue(
                            System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET"));
        }
        waitForPageLoad("Example - GOV.UK - User Info");
    }

    public void smsUserChangeHowGetSecurityCodesToAuthApp() {
        String authAppSecretKey;
        selectLinkByText("Problems with the code?");
        selectLinkByText("change how you get security codes");
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("How do you want to get security codes?");
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("auth app");
        waitForPageLoad("Set up an authenticator app");
        setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
        authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
        setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
        waitForPageLoad("You’ve changed how you get security codes");
        findAndClickContinue();
    }

    public void authAppUserChangeHowGetSecurityCodesToSms() {
        selectLinkByText("I do not have access to the authenticator app");
        selectLinkByText("change how you get security codes");
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("How do you want to get security codes?");
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        waitForPageLoad("Enter your mobile phone number");
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("You’ve changed how you get security codes");
        findAndClickContinue();
    }

    public void smsUserChangesPassword() {
        enterYourPasswordPage.clickForgottenPasswordLink();
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Check your phone");
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("Reset your password");
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                NEW_VALID_PASSWORD, NEW_VALID_PASSWORD);
    }

    public void completeAccountCreationAfterNewEmailCode() {
        getSecurityCodePage.waitForPage();
        getSecurityCodePage.pressGetSecurityCodeButton();
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        String pw = System.getenv().get("TEST_USER_PASSWORD");
        createYourPasswordPage.enterBothPasswordsAndContinue(pw, pw);
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                System.getenv().get("TEST_USER_PHONE_NUMBER"));
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("You’ve created your GOV.UK One Login");
        findAndClickContinue();
        waitForPageLoad("User Info");
        userInformationPage.logoutOfAccount();
    }

    public void createPartialRegisteredUpToChooseHowToGetSecurityCodesPage(
            String userEmailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(userEmailAddress));
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Create your password");
        String passwordVal = System.getenv().get("TEST_USER_PASSWORD");
        createYourPasswordPage.enterBothPasswordsAndContinue(passwordVal, passwordVal);
        waitForPageLoad("Choose how to get security codes");
    }

    public void selectForgottenPasswordLinkAndCompletePasswordChange(String userEmailAddress) {
        rpStubPage.goToRpStub();
        rpStubPage.selectRpOptionsByIdAndContinue("");
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
                System.getenv().get(userEmailAddress));
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.clickForgottenPasswordLink();
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Reset your password");
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(
                NEW_VALID_PASSWORD, NEW_VALID_PASSWORD);
    }

    public void setUpAuthenticationBy(String userType) {
        switch (userType.toLowerCase()) {
            case "text message":
                chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
                waitForPageLoad("Enter your mobile phone number");
                enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                        System.getenv().get("TEST_USER_PHONE_NUMBER"));
                waitForPageLoad("Check your phone");
                checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
                break;

            case "auth app":
                chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("auth app");
                waitForPageLoad("Set up an authenticator app");

                authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
                setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
                authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
                assertEquals(32, setUpAnAuthenticatorAppPage.getSecretFieldText().length());

                if (authAppSecretKey == null) {
                    authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
                }
                setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
                break;

            default:
                throw new RuntimeException("Invalid method type: " + userType);
        }
    }
}

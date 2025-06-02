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
import uk.gov.di.test.pages.ReenterYourSignInDetailsToContinuePage;
import uk.gov.di.test.pages.ResetYourPasswordPage;
import uk.gov.di.test.pages.SetUpAnAuthenticatorAppPage;
import uk.gov.di.test.pages.StubStartPage;
import uk.gov.di.test.pages.StubUserInfoPage;
import uk.gov.di.test.services.UserLifecycleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER;

public class CrossPageFlows extends BasePage {

    private final World world;
    private String authAppSecretKey;

    public EnterYourPasswordPage enterYourPasswordPage;
    public CreateOrSignInPage createOrSignInPage = new CreateOrSignInPage();
    public StubStartPage stubStartPage = StubStartPage.getStubStartPage();
    public EnterYourEmailAddressToSignInPage enterYourEmailAddressToSignInPage =
            new EnterYourEmailAddressToSignInPage();
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
    public StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();
    public ReenterYourSignInDetailsToContinuePage reenterYourSignInDetailsToContinuePage;

    public CrossPageFlows(World world) {
        this.world = world;
        this.enterYourPasswordPage = new EnterYourPasswordPage(world);
        reenterYourSignInDetailsToContinuePage = new ReenterYourSignInDetailsToContinuePage(world);
    }

    public void requestPhoneSecurityCodeResendNumberOfTimes(
            Integer numberOfTimes, Boolean isReauth) {
        for (int i = 0; i < numberOfTimes; i++) {
            checkYourPhonePage.clickProblemsWithTheCodeLink();
            checkYourPhonePage.clickSendTheCodeAgainLink();
            waitForPageLoad("Get security code");
            if (isReauth) {
                waitForThisText("you will be signed out");
            }
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

    private void enterMfaCodeAndContinue() {
        switch (world.getMfaType()) {
            case SMS:
                waitForPageLoad("Check your phone");
                checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
                break;
            case APP:
                waitForPageLoad("Enter the 6 digit security code shown in your authenticator app");
                checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
                enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
                        .enterCorrectAuthAppCodeAndContinue(world.getAuthAppSecret());
                break;
            default:
                throw new RuntimeException("Invalid MFA type: " + world.getMfaType());
        }
    }

    public void successfulSignIn() {
        stubStartPage.goToRpStub();
        stubStartPage.useDefaultOptionsAndContinue();
        setAnalyticsCookieTo(false);
        createOrSignInPage.waitForPage();
        createOrSignInPage.clickSignInButton();
        enterYourEmailAddressToSignInPage.waitForPage();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(world.getUserEmailAddress());
        enterYourPasswordPage.waitForPage();
        enterYourPasswordPage.enterPasswordAndContinue(world.getUserPassword());
        enterMfaCodeAndContinue();
        stubUserInfoPage.waitForReturnToTheService();
    }

    public void successfulSignIn(String userType, String userEmailAddress) {
        throw new RuntimeException("Need to implement new-style user flows for this");
        //        stubPage.goToRpStub();
        //        stubPage.selectRpOptionsByIdAndContinue(null);
        //        setAnalyticsCookieTo(false);
        //        waitForPageLoad("Create your GOV.UK One Login or sign in");
        //        createOrSignInPage.clickSignInButton();
        //        waitForPageLoad("Enter your email address to sign in");
        //        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(
        //                System.getenv().get(userEmailAddress));
        //        waitForPageLoad("Enter your password");
        //
        // enterYourPasswordPage.enterPasswordAndContinue(System.getenv().get("TEST_USER_PASSWORD"));
        //        if (userType.equalsIgnoreCase("sms")) {
        //            // sms steps
        //            waitForPageLoad("Check your phone");
        //            checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        //        } else {
        //            // auth app steps
        //            waitForPageLoad("Enter the 6 digit security code shown in your authenticator
        // app");
        //            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
        //                    .enterCorrectAuthAppCodeAndContinue(
        //
        // Constants.AUTH_APP_SECRET);
        //        }
        //        rpStubPage.waitForReturnToTheService();
    }

    public void successfulReauth() {
        stubUserInfoPage.waitForReturnToTheService();
        String idToken = stubUserInfoPage.getIdToken();
        stubStartPage.reauthRequired(idToken);
        reenterYourSignInDetailsToContinuePage.waitForPage();
        reenterYourSignInDetailsToContinuePage.enterEmailAddressAndContinue(
                world.getUserEmailAddress());
        enterYourPasswordPage.waitForPage();
        enterYourPasswordPage.enterCorrectPasswordAndContinue();
        enterMfaCodeAndContinue();
        stubUserInfoPage.waitForReturnToTheService();
    }

    public void successfulReauth(String userType, String userEmailAddress) {
        throw new RuntimeException("Need to implement new-style user flows for this");
        //        // assumes that the signed in page is currently displayed
        //        rpStubPage.waitForReturnToTheService();
        //        String idToken = userInformationPage.getIdToken();
        //        stubPage.reauthRequired(idToken);
        //        waitForPageLoad("Enter your sign in details for GOV.UK One Login again");
        //        // enter original email address
        //        reenterYourSignInDetailsToContinuePage.enterEmailAddressAndContinue(
        //                System.getenv().get(userEmailAddress));
        //        // enter correct password
        //        enterYourPasswordPage.enterCorrectPasswordAndContinue();
        //        // enter correct otp
        //        if (userType.equalsIgnoreCase("sms")) {
        //            // sms steps
        //            waitForPageLoad("Check your phone");
        //            checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        //        } else {
        //            // auth app steps
        //            waitForPageLoad("Enter the 6 digit security code shown in your authenticator
        // app");
        //            enterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage
        //                    .enterCorrectAuthAppCodeAndContinue(
        //
        // Constants.AUTH_APP_SECRET);
        //        }
        //        rpStubPage.waitForReturnToTheService();
    }

    public void smsUserChangeHowGetSecurityCodesToAuthApp() {
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
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(UK_MOBILE_PHONE_NUMBER);
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
        String newPassword = UserLifecycleService.generateValidPassword();
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(newPassword, newPassword);
    }

    public void completeAccountCreationAfterNewEmailCode() {
        getSecurityCodePage.waitForPage();
        getSecurityCodePage.pressGetSecurityCodeButton();
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();

        String userPassword = world.getUserPassword();
        createYourPasswordPage.enterBothPasswordsAndContinue(userPassword, userPassword);
        chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
        enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(world.getUserPhoneNumber());
        checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
        waitForPageLoad("You’ve created your GOV.UK One Login");
        findAndClickContinue();
        stubUserInfoPage.waitForReturnToTheService();
        stubUserInfoPage.logoutOfAccount();
    }

    public void createPartialRegisteredUpToChooseHowToGetSecurityCodesPage() {
        stubStartPage.useRpStubAndWaitForPage("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickCreateAGovUkOneLoginButton();
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(world.getUserEmailAddress());
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Create your password");
        String userPassword = world.getUserPassword();
        createYourPasswordPage.enterBothPasswordsAndContinue(userPassword, userPassword);
        waitForPageLoad("Choose how to get security codes");
    }

    public void selectForgottenPasswordLinkAndCompletePasswordChange() {
        stubStartPage.goToRpStub();
        stubStartPage.useDefaultOptionsAndContinue();
        setAnalyticsCookieTo(false);
        waitForPageLoad("Create your GOV.UK One Login or sign in");
        createOrSignInPage.clickSignInButton();
        waitForPageLoad("Enter your email address to sign in");
        enterYourEmailAddressToSignInPage.enterEmailAddressAndContinue(world.getUserEmailAddress());
        waitForPageLoad("Enter your password");
        enterYourPasswordPage.clickForgottenPasswordLink();
        waitForPageLoad("Check your email");
        checkYourEmailPage.enterCorrectEmailCodeAndContinue();
        waitForPageLoad("Reset your password");
        String newPassword = UserLifecycleService.generateValidPassword();
        resetYourPasswordPage.enterPasswordResetDetailsAndContinue(newPassword, newPassword);

        world.setUserPassword(newPassword);
    }

    public void setUpAuthenticationBy(String userType) {
        switch (userType.toLowerCase()) {
            case "text message":
                chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("text message");
                waitForPageLoad("Enter your mobile phone number");
                enterYourMobilePhoneNumberPage.enterUkPhoneNumberAndContinue(
                        world.getUserPhoneNumber());
                waitForPageLoad("Check your phone");
                checkYourPhonePage.enterCorrectPhoneCodeAndContinue();
                break;

            case "auth app":
                chooseHowToGetSecurityCodesPage.selectAuthMethodAndContinue("auth app");
                waitForPageLoad("Set up an authenticator app");

                setUpAnAuthenticatorAppPage.iCannotScanQrCodeClick();
                authAppSecretKey = setUpAnAuthenticatorAppPage.getSecretFieldText();
                assertEquals(32, authAppSecretKey.length());

                setUpAnAuthenticatorAppPage.enterCorrectAuthAppCodeAndContinue(authAppSecretKey);
                break;

            default:
                throw new RuntimeException("Invalid method type: " + userType);
        }
    }

    public void requestAuthSecurityCodeResendNumberOfTimes(
                Integer numberOfTimes, Boolean isReauth) {
            for (int i = 0; i < numberOfTimes; i++) {
                checkYourPhonePage.clickProblemsWithTheCodeLink();
                checkYourPhonePage.clickSendTheCodeAgainLink();
                waitForPageLoad("Get security code");
                if (isReauth) {
                    waitForThisText("you will be signed out");
                }
                getSecurityCodePage.pressGetSecurityCodeButton();
                System.out.println(
                        "Code request count: "
                                + (i + 1)
                                + " ("
                                + (i + 2)
                                + " including code sent on initial entry to the Check Your Auth page)");
            }
    }
}

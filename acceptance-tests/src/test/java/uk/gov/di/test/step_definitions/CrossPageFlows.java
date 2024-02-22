package uk.gov.di.test.step_definitions;

import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.CheckYourEmailPage;
import uk.gov.di.test.pages.CheckYourPhonePage;
import uk.gov.di.test.pages.GetSecurityCodePage;

public class CrossPageFlows extends BasePage {

    public CheckYourPhonePage checkYourPhonePage = new CheckYourPhonePage();
    public GetSecurityCodePage getSecurityCodePage = new GetSecurityCodePage();
    public CheckYourEmailPage checkYourEmailPage = new CheckYourEmailPage();

    public void requestPhoneSecurityCodeResendNumberOfTimes(Integer numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            checkYourPhonePage.clickProblemsWithTheCodeLink();
            checkYourPhonePage.clickSendTheCodeAgainLink();
            waitForPageLoad("Get security code");
            getSecurityCodePage.pressGetSecurityCodeButton();
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
}

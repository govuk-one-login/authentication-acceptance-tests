package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class CheckYourEmailPage extends BasePage {

    By emailCodeField = By.id("code");

    public void enterEmailCode(String emailCode) {
        clearFieldAndEnter(emailCodeField, emailCode);
    }

    public void enterEmailCodeAndContinue(String code) {
        enterEmailCode(code);
        findAndClickContinue();
    }

    public void requestResendOfEmailOTPCode() {
        selectLinkByText("Problems with the code?");
        selectLinkByText("send the code again");
    }

    public void waitForPage() {
        waitForPageLoad("Check your email");
    }
}

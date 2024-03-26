package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import static uk.gov.di.test.utils.Constants.INCORRECT_EMAIL_OTP_CODE;

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

    public void enterIncorrectEmailOTPNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPage();
            enterEmailCodeAndContinue(INCORRECT_EMAIL_OTP_CODE);
            System.out.println("Incorrect code entry count: " + (index + 1));
        }
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import java.net.MalformedURLException;

import static uk.gov.di.test.utils.Constants.INCORRECT_EMAIL_OTP_CODE;

public class CheckYourEmailPage extends BasePage {

    By emailCodeField = By.id("code");

    public void enterEmailCode(String emailCode) throws MalformedURLException {
        clearFieldAndEnter(emailCodeField, emailCode);
    }

    public void enterCorrectEmailCodeAndContinue() throws MalformedURLException {
        enterEmailCode(System.getenv().get("TEST_USER_EMAIL_CODE"));
        findAndClickContinue();
    }

    public void enterIncorrectOTPCodeAndContinue() throws MalformedURLException {
        enterEmailCode(INCORRECT_EMAIL_OTP_CODE);
        findAndClickContinue();
    }

    public void requestResendOfEmailOTPCode() throws MalformedURLException {
        selectLinkByText("Problems with the code?");
        selectLinkByText("send the code again");
    }

    public void waitForPage() throws MalformedURLException {
        waitForPageLoad("Check your email");
    }

    public void enterIncorrectEmailOTPNumberOfTimes(Integer numberOfTimes)
            throws MalformedURLException {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPage();
            enterIncorrectOTPCodeAndContinue();
            System.out.println("Incorrect code entry count: " + (index + 1));
        }
    }
}

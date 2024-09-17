package uk.gov.di.test.pages;

import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import static uk.gov.di.test.utils.Constants.INCORRECT_EMAIL_OTP_CODE;

public class CheckYourEmailPage extends BasePage {

    By emailCodeField = By.id("code");

    public void enterEmailCode(String emailCode) {
        clearFieldAndEnter(emailCodeField, emailCode);
    }

    @When("the user enters the correct email code and clicks continue")
    public void enterCorrectEmailCodeAndContinue() {
        enterEmailCode(
                secretsManagerController.getDeploySecretValue("test_client_verify_email_otp"));
        findAndClickContinue();
    }

    public void enterIncorrectOTPCodeAndContinue() {
        enterEmailCode(INCORRECT_EMAIL_OTP_CODE);
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
            enterIncorrectOTPCodeAndContinue();
            System.out.println("Incorrect code entry count: " + (index + 1));
        }
    }
}

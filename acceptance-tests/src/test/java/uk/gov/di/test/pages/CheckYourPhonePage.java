package uk.gov.di.test.pages;

import org.openqa.selenium.By;

import static uk.gov.di.test.utils.Constants.INCORRECT_PHONE_CODE;

public class CheckYourPhonePage extends BasePage {
    By phoneCodeField = By.id("code");
    By problemsWithTheCodeLink = By.xpath("//*[contains(text(), 'Problems with the code?')]");
    By sendTheCodeAgainLink = By.xpath("//*[contains(text(), 'send the code again')]");

    public void enterPhoneCode(String code) {
        clearFieldAndEnter(phoneCodeField, code);
    }

    public void enterCorrectPhoneCodeAndContinue() {
        enterPhoneCode(System.getenv().get("TEST_USER_PHONE_CODE"));
        findAndClickContinue();
    }

    public void enterIncorrectPhoneCodeAndContinue() {
        enterPhoneCode(INCORRECT_PHONE_CODE);
        findAndClickContinue();
    }

    public void clickProblemsWithTheCodeLink() {
        driver.findElement(problemsWithTheCodeLink).click();
    }

    public void clickSendTheCodeAgainLink() {
        driver.findElement(sendTheCodeAgainLink).click();
    }

    public void enterIncorrectPhoneSecurityCodeNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Check your phone");
            enterIncorrectPhoneCodeAndContinue();
            System.out.println("Incorrect phone security code count: " + (index + 1));
        }
    }
}

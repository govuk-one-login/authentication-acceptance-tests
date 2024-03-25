package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class CheckYourPhonePage extends BasePage {
    By phoneCodeField = By.id("code");
    By problemsWithTheCodeLink = By.xpath("//*[contains(text(), 'Problems with the code?')]");
    By sendTheCodeAgainLink = By.xpath("//*[contains(text(), 'send the code again')]");

    public void enterPhoneCode(String code) {
        clearFieldAndEnter(phoneCodeField, code);
    }

    public void enterPhoneCodeAndContinue(String code) {
        enterPhoneCode(code);
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
            enterPhoneCodeAndContinue("123456");
            System.out.println("Incorrect phone security code count: " + (index + 1));
        }
    }
}

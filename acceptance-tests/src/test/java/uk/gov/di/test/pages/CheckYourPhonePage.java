package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import static uk.gov.di.test.utils.Constants.INCORRECT_PHONE_CODE;

public class CheckYourPhonePage extends BasePage {
    By phoneCodeField = By.id("code");
    By problemsWithTheCodeLink = By.xpath("//*[contains(text(), 'Problems with the code?')]");
    By sendTheCodeAgainLink = By.xpath("//*[contains(text(), 'send the code again')]");
    By changeHowYouGetSecurityCodesLink =
            By.xpath("//*[contains(text(), 'change how you get security codes')]");

    public void enterPhoneCode(String code) {
        clearFieldAndEnter(phoneCodeField, code);
    }

    public void enterCorrectPhoneCodeAndContinue() {
        enterPhoneCode(TEST_CONFIG_SERVICE.get("PHONE_VERIFY_CODE"));
        findAndClickContinue();
    }

    public void enterIncorrectPhoneCodeAndContinue() {
        enterPhoneCode(INCORRECT_PHONE_CODE);
        findAndClickContinue();
    }

    public void clickProblemsWithTheCodeLink() {
        Driver.get().findElement(problemsWithTheCodeLink).click();
    }

    public void clickChangeHowYouGetSecurityCodesLink() {
        Driver.get().findElement(changeHowYouGetSecurityCodesLink).click();
    }

    public void clickSendTheCodeAgainLink() {
        Driver.get().findElement(sendTheCodeAgainLink).click();
    }

    public void enterIncorrectPhoneSecurityCodeNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Check your phone");
            enterIncorrectPhoneCodeAndContinue();
            System.out.println("Incorrect phone security code count: " + (index + 1));
        }
    }
}

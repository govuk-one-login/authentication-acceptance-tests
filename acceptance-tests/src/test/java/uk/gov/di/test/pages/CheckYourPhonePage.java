package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

import static uk.gov.di.test.utils.Constants.INCORRECT_PHONE_CODE;

public class CheckYourPhonePage extends BasePage {
    By phoneCodeField = By.id("code");
    By problemsWithTheCodeLink = By.xpath("//*[contains(text(), 'Problems with the code?')]");
    By sendTheCodeAgainLink = By.xpath("//*[contains(text(), 'send the code again')]");

    public void enterPhoneCode(String code) throws MalformedURLException {
        clearFieldAndEnter(phoneCodeField, code);
    }

    public void enterCorrectPhoneCodeAndContinue() throws MalformedURLException {
        enterPhoneCode(System.getenv().get("TEST_USER_PHONE_CODE"));
        findAndClickContinue();
    }

    public void enterIncorrectPhoneCodeAndContinue() throws MalformedURLException {
        enterPhoneCode(INCORRECT_PHONE_CODE);
        findAndClickContinue();
    }

    public void clickProblemsWithTheCodeLink() throws MalformedURLException {
        Driver.get().findElement(problemsWithTheCodeLink).click();
    }

    public void clickSendTheCodeAgainLink() throws MalformedURLException {
        Driver.get().findElement(sendTheCodeAgainLink).click();
    }

    public void enterIncorrectPhoneSecurityCodeNumberOfTimes(Integer numberOfTimes)
            throws MalformedURLException {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Check your phone");
            enterIncorrectPhoneCodeAndContinue();
            System.out.println("Incorrect phone security code count: " + (index + 1));
        }
    }
}

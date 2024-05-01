package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends BasePage {
    By authAppCodeField = By.id("code");

    public void enterIncorrectAuthAppCodeAndContinue() {
        clearFieldAndEnter(authAppCodeField, "123456");
        findAndClickContinue();
    }

    public void enterIncorrectAuthAppCodeNumberOfTimes(int numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            enterIncorrectAuthAppCodeAndContinue();
            System.out.println("Incorrect auth app security code count: " + (index + 1));
        }
    }
}

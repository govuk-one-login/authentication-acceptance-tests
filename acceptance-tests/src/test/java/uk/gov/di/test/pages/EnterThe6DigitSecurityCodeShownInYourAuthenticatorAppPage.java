package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.AuthAppStub;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends BasePage {
    By authAppCodeField = By.id("code");

    public void enterCorrectAuthAppCode(String authAppSecretKey) {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
    }

    public void enterIncorrectAuthAppCodeAndContnue() {
        clearFieldAndEnter(authAppCodeField, "123456");
        findAndClickContinue();
    }

    public void enterIncorrectAuthAppCodeNumberOfTimes(int numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("security code");
            enterIncorrectAuthAppCodeAndContnue();
            System.out.println("Incorrect auth app security code count: " + (index + 1));
        }
    }
}

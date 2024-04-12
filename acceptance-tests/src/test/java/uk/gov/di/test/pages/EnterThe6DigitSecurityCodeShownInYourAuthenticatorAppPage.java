package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.AuthAppStub;

import java.net.MalformedURLException;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends BasePage {
    By authAppCodeField = By.id("code");

    public void enterCorrectAuthAppCode(String authAppSecretKey) throws MalformedURLException {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
    }

    public void enterIncorrectAuthAppCodeAndContnue() throws MalformedURLException {
        clearFieldAndEnter(authAppCodeField, "123456");
        findAndClickContinue();
    }

    public void enterIncorrectAuthAppCodeNumberOfTimes(int numberOfTimes)
            throws MalformedURLException {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("security code");
            enterIncorrectAuthAppCodeAndContnue();
            System.out.println("Incorrect auth app security code count: " + (index + 1));
        }
    }
}

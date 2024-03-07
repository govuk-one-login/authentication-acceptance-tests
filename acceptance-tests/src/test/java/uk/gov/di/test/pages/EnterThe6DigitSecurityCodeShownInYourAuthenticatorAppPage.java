package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.AuthAppStub;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends BasePage {
    By authAppCodeField = By.id("code");

    public void enterAuthAppCodeAndContinue(String authAppSecretKey) {
        if (authAppSecretKey == null) {
            authAppSecretKey = System.getenv().get("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");
        }
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

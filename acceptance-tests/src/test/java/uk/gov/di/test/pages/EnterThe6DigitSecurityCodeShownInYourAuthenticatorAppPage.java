package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.AuthAppStub;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends BasePage {
    By authAppCodeField = By.id("code");

    public void enterCorrectAuthAppCode(String authAppSecretKey) {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
    }
}

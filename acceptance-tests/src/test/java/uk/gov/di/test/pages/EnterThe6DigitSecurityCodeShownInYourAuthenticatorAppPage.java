package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.AuthAppStub;
import uk.gov.di.test.utils.SignIn;

public class EnterThe6DigitSecurityCodeShownInYourAuthenticatorAppPage extends SignIn {
    By authAppCodeField = By.id("code");

    public void enterCorrectAuthAppCode(String authAppSecretKey) {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
    }
}

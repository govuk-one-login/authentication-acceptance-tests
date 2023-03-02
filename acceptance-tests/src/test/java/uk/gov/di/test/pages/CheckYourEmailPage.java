package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class CheckYourEmailPage extends SignIn {
    By enterSixDigitEmailCodeField = By.id("code");

    public void enterEmailSecurityCode(String emailCode) {
        driver.findElement(enterSixDigitEmailCodeField).clear();
        driver.findElement(enterSixDigitEmailCodeField).sendKeys(emailCode);
    }
}

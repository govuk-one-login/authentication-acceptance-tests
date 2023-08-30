package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class GetSecurityCodePage extends SignIn {
    By getSecurityCodeButton = By.xpath("//button[contains(text(), 'Get security code')]");

    public void pressGetSecurityCodeButton() {
        driver.findElement(getSecurityCodeButton).click();
    }

    public void waitForPage() {
        waitForPageLoad("Get security code");
    }
}

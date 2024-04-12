package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class GetSecurityCodePage extends BasePage {
    By getSecurityCodeButton = By.xpath("//button[contains(text(), 'Get security code')]");

    public void pressGetSecurityCodeButton() throws MalformedURLException {
        Driver.get().findElement(getSecurityCodeButton).click();
    }

    public void waitForPage() throws MalformedURLException {
        waitForPageLoad("Get security code");
    }
}

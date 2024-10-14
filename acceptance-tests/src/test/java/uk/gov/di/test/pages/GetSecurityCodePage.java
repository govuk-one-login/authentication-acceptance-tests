package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class GetSecurityCodePage extends BasePage {
    By getSecurityCodeButton = By.xpath("//button[contains(text(), 'Get security code')]");

    public void pressGetSecurityCodeButton() {
        Driver.get().findElement(getSecurityCodeButton).click();
    }

    @Override
    public void waitForPage() {
        waitForPageLoad("Get security code");
    }
}

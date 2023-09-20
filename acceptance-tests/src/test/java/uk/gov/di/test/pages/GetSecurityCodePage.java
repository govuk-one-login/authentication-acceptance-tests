package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class GetSecurityCodePage extends BasePage {
    By getSecurityCodeButton = By.xpath("//button[contains(text(), 'Get security code')]");

    public void pressGetSecurityCodeButton() {
        driver.findElement(getSecurityCodeButton).click();
    }

    public void waitForPage() {
        waitForPageLoad("Get security code");
    }
}

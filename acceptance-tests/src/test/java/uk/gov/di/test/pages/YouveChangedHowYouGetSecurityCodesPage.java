package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class YouveChangedHowYouGetSecurityCodesPage extends BasePage {

    By securityCodeConfirmationText = By.cssSelector("form .govuk-body");

    public String getSecurityCodeMessageText() {
        return driver.findElement(securityCodeConfirmationText).getText();
    }
}

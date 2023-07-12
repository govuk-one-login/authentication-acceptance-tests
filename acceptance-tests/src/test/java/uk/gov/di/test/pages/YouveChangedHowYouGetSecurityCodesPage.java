package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class YouveChangedHowYouGetSecurityCodesPage extends SignIn {

    By securityCodeConfirmationText = By.cssSelector("form .govuk-body");

    public String getSecurityCodeMessageText() {
        return driver.findElement(securityCodeConfirmationText).getText();
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class YouveChangedHowYouGetSecurityCodesPage extends BasePage {

    By securityCodeConfirmationText = By.cssSelector("form .govuk-body");

    public String getSecurityCodeMessageText() throws MalformedURLException {
        return Driver.get().findElement(securityCodeConfirmationText).getText();
    }
}

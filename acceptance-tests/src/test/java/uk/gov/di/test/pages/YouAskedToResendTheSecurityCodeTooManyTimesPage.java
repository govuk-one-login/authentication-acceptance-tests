package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class YouAskedToResendTheSecurityCodeTooManyTimesPage extends BasePage {

    By getANewCodeLink = By.xpath("//*[contains(text(), 'get a new code')]");

    public void clickGetANewCodeLink() {
        Driver.get().findElement(getANewCodeLink).click();
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class YouAskedToResendTheSecurityCodeTooManyTimesPage extends SignIn {

    By getANewCodeLink = By.xpath("//*[contains(text(), 'get a new code')]");

    public void clickGetANewCodeLink() {
        driver.findElement(getANewCodeLink).click();
    }
}

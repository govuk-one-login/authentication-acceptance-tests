package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class YouAskedToResendTheSecurityCodeTooManyTimesPage extends BasePage {

    By getANewCodeLink = By.xpath("//*[contains(text(), 'get a new code')]");

    public void clickGetANewCodeLink() {
        driver.findElement(getANewCodeLink).click();
    }
}

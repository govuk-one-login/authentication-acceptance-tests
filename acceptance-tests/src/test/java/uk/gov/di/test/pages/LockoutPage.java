package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class LockoutPage extends BasePage {

    By lockoutScreenText = By.cssSelector("#main-content .govuk-grid-column-two-thirds");

    public String getLockoutScreenText() {
        return driver.findElement(lockoutScreenText).getText();
    }
}

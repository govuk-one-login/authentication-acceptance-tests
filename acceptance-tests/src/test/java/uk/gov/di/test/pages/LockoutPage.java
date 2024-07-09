package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class LockoutPage extends BasePage {

    By lockoutScreenText = By.cssSelector("#main-content .govuk-grid-column-two-thirds");

    public String getLockoutScreenText() {
        return Driver.get().findElement(lockoutScreenText).getText();
    }
}

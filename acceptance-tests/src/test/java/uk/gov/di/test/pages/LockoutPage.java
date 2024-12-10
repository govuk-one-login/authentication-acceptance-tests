package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class LockoutPage extends BasePage {

    By lockoutScreenText = By.cssSelector("#main-content .govuk-grid-column-two-thirds");
    By strategicAppWaitAndThenTryAgainText =
            By.cssSelector("#main-content .govuk-grid-column-two-thirds");

    public String getLockoutScreenText() {
        return Driver.get().findElement(lockoutScreenText).getText();
    }

    public String getStrategicAppWaitAndThenTryAgainTextMessage(String textValue) {
        return Driver.get()
                .findElement(
                        By.xpath(String.format("//p[normalize-space(text())='%s']", textValue)))
                .getText();
    }
}

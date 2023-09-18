package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class RpStubPage extends BasePage {

    public void goToRpStub() {
        driver.get(RP_URL.toString());
    }

    public void selectRpOptionsById(String opts) {
        if (!opts.isEmpty() && !opts.equalsIgnoreCase("default")) {
            String ids[] = opts.split(",");
            for (String id : ids) {
                driver.findElement(By.id(id)).click();
            }
        }
    }
}

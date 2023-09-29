package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class EnterASecurityCodeToContinuePage extends BasePage {
    public By authAppCodeField = By.id("code");

    // Todo : Need to use the correct id locator
    public By threeDigitsNumber = By.id("-----");

    public String getThreeDigitsNumber() {
        return driver.findElement(threeDigitsNumber).getText();
    }
}

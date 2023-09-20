package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class ChooseHowToGetSecurityCodesPage extends BasePage {
    By textMessageRadioButton = By.cssSelector("input[value='SMS']");
    By authAppRadioButton = By.cssSelector("input[value='AUTH_APP']");

    public void selectAuthMethodAndContinue(String method) {
        switch (method.toLowerCase()) {
            case "text message":
                driver.findElement(textMessageRadioButton).click();
                break;
            case "auth app":
                driver.findElement(authAppRadioButton).click();
                break;
        }
        findAndClickContinue();
    }

    public Boolean getTextMessageRadioButtonStatus() {
        return driver.findElement(textMessageRadioButton).isSelected();
    }

    public Boolean getAuthAppRadioButtonStatus() {
        return driver.findElement(authAppRadioButton).isSelected();
    }
}

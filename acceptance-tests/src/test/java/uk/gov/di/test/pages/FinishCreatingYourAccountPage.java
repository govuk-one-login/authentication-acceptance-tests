package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class FinishCreatingYourAccountPage extends BasePage {
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
}

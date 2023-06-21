package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class ChooseHowToGetSecurityCodesPage extends SignIn {
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

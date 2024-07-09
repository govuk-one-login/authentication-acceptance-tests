package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class FinishCreatingYourAccountPage extends BasePage {
    By textMessageRadioButton = By.cssSelector("input[value='SMS']");
    By authAppRadioButton = By.cssSelector("input[value='AUTH_APP']");

    public void selectAuthMethodAndContinue(String method) {
        switch (method.toLowerCase()) {
            case "text message":
                Driver.get().findElement(textMessageRadioButton).click();
                break;
            case "auth app":
                Driver.get().findElement(authAppRadioButton).click();
                break;
        }
        findAndClickContinue();
    }
}

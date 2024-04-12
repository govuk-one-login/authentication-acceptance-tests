package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class ChooseHowToGetSecurityCodesPage extends BasePage {
    By textMessageRadioButton = By.cssSelector("input[value='SMS']");
    By authAppRadioButton = By.cssSelector("input[value='AUTH_APP']");

    public void selectAuthMethodAndContinue(String method) throws MalformedURLException {
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

    public Boolean getTextMessageRadioButtonStatus() throws MalformedURLException {
        return Driver.get().findElement(textMessageRadioButton).isSelected();
    }

    public Boolean getAuthAppRadioButtonStatus() throws MalformedURLException {
        return Driver.get().findElement(authAppRadioButton).isSelected();
    }
}

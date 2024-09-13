package uk.gov.di.test.pages;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class ChooseHowToGetSecurityCodesPage extends BasePage {
    By textMessageRadioButton = By.cssSelector("input[value='SMS']");
    By authAppRadioButton = By.cssSelector("input[value='AUTH_APP']");

    @ParameterType("text message|auth app")
    public String authMethod(String intervention) {
        return intervention;
    }

    @When("the user selects {authMethod} auth method and clicks continue")
    public void selectAuthMethodAndContinue(String method) {
        switch (method.toLowerCase()) {
            case "text message":
                Driver.get().findElement(textMessageRadioButton).click();
                break;
            case "auth app":
                Driver.get().findElement(authAppRadioButton).click();
                break;
            default:
                throw new RuntimeException("Invalid method type: " + method);
        }
        findAndClickContinue();
    }

    public Boolean getTextMessageRadioButtonStatus() {
        return Driver.get().findElement(textMessageRadioButton).isSelected();
    }

    public Boolean getAuthAppRadioButtonStatus() {
        return Driver.get().findElement(authAppRadioButton).isSelected();
    }
}

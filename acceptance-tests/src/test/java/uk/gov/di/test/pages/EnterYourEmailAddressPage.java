package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class EnterYourEmailAddressPage extends BasePage {
    By emailField = By.id("email");
    By continueButton = By.cssSelector("button[type='Submit']");
    By backButton = By.className("govuk-back-link");

    public void enterEmailAddress(String emailAddress) {
        clearFieldAndEnter(emailField, emailAddress);
    }

    public void enterEmailAddressAndContinue(String emailAddress) {
        enterEmailAddress(emailAddress);
        findAndClickContinue();
    }

    public String continueButtonText() {
        return Driver.get().findElement(continueButton).getText();
    }

    public String backButtonText() {
        return Driver.get().findElement(backButton).getText();
    }
}

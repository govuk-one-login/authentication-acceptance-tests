package uk.gov.di.test.pages;

import org.openqa.selenium.By;

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
        return driver.findElement(continueButton).getText();
    }

    public String backButtonText() {
        return driver.findElement(backButton).getText();
    }
}

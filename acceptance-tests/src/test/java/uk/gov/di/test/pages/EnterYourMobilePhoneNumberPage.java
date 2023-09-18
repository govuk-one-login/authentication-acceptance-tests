package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class EnterYourMobilePhoneNumberPage extends BasePage {
    By internationalPhoneNumberField = By.id("internationalPhoneNumber");
    By iDoNotHaveUkMobilePhoneNumberCheckbox = By.id("hasInternationalPhoneNumber");
    By ukPhoneNumberField = By.id("phoneNumber");

    public boolean isInternationalMobileNumberFieldDisplayed() {
        return driver.findElement(internationalPhoneNumberField).isDisplayed();
    }

    public void tickIDoNotHaveUkMobileNumber() {
        driver.findElement(iDoNotHaveUkMobilePhoneNumberCheckbox).click();
    }

    public void enterInternationalMobilePhoneNumber(String internationalMobilePhoneNumber) {
        clearFieldAndEnter(internationalPhoneNumberField, internationalMobilePhoneNumber);
    }

    public void enterUkPhoneNumber(String phoneNumber) {
        clearFieldAndEnter(ukPhoneNumberField, phoneNumber);
    }
}

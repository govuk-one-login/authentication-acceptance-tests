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

    public void enterInternationalMobilePhoneNumberAndContinue(
            String internationalMobilePhoneNumber) {
        clearFieldAndEnter(internationalPhoneNumberField, internationalMobilePhoneNumber);
        findAndClickContinue();
    }

    public void enterUkPhoneNumberAndContinue(String phoneNumber) {
        clearFieldAndEnter(ukPhoneNumberField, phoneNumber);
        findAndClickContinue();
    }

    public void enterValidUkPhoneNumberAndContinue() {
        String validUkPhoneNumber = System.getenv("TEST_USER_PHONE_NUMBER");
        enterUkPhoneNumberAndContinue(validUkPhoneNumber);
    }
}

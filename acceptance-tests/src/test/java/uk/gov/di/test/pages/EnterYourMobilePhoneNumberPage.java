package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class EnterYourMobilePhoneNumberPage extends SignIn {
    By internationalPhoneNumberField = By.id("internationalPhoneNumber");
    By iDoNotHaveUkMobilePhoneNumberCheckbox = By.id("hasInternationalPhoneNumber");
    By ukPhoneNumberField = By.id("phoneNumber");

    public boolean isInternationalMobileNumberFieldDisplayed() {
        return driver.findElement(internationalPhoneNumberField).isDisplayed();
    }

    public void tickIDoNotHaveUkMobileNumber() {
        driver.findElement(iDoNotHaveUkMobilePhoneNumberCheckbox).click();
    }

    public boolean getStatusOfUKMobileNumberField() {
        return driver.findElement(ukPhoneNumberField).isEnabled();
    }

    public void enterInternationalMobilePhoneNumber(String internationalMobilePhoneNumber) {
        driver.findElement(internationalPhoneNumberField).clear();
        driver.findElement(internationalPhoneNumberField).sendKeys(internationalMobilePhoneNumber);
    }

    public void enterUkPhoneNumber(String phoneNumber) {
        driver.findElement(ukPhoneNumberField).clear();
        driver.findElement(ukPhoneNumberField).sendKeys(phoneNumber);
    }
}

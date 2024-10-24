package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import static uk.gov.di.test.utils.Constants.UK_MOBILE_PHONE_NUMBER;

public class EnterYourMobilePhoneNumberPage extends BasePage {
    By internationalPhoneNumberField = By.id("internationalPhoneNumber");
    By iDoNotHaveUkMobilePhoneNumberCheckbox = By.id("hasInternationalPhoneNumber");
    By ukPhoneNumberField = By.id("phoneNumber");

    public boolean isInternationalMobileNumberFieldDisplayed() {
        return Driver.get().findElement(internationalPhoneNumberField).isDisplayed();
    }

    public void tickIDoNotHaveUkMobileNumber() {
        Driver.get().findElement(iDoNotHaveUkMobilePhoneNumberCheckbox).click();
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
        enterUkPhoneNumberAndContinue(UK_MOBILE_PHONE_NUMBER);
    }
}

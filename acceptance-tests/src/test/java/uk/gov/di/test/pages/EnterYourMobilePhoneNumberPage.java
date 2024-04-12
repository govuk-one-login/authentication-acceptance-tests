package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class EnterYourMobilePhoneNumberPage extends BasePage {
    By internationalPhoneNumberField = By.id("internationalPhoneNumber");
    By iDoNotHaveUkMobilePhoneNumberCheckbox = By.id("hasInternationalPhoneNumber");
    By ukPhoneNumberField = By.id("phoneNumber");

    public boolean isInternationalMobileNumberFieldDisplayed() throws MalformedURLException {
        return Driver.get().findElement(internationalPhoneNumberField).isDisplayed();
    }

    public void tickIDoNotHaveUkMobileNumber() throws MalformedURLException {
        Driver.get().findElement(iDoNotHaveUkMobilePhoneNumberCheckbox).click();
    }

    public void enterInternationalMobilePhoneNumberAndContinue(
            String internationalMobilePhoneNumber) throws MalformedURLException {
        clearFieldAndEnter(internationalPhoneNumberField, internationalMobilePhoneNumber);
        findAndClickContinue();
    }

    public void enterUkPhoneNumberAndContinue(String phoneNumber) throws MalformedURLException {
        clearFieldAndEnter(ukPhoneNumberField, phoneNumber);
        findAndClickContinue();
    }
}

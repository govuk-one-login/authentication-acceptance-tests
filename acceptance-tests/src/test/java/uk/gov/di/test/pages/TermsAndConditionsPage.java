package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class TermsAndConditionsPage extends BasePage {

    By agreeAndContinueButton = By.name("termsAndConditionsResult");

    public void pressAgreeAndContinueButton() {
        Driver.get().findElement(agreeAndContinueButton).click();
    }
}

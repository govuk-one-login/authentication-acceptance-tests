package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class TermsAndConditionsPage extends BasePage {

    By agreeAndContinueButton = By.name("termsAndConditionsResult");

    public void pressAgreeAndContinueButton() {
        driver.findElement(agreeAndContinueButton).click();
    }
}

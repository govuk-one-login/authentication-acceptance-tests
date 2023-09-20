package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class TermsAndConditionsPage extends BasePage {

    By agreeAndContinueButton = By.name("termsAndConditionsResult");
    By iDoNotAgreeLink = By.xpath("//a[@href='/updated-terms-and-conditions-disagree']");

    public void pressAgreeAndContinueButton() {
        driver.findElement(agreeAndContinueButton).click();
    }
}

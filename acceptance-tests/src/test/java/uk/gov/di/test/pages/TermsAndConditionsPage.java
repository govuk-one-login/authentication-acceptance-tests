package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

public class TermsAndConditionsPage extends SignIn {

    By agreeAndContinueButton = By.name("termsAndConditionsResult");
    By iDoNotAgreeLink = By.xpath("//a[@href='/updated-terms-and-conditions-disagree']");

    public void pressAgreeAndContinueButton() {
        driver.findElement(agreeAndContinueButton).click();
    }

    public void clickIDoNotAgreeLink() {
        driver.findElement(iDoNotAgreeLink).click();
    }
}

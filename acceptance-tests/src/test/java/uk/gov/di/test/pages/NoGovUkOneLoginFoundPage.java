package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class NoGovUkOneLoginFoundPage extends BasePage {
    By createAccountButton = By.xpath("//button[contains(text(), 'Create a GOV.UK One Login')]");

    public void clickCreateGovUkOneLoginButton() {
        driver.findElement(createAccountButton).click();
    }
}

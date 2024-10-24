package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class NoGovUkOneLoginFoundPage extends BasePage {
    By createAccountButton = By.xpath("//button[contains(text(), 'Create a GOV.UK One Login')]");

    public void clickCreateGovUkOneLoginButton() {
        Driver.get().findElement(createAccountButton).click();
    }
}

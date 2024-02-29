package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class CreateOrSignInPage extends BasePage {

    By signInButton = By.id("sign-in-button");
    By createAGovUkOneLogin = By.id("create-account-link");
    By linkToSwitchToWelsh = By.xpath("//a[@href='?lng=cy']");
    By linkToSwitchToEnglish = By.xpath("//a[@href='?lng=en']");

    public void clickCreateAGovUkOneLoginButton() {
        waitUntilElementClickable(createAGovUkOneLogin);
        driver.findElement(createAGovUkOneLogin).click();
    }

    public void clickSignInButton() {
        waitUntilElementClickable(signInButton);
        driver.findElement(signInButton).click();
    }

    public void switchLanguageTo(String lang) {
        switch (lang.toLowerCase()) {
            case "welsh":
                driver.findElement(linkToSwitchToWelsh).click();
                break;
            case "english":
                driver.findElement(linkToSwitchToEnglish).click();
                break;
        }
    }
}

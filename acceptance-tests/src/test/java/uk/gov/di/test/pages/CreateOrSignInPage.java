package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class CreateOrSignInPage extends BasePage {

    By signInButton = By.id("sign-in-button");
    By createAGovUkOneLogin = By.id("create-account-link");
    By linkToSwitchToWelsh = By.xpath("//a[@href='?lng=cy']");
    By linkToSwitchToEnglish = By.xpath("//a[@href='?lng=en']");

    public void clickCreateAGovUkOneLoginButton() throws MalformedURLException {
        Driver.get().findElement(createAGovUkOneLogin).click();
    }

    public void clickSignInButton() throws MalformedURLException {
        Driver.get().findElement(signInButton).click();
    }

    public void switchLanguageTo(String lang) throws MalformedURLException {
        switch (lang.toLowerCase()) {
            case "welsh":
                Driver.get().findElement(linkToSwitchToWelsh).click();
                break;
            case "english":
                Driver.get().findElement(linkToSwitchToEnglish).click();
                break;
        }
    }
}

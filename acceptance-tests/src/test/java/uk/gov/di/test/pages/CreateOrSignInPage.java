package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class CreateOrSignInPage extends BasePage {

    By signInButton = By.id("sign-in-button");
    By createAGovUkOneLogin = By.id("create-account-link");
    By linkToSwitchToWelsh = By.xpath("//a[@href='?lng=cy']");
    By linkToSwitchToEnglish = By.xpath("//a[@href='?lng=en']");

    public void clickCreateAGovUkOneLoginButton() {
        Driver.getOrCreate().findElement(createAGovUkOneLogin).click();
    }

    public void clickSignInButton() {
        Driver.getOrCreate().findElement(signInButton).click();
    }

    public void switchLanguageTo(String language) {
        switch (language.toLowerCase()) {
            case "welsh":
                Driver.getOrCreate().findElement(linkToSwitchToWelsh).click();
                break;
            case "english":
                Driver.getOrCreate().findElement(linkToSwitchToEnglish).click();
                break;
            default:
                throw new RuntimeException("Invalid language: " + language);
        }
    }

    @Override
    public void waitForPage() {
        waitForPageLoad("Create your GOV.UK One Login or sign in");
    }
}

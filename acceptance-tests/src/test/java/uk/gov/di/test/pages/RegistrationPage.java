package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.SignIn;

import java.util.List;

public class RegistrationPage extends SignIn {

    By radioTextMessageSecurityCodes = By.id("mfaOptions");
    By secretKeyField = By.id("secret-key");
    By secretKeyBlocks = By.cssSelector("#secret-key span");
    By iCannotScanQrCode =
            By.cssSelector("#main-content > div > div > details:nth-child(6) > summary");
    By goBackButton = By.cssSelector("#form-tracking > button");
    By radioShareInfoAccept = By.id("share-info-accepted");
    By deleteAccountButton = By.className("govuk-button--warning");
    By shareInfoReject = By.id("share-info-rejected");
    By signinToServiceButton = By.cssSelector("form > button");

    public void radioTextMessageSecurityCodesClick() {
        driver.findElement(radioTextMessageSecurityCodes).click();
    }

    public void deleteAccountButtonClick() {
        driver.findElement(deleteAccountButton).click();
    }

    public void shareInfoRejectClick() {
        driver.findElement(shareInfoReject).click();
    }

    public void goBackClick() {
        driver.findElement(goBackButton).click();
    }

    public void shareInfoAcceptClick() {
        driver.findElement(radioShareInfoAccept).click();
    }

    public void signinToServiceButtonClick() {
        driver.findElement(signinToServiceButton).click();
    }

    public void iCannotScanQrCodeClick() {
        driver.findElement(iCannotScanQrCode).click();
    }

    public String getSecretFieldText() {
        new WebDriverWait(driver, DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.visibilityOf(driver.findElement(secretKeyField)));
        List<WebElement> secretKeyComponents = driver.findElements(secretKeyBlocks);
        String secretKeyText = "";
        for (int i = 1; i < secretKeyComponents.size(); i++) {
            secretKeyText = secretKeyText + secretKeyComponents.get(i).getText();
        }
        return secretKeyText;
    }
}

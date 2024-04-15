package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.Driver;

import java.util.List;

public class SetUpAnAuthenticatorAppPage extends BasePage {
    By secretKeyField = By.id("secret-key");
    By secretKeyBlocks = By.cssSelector("#secret-key span");
    By iCannotScanTheQrCode = By.xpath("//span[contains(text(), 'I cannot scan the QR code')]");
    By authAppCodeField = By.id("code");

    public void iCannotScanQrCodeClick() {
        Driver.get().findElement(iCannotScanTheQrCode).click();
    }

    public String getSecretFieldText() {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.visibilityOf(Driver.get().findElement(secretKeyField)));
        List<WebElement> secretKeyComponents = Driver.get().findElements(secretKeyBlocks);
        String secretKeyText = "";
        for (int i = 1; i < secretKeyComponents.size(); i++) {
            secretKeyText = secretKeyText + secretKeyComponents.get(i).getText();
        }
        return secretKeyText;
    }
}

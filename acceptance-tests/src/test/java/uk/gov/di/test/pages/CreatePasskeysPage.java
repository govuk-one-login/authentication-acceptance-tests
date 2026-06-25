package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.Driver;

public class CreatePasskeysPage extends BasePage {

    public static final String PATH = "/create-passkey";

    private final By h1 = By.cssSelector("h1");
    private final By skipButton = By.xpath("//button[contains(text(), 'Skip for now')]");

    @Override
    public void waitForPage() {
        WebDriverWait wait = new WebDriverWait(Driver.getOrCreate(), DEFAULT_PAGE_LOAD_WAIT_TIME);
        wait.until(ExpectedConditions.urlContains(PATH));
        wait.until(ExpectedConditions.visibilityOfElementLocated(h1));
    }

    public void clickSkipButton() {
        Driver.getOrCreate().findElement(skipButton).click();
    }

    public void dismissIfPresent() {
        // Prevents a race condition after MFA code submission. In non-MFA flows this is a no-op.
        new WebDriverWait(Driver.getOrCreate(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("/enter-code")));
        waitForReadyStateComplete();
        if (Driver.getOrCreate().getCurrentUrl().contains(PATH)) {
            clickSkipButton();
        }
    }
}

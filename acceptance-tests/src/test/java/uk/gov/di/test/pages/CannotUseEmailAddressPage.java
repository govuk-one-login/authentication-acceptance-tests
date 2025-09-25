package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.Driver;

public class CannotUseEmailAddressPage extends BasePage {

    private static final String PATH = "/cannot-use-email-address";

    private final By h1 = By.cssSelector("h1");
    private final By bodyText = By.cssSelector("#main-content .govuk-grid-column-two-thirds");
    private final By tryAnotherEmailLink = By.linkText("try another email address");

    @Override
    public void waitForPage() {
        WebDriverWait wait = new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME);
        wait.until(ExpectedConditions.urlContains(PATH));
        wait.until(ExpectedConditions.visibilityOfElementLocated(h1));
    }

    public String heading() {
        return Driver.get().findElement(h1).getText();
    }

    public String screenText() {
        WebElement el = Driver.get().findElement(bodyText);
        return el.getText();
    }

    public void clickTryAnotherEmail() {
        WebDriverWait wait = new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(tryAnotherEmailLink)).click();
    }

    public static boolean matches(String url) {
        return url != null && url.contains(PATH);
    }
}

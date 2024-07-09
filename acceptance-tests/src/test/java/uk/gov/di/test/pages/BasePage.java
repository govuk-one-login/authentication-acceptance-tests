package uk.gov.di.test.pages;

import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.AuthAppStub;
import uk.gov.di.test.utils.AuthenticationJourneyPages;
import uk.gov.di.test.utils.Driver;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasePage {
    protected static final String RP_URL =
            System.getenv()
                    .getOrDefault(
                            "RP_URL",
                            "https://acceptance-test-rp-build.build.stubs.account.gov.uk/");
    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.of(20, SECONDS);

    protected static WebDriver driver;
    protected static Scenario scenario;

    public By authAppCodeField = By.id("code");

    protected void waitForPageLoad(String titleContains) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.titleContains(titleContains));
        waitForReadyStateComplete();
    }

    protected void waitForPageLoadThenValidate(AuthenticationJourneyPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(Driver.get().getCurrentUrl()).getPath());
        assertEquals(page.getFullTitle(), Driver.get().getTitle());
    }

    protected void findAndClickContinue() {
        waitForReadyStateComplete();
        WebElement continueButton =
                Driver.get()
                        .findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    protected void findAndClickContinueWelsh() {
        waitForReadyStateComplete();
        WebElement continueButton =
                Driver.get()
                        .findElement(By.cssSelector("#main-content > div > div > form > button"));
        continueButton.click();
    }

    protected void findAndClickButtonByText(String buttonText) {
        waitForReadyStateComplete();
        WebElement button =
                Driver.get()
                        .findElement(
                                By.xpath(
                                        "//button[text()[normalize-space() = '"
                                                + buttonText
                                                + "']]"));
        button.click();
    }

    public void switchDefaultTimeout(String status) {
        switch (status.toLowerCase()) {
            case "on":
                Driver.get().manage().timeouts().implicitlyWait(DEFAULT_PAGE_LOAD_WAIT_TIME);
                break;
            case "off":
                Driver.get().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                break;
        }
    }

    protected boolean isLinkTextDisplayedImmediately(String linkText) {
        switchDefaultTimeout("off");
        List elements =
                Driver.get()
                        .findElements(
                                By.xpath("//*[text()[normalize-space() = '" + linkText + "']]"));
        switchDefaultTimeout("off");
        if (elements.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void waitForThisErrorMessage(String expectedMessage) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        ExpectedConditions.visibilityOf(
                                Driver.get()
                                        .findElement(
                                                By.xpath(
                                                        "//li/a[text() = '"
                                                                + expectedMessage
                                                                + "']"))));
    }

    public void waitForThisText(String expectedText) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        ExpectedConditions.visibilityOf(
                                Driver.get()
                                        .findElement(
                                                By.xpath(
                                                        "//*[contains(text(), '"
                                                                + expectedText
                                                                + "')]"))));
    }

    protected void pressBack() {
        Driver.get().findElement(By.xpath("//a[text()[normalize-space() = 'Back']]")).click();
    }

    protected void selectLinkByText(String linkText) {
        Driver.get()
                .findElement(By.xpath("//*[text()[normalize-space() = '" + linkText + "']]"))
                .click();
    }

    protected void clearFieldAndEnter(By ele, String text) {
        Driver.get().findElement(ele).clear();
        Driver.get().findElement(ele).sendKeys(text);
    }

    public Boolean isErrorSummaryDisplayed() {
        return Driver.get().findElement(By.className("govuk-error-summary")).isDisplayed();
    }

    public String getPageHeading() {
        return Driver.get().findElement(By.cssSelector("h1")).getText().trim();
    }

    public void checkForNewTabAndGoToIt(String newTabTitle) {
        switchToTabByIndex(1);
        waitForPageLoad(newTabTitle);
    }

    public void switchToTabByIndex(Integer idx) {
        ArrayList<String> tabs = new ArrayList<String>(Driver.get().getWindowHandles());
        Driver.get().switchTo().window(tabs.get(idx));
    }

    public void switchToTabWithTitleContaining(String titleToSwitchTo) {
        Set<String> allTabs = Driver.get().getWindowHandles();
        for (String tab : allTabs) {
            String title = Driver.get().switchTo().window(tab).getTitle();
            if (title.contains(titleToSwitchTo)) {
                break;
            }
        }
    }

    protected void closeActiveTab() {
        Driver.get().close();
    }

    public void enterCorrectAuthAppCodeAndContinue(String authAppSecretKey) {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
        findAndClickContinue();
    }

    public void setAnalyticsCookieTo(Boolean state) {
        Driver.get()
                .manage()
                .addCookie(new Cookie("cookies_preferences_set", "{\"analytics\":" + state + "}"));
    }

    public void waitUntilElementClickable(By by) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.elementToBeClickable(Driver.get().findElement(by)));
    }

    public void waitUntilElementClickable(WebElement element) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForReadyStateComplete() {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        (ExpectedCondition<Boolean>)
                                wd ->
                                        ((JavascriptExecutor) wd)
                                                .executeScript("return document.readyState")
                                                .equals("complete"));
    }

    public void takeScreenshot(WebDriver driver, Scenario scenario) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        final byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Step screenshot");
    }
}

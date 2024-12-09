package uk.gov.di.test.pages;

import io.cucumber.java.Scenario;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.services.TestConfigurationService;
import uk.gov.di.test.utils.AuthAppStub;
import uk.gov.di.test.utils.AuthenticationJourneyPages;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.OneLoginSession;
import uk.gov.di.test.utils.SessionContextExceptions;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasePage {

    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.ofSeconds(20);
    protected static final Duration NO_PAGE_LOAD_WAIT_TIME = Duration.ofSeconds(0);

    protected static WebDriver driver;
    protected static Scenario scenario;

    public void waitForPage() {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public By authAppCodeField = By.id("code");

    protected void waitForPageLoad(String titleContains) {
        Instant start = Instant.now();
        try {
            new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                    .until(ExpectedConditions.titleContains(titleContains));
        } catch (WebDriverException e) {
            throw new SessionContextExceptions.WaitForPageLoadException(
                    titleContains,
                    Driver.get().getCurrentUrl(),
                    Driver.get().getTitle(),
                    start,
                    new OneLoginSession(Driver.get().manage().getCookies()),
                    e);
        }
        waitForReadyStateComplete();
    }

    protected void waitForPageLoadThenValidate(AuthenticationJourneyPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(
                page.getRoute(),
                URI.create(Objects.requireNonNull(Driver.get().getCurrentUrl())).getPath());
        assertEquals(page.getFullTitle(), Driver.get().getTitle());
    }

    protected WebElement findElement(By selector) {
        waitForReadyStateComplete();
        try {
            return Driver.get().findElement(selector);
        } catch (WebDriverException e) {
            throw new SessionContextExceptions.FindElementException(
                    selector.toString(),
                    Driver.get().getCurrentUrl(),
                    Driver.get().getTitle(),
                    new OneLoginSession(Driver.get().manage().getCookies()),
                    e);
        }
    }

    protected void findAndClickButton(By selector) {
        waitForReadyStateComplete();
        WebElement button = findElement(selector);
        button.click();
    }

    protected void findAndClickContinue() {
        findAndClickButton(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
    }

    protected void findAndClickContinueWelsh() {
        findAndClickButton(By.cssSelector("#main-content > div > div > form > button"));
    }

    protected void findAndClickButtonByText(String buttonText) {
        findAndClickButton(By.xpath("//button[text()[normalize-space() = '" + buttonText + "']]"));
    }

    public void switchDefaultTimeout(String status) {
        switch (status.toLowerCase()) {
            case "on":
                Driver.get().manage().timeouts().implicitlyWait(DEFAULT_PAGE_LOAD_WAIT_TIME);
                break;
            case "off":
                Driver.get().manage().timeouts().implicitlyWait(NO_PAGE_LOAD_WAIT_TIME);
                break;
            default:
                throw new RuntimeException("Invalid status: " + status);
        }
    }

    protected boolean isLinkTextDisplayedImmediately(String linkText) {
        switchDefaultTimeout("off");
        List<WebElement> elements =
                Driver.get()
                        .findElements(
                                By.xpath("//*[text()[normalize-space() = '" + linkText + "']]"));
        switchDefaultTimeout("on");
        return !elements.isEmpty();
    }

    public void waitForThisErrorMessage(String expectedMessage) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        ExpectedConditions.visibilityOf(
                                findElement(
                                        By.xpath("//li/a[text() = '" + expectedMessage + "']"))));
    }

    public void waitForThisText(String expectedText) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        ExpectedConditions.visibilityOf(
                                findElement(
                                        By.xpath(
                                                "//*[contains(text(), '" + expectedText + "')]"))));
    }

    protected void pressBack() {
        findElement(By.xpath("//a[text()[normalize-space() = 'Back']]")).click();
    }

    protected void selectLinkByText(String linkText) {
        findElement(By.xpath("//*[text()[normalize-space() = '" + linkText + "']]")).click();
    }

    protected void clearFieldAndEnter(By ele, String text) {
        findElement(ele).clear();
        findElement(ele).sendKeys(text);
    }

    public Boolean isErrorSummaryDisplayed() {
        return findElement(By.className("govuk-error-summary")).isDisplayed();
    }

    public String getPageHeading() {
        return findElement(By.cssSelector("h1")).getText().trim();
    }

    public String getLockoutText() {
        return findElement(By.xpath("//*[normalize-space()='wait 2 hours, then try again']"))
                .getText()
                .trim();
    }

    public String getLockoutTexts() {
        return findElement(By.xpath("//*[normalize-space()='Wait 2 hours, then try again.']"))
                .getText()
                .trim();
    }

    public String getRetryText() {
        return findElement(By.xpath("//*[@class='govuk-body strategic-app-retry-options']"))
                .getText()
                .trim();
    }

    public void checkForNewTabAndGoToIt(String newTabTitle) {
        switchToTabByIndex(1);
        waitForPageLoad(newTabTitle);
    }

    public void switchToTabByIndex(Integer idx) {
        ArrayList<String> tabs = new ArrayList<>(Driver.get().getWindowHandles());
        Driver.get().switchTo().window(tabs.get(idx));
    }

    public void switchToTabWithTitleContaining(String titleToSwitchTo) {
        Set<String> allTabs = Driver.get().getWindowHandles();
        for (String tab : allTabs) {
            String title = Driver.get().switchTo().window(tab).getTitle();
            assert title != null;
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
                .until(ExpectedConditions.elementToBeClickable(findElement(by)));
    }

    public void waitUntilElementClickable(WebElement element) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForReadyStateComplete() {
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

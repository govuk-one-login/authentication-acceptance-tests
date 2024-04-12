package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.AuthAppStub;
import uk.gov.di.test.utils.AuthenticationJourneyPages;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasePage {
    // protected WebDriver driver;
    // public BasePage() throws MalformedURLException {
    //    this.driver = Driver.get();
    // }

    //    protected static final String CHROME_BROWSER = "chrome";
    //    protected static final String FIREFOX_BROWSER = "firefox";
    //    protected static final String SELENIUM_URL =
    //            System.getenv().getOrDefault("SELENIUM_URL", "http://localhost:4445/wd/hub");

    //    protected static final String IDP_URL =
    //            System.getenv()
    //                    .getOrDefault(
    //                            "IDP_URL",
    //                            "https://front.build.auth.ida.digital.cabinet-office.gov.uk/");
    protected static final String RP_URL =
            System.getenv()
                    .getOrDefault(
                            "RP_URL",
                            "https://acceptance-test-rp-build.build.stubs.account.gov.uk/");

    //    protected static final Boolean SELENIUM_LOCAL =
    //            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_LOCAL", "false"));
    //    protected static final Boolean SELENIUM_HEADLESS =
    //            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_HEADLESS", "false"));
    //    protected static final String SELENIUM_BROWSER =
    //            System.getenv().getOrDefault("SELENIUM_BROWSER", FIREFOX_BROWSER);
    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.of(20, SECONDS);
    // protected static WebDriver driver;
    public By authAppCodeField = By.id("code");

    //    protected void setupWebdriver() throws MalformedURLException {
    //        if (driver == null) {
    //            switch (SELENIUM_BROWSER) {
    //                case CHROME_BROWSER:
    //                    ChromeOptions chromeOptions = new ChromeOptions();
    //                    chromeOptions.setHeadless(SELENIUM_HEADLESS);
    //                    chromeOptions.addArguments("--remote-allow-origins=*");
    //                    chromeOptions.addArguments("--disable-gpu");
    //                    chromeOptions.addArguments("--disable-extensions");
    //                    chromeOptions.addArguments("--no-sandbox");
    //                    chromeOptions.addArguments("--disable-dev-shm-usage");
    //                    if (SELENIUM_LOCAL) {
    //                        System.setProperty("webdriver.chrome.whitelistedIps", "");
    //                        driver = new ChromeDriver(chromeOptions);
    //                    } else {
    //                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), chromeOptions);
    //                    }
    //                    break;
    //                default:
    //                    FirefoxOptions firefoxOptions = new FirefoxOptions();
    //                    firefoxOptions.setHeadless(SELENIUM_HEADLESS);
    //                    firefoxOptions.setPageLoadTimeout(Duration.of(30, SECONDS));
    //                    firefoxOptions.setImplicitWaitTimeout(Duration.of(30, SECONDS));
    //                    if (SELENIUM_LOCAL) {
    //                        driver = new FirefoxDriver(firefoxOptions);
    //                    } else {
    //                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), firefoxOptions);
    //                    }
    //            }
    //        }
    //    }

    //    protected void closeWebdriver() {
    //        if (driver != null) {
    //            driver.quit();
    //            driver = null;
    //        }
    //    }

    protected void waitForPageLoad(String titleContains) throws MalformedURLException {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.titleContains(titleContains));
        waitForReadyStateComplete();
    }

    protected void waitForPageLoadThenValidate(AuthenticationJourneyPages page)
            throws MalformedURLException {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(Driver.get().getCurrentUrl()).getPath());
        assertEquals(page.getFullTitle(), Driver.get().getTitle());
    }

    protected void findAndClickContinue() throws MalformedURLException {
        waitForReadyStateComplete();
        WebElement continueButton =
                Driver.get()
                        .findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    protected void findAndClickContinueWelsh() throws MalformedURLException {
        waitForReadyStateComplete();
        WebElement continueButton =
                Driver.get()
                        .findElement(By.cssSelector("#main-content > div > div > form > button"));
        continueButton.click();
    }

    protected void findAndClickButtonByText(String buttonText) throws MalformedURLException {
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

    public void switchDefaultTimeout(String status) throws MalformedURLException {
        switch (status.toLowerCase()) {
            case "on":
                Driver.get().manage().timeouts().implicitlyWait(DEFAULT_PAGE_LOAD_WAIT_TIME);
                break;
            case "off":
                Driver.get().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                break;
        }
    }

    protected boolean isLinkTextDisplayedImmediately(String linkText) throws MalformedURLException {
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

    public void waitForThisErrorMessage(String expectedMessage) throws MalformedURLException {
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

    public void waitForThisText(String expectedText) throws MalformedURLException {
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

    protected void pressBack() throws MalformedURLException {
        Driver.get().findElement(By.xpath("//a[text()[normalize-space() = 'Back']]")).click();
    }

    protected void selectLinkByText(String linkText) throws MalformedURLException {
        Driver.get()
                .findElement(By.xpath("//*[text()[normalize-space() = '" + linkText + "']]"))
                .click();
    }

    protected void clearFieldAndEnter(By ele, String text) throws MalformedURLException {
        Driver.get().findElement(ele).clear();
        Driver.get().findElement(ele).sendKeys(text);
    }

    public Boolean isErrorSummaryDisplayed() throws MalformedURLException {
        return Driver.get().findElement(By.className("govuk-error-summary")).isDisplayed();
    }

    public String getPageHeading() throws MalformedURLException {
        return Driver.get().findElement(By.cssSelector("h1")).getText().trim();
    }

    public void checkForNewTabAndGoToIt(String newTabTitle) throws MalformedURLException {
        switchToTabByIndex(1);
        waitForPageLoad(newTabTitle);
    }

    public void switchToTabByIndex(Integer idx) throws MalformedURLException {
        ArrayList<String> tabs = new ArrayList<String>(Driver.get().getWindowHandles());
        Driver.get().switchTo().window(tabs.get(idx));
    }

    public void switchToTabWithTitleContaining(String titleToSwitchTo)
            throws MalformedURLException {
        Set<String> allTabs = Driver.get().getWindowHandles();
        for (String tab : allTabs) {
            String title = Driver.get().switchTo().window(tab).getTitle();
            if (title.contains(titleToSwitchTo)) {
                break;
            }
        }
    }

    protected void closeActiveTab() throws MalformedURLException {
        Driver.get().close();
    }

    public void enterCorrectAuthAppCodeAndContinue(String authAppSecretKey)
            throws MalformedURLException {
        String authAppCode = AuthAppStub.getAuthAppCode(authAppSecretKey);
        clearFieldAndEnter(authAppCodeField, authAppCode);
        findAndClickContinue();
    }

    public void setAnalyticsCookieTo(Boolean state) throws MalformedURLException {
        Driver.get()
                .manage()
                .addCookie(new Cookie("cookies_preferences_set", "{\"analytics\":" + state + "}"));
    }

    public void waitUntilElementClickable(By by) throws MalformedURLException {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.elementToBeClickable(Driver.get().findElement(by)));
    }

    public void waitUntilElementClickable(WebElement element) throws MalformedURLException {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForReadyStateComplete() throws MalformedURLException {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        (ExpectedCondition<Boolean>)
                                wd ->
                                        ((JavascriptExecutor) wd)
                                                .executeScript("return document.readyState")
                                                .equals("complete"));
    }
}

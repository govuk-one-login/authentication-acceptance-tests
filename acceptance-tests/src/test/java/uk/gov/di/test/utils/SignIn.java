package uk.gov.di.test.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignIn {
    protected static final String CHROME_BROWSER = "chrome";
    protected static final String FIREFOX_BROWSER = "firefox";
    protected static final String SELENIUM_URL =
            System.getenv().getOrDefault("SELENIUM_URL", "http://localhost:4445/wd/hub");

    protected static final String IDP_URL =
            System.getenv()
                    .getOrDefault(
                            "IDP_URL",
                            "https://front.build.auth.ida.digital.cabinet-office.gov.uk/");
    protected static final String RP_URL =
            System.getenv()
                    .getOrDefault(
                            "RP_URL",
                            "https://di-auth-stub-relying-party-build-s2.london.cloudapps.digital/");

    protected static final String TEST_USER_EMAIL =
            System.getenv().getOrDefault("TEST_USER_EMAIL", "");

    protected static final String TEST_USER_PASSWORD =
            System.getenv().getOrDefault("TEST_USER_PASSWORD", "");

    protected static final String TEST_USER_PHONE_CODE =
            System.getenv().getOrDefault("TEST_USER_PHONE_CODE", "");
    protected static final Boolean SELENIUM_LOCAL =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_LOCAL", "false"));
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_HEADLESS", "false"));
    protected static final Boolean DEBUG_MODE =
            Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG_MODE", "false"));
    protected static final String SELENIUM_BROWSER =
            System.getenv().getOrDefault("SELENIUM_BROWSER", FIREFOX_BROWSER);
    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.of(20, SECONDS);
    protected static WebDriver driver;

    protected void setupWebdriver() throws MalformedURLException {
        if (driver == null) {
            switch (SELENIUM_BROWSER) {
                case CHROME_BROWSER:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(SELENIUM_HEADLESS);
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    if (SELENIUM_LOCAL) {
                        driver = new ChromeDriver(chromeOptions);
                    } else {
                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), chromeOptions);
                    }
                    break;
                default:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(SELENIUM_HEADLESS);
                    firefoxOptions.setPageLoadTimeout(Duration.of(30, SECONDS));
                    firefoxOptions.setImplicitWaitTimeout(Duration.of(30, SECONDS));
                    if (SELENIUM_LOCAL) {
                        driver = new FirefoxDriver(firefoxOptions);
                    } else {
                        driver = new RemoteWebDriver(new URL(SELENIUM_URL), firefoxOptions);
                    }
            }
        }
    }

    protected void closeWebdriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected void waitForPageLoad(String titleContains) {
        new WebDriverWait(driver, DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(ExpectedConditions.titleContains(titleContains));
    }

    protected void waitForPageLoadThenValidate(AuthenticationJourneyPages page) {
        waitForPageLoad(page.getShortTitle());
        assertEquals(page.getRoute(), URI.create(driver.getCurrentUrl()).getPath());
        assertEquals(page.getFullTitle(), driver.getTitle());
    }

    protected void findAndClickContinue() {
        WebElement continueButton =
                driver.findElement(By.xpath("//button[text()[normalize-space() = 'Continue']]"));
        continueButton.click();
    }

    protected void findAndClickContinueWelsh() {
        WebElement continueButton =
                driver.findElement(By.cssSelector("#main-content > div > div > form > button"));
        continueButton.click();
    }

    protected void findAndClickButtonByText(String buttonText) {
        WebElement continueButton =
                driver.findElement(
                        By.xpath("//button[text()[normalize-space() = '" + buttonText + "']]"));
        continueButton.click();
    }
}

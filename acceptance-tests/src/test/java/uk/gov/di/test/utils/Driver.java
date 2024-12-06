package uk.gov.di.test.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Driver {
    protected static final String FIREFOX_BROWSER = "firefox";
    protected static final String SELENIUM_URL = Environment.getOrThrow("SELENIUM_URL");
    protected static final Boolean SELENIUM_LOCAL =
            Boolean.valueOf(System.getenv().getOrDefault("SELENIUM_LOCAL", "true"));
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.valueOf(Environment.getOrThrow("SELENIUM_HEADLESS"));
    protected static final String SELENIUM_BROWSER = Environment.getOrThrow("SELENIUM_BROWSER");
    protected static WebDriver driver;
    private static final InheritableThreadLocal<WebDriver> driverPool =
            new InheritableThreadLocal<>();

    public static WebDriver getDriver() {
        return driverPool.get();
    }

    public static WebDriver get() {

        if (driverPool.get() == null)
            synchronized (Driver.class) {
                switch (SELENIUM_BROWSER) {
                    case "chrome" -> {
                        ChromeOptions chromeOptions = buildChromeOptions();
                        if (SELENIUM_LOCAL) {
                            System.setProperty("webdriver.chrome.whitelistedIps", "");
                            driverPool.set(new ChromeDriver(chromeOptions));
                        } else {
                            try {
                                driverPool.set(
                                        new RemoteWebDriver(
                                                new URL(SELENIUM_URL), chromeOptions, false));
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    case FIREFOX_BROWSER -> {
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (SELENIUM_HEADLESS) {
                            firefoxOptions.addArguments("--headless");
                        }
                        firefoxOptions.setPageLoadTimeout(Duration.of(30, SECONDS));
                        firefoxOptions.setImplicitWaitTimeout(Duration.of(30, SECONDS));
                        if (SELENIUM_LOCAL) {
                            driverPool.set(new FirefoxDriver(firefoxOptions));
                        } else {
                            try {
                                driverPool.set(
                                        new RemoteWebDriver(
                                                new URL(SELENIUM_URL), firefoxOptions, false));
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    default -> throw new RuntimeException("Invalid driver: " + SELENIUM_BROWSER);
                }
                driverPool.get().manage().deleteAllCookies();
                driverPool.get().manage().window().maximize();
            }
        return driverPool.get();
    }

    private static ChromeOptions buildChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        if (SELENIUM_HEADLESS) {
            chromeOptions.addArguments("--headless=new");
        }
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--incognito");
        return chromeOptions;
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}

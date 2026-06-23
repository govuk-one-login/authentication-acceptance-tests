package uk.gov.di.test.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.virtualauthenticator.HasVirtualAuthenticator;

import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
    protected static final String SELENIUM_URL = Environment.getOrThrow("SELENIUM_URL");
    protected static final Boolean SELENIUM_LOCAL =
            Boolean.valueOf(System.getenv().getOrDefault("SELENIUM_LOCAL", "true"));
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.valueOf(Environment.getOrThrow("SELENIUM_HEADLESS"));
    private static final InheritableThreadLocal<WebDriver> driverPool =
            new InheritableThreadLocal<>();

    public static WebDriver getDriver() {
        return driverPool.get();
    }

    public static WebDriver get() {

        if (driverPool.get() == null)
            synchronized (Driver.class) {
                ChromeOptions chromeOptions = buildChromeOptions();
                if (SELENIUM_LOCAL) {
                    System.setProperty("webdriver.chrome.whitelistedIps", "");
                    driverPool.set(new ChromeDriver(chromeOptions));
                } else {
                    try {
                        driverPool.set(new RemoteWebDriver(new URL(SELENIUM_URL), chromeOptions));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
                driverPool.get().manage().deleteAllCookies();
                driverPool
                        .get()
                        .manage()
                        .window()
                        .setSize(new org.openqa.selenium.Dimension(1200, 800));
                driverPool
                        .get()
                        .manage()
                        .window()
                        .setPosition(new org.openqa.selenium.Point(100, 100));
            }
        return driverPool.get();
    }

    public static HasVirtualAuthenticator getAsAuthenticator() {
        var d = get();
        if (d instanceof HasVirtualAuthenticator) {
            return (HasVirtualAuthenticator) d;
        }
        throw new UnsupportedOperationException(
                "The current driver does not support Virtual Authenticators");
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

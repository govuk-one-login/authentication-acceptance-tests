package uk.gov.di.test.utils;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class Driver {
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.valueOf(Environment.getOrThrow("SELENIUM_HEADLESS"));
    private static final InheritableThreadLocal<ChromeDriver> driverPool =
            new InheritableThreadLocal<>();

    public static ChromeDriver getDriver() {
        return driverPool.get();
    }

    public static ChromeDriver get() {

        if (driverPool.get() == null)
            synchronized (Driver.class) {
                ChromeOptions chromeOptions = buildChromeOptions();
                File chromedriver = new File("/usr/bin/chromedriver");
                if (chromedriver.exists()) {
                    System.setProperty("webdriver.chrome.driver", chromedriver.getAbsolutePath());
                }
                System.setProperty("webdriver.chrome.whitelistedIps", "");
                driverPool.set(new ChromeDriver(chromeOptions));
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

package uk.gov.di.test.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Driver {

    private Driver() {}

    protected static final String CHROME_BROWSER = "chrome";
    protected static final String FIREFOX_BROWSER = "firefox";
    protected static final String SELENIUM_URL =
            System.getenv().getOrDefault("SELENIUM_URL", "http://localhost:4445/wd/hub");
    protected static final Boolean SELENIUM_LOCAL =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_LOCAL", "false"));
    protected static final Boolean SELENIUM_HEADLESS =
            Boolean.parseBoolean(System.getenv().getOrDefault("SELENIUM_HEADLESS", "false"));
    protected static final String SELENIUM_BROWSER =
            System.getenv().getOrDefault("SELENIUM_BROWSER", FIREFOX_BROWSER);
    protected static final Duration DEFAULT_PAGE_LOAD_WAIT_TIME = Duration.of(20, SECONDS);
    protected static WebDriver driver;

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    private static Driver instance = new Driver();

    public static Driver getInstance() {
        return instance;
    }

    public static WebDriver getDriver() {
        return driverPool.get();
    }

    public static WebDriver get() throws MalformedURLException {

        if (driverPool.get() == null)
            synchronized (Driver.class) {

                // String browser = ConfigurationReader.getBrowser();
                // System.setProperty("webdriver.http.factory", "jdk-http-client");

                switch (SELENIUM_BROWSER) {
                    case "chrome":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.setHeadless(SELENIUM_HEADLESS);
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--disable-extensions");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        if (SELENIUM_LOCAL) {
                            System.setProperty("webdriver.chrome.whitelistedIps", "");
                            driverPool.set(new ChromeDriver(chromeOptions));
                        } else {
                            driverPool.set(
                                    new RemoteWebDriver(new URL(SELENIUM_URL), chromeOptions));
                        }
                        // driverPool.get().manage().window().maximize();
                        break;
                        //                case "chrome-headless":
                        //                    ChromeOptions chromeOptions = new ChromeOptions();
                        //                    chromeOptions.addArguments("--headless=new");
                        //                    if (ConfigurationReader.noChromeSandbox()) {
                        //                        // no-sandbox is needed for chrome-headless when
                        // running in a container due to restricted syscalls
                        //                        chromeOptions.addArguments("--no-sandbox");
                        //                    }
                        //                    //
                        // chromeOptions.addArguments("--remote-allow-origins=*");
                        //                    driverPool.set(new ChromeDriver(chromeOptions));
                        //                    break;
                        //                case "firefox":
                        //                    driverPool.set(new FirefoxDriver());
                        //                    break;
                        //                case "firefox-headless":
                        //                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                        //                    firefoxOptions.addArguments("--headless");
                        //                    driverPool.set(new FirefoxDriver(firefoxOptions));
                        //                    break;
                        //                case "ie":
                        //                    if
                        // (!System.getProperty("os.name").toLowerCase().contains("windows"))
                        //                        throw new WebDriverException("Your OS doesn't
                        // support Internet Explorer");
                        //                    driverPool.set(new InternetExplorerDriver());
                        //                    break;
                        //
                        //                case "edge":
                        //                    if
                        // (!System.getProperty("os.name").toLowerCase().contains("windows"))
                        //                        throw new WebDriverException("Your OS doesn't
                        // support Edge");
                        //                    driverPool.set(new EdgeDriver());
                        //                    break;
                        //
                        //                case "safari":
                        //                    if
                        // (!System.getProperty("os.name").toLowerCase().contains("mac"))
                        //                        throw new WebDriverException("Your OS doesn't
                        // support Safari");
                        //                    driverPool.set(new SafariDriver());
                        //                    break;
                }
            }
        return driverPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}

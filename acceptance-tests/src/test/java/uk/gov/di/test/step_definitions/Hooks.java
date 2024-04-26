package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import uk.gov.di.test.pages.BasePage;

import java.net.MalformedURLException;

public class Hooks extends BasePage {

    private static int failureCount = 0;

    protected static final Boolean FAIL_FAST_ENABLED =
            Boolean.parseBoolean(System.getenv().getOrDefault("FAIL_FAST_ENABLED", "false"));

    @Before(order = 2)
    public void setupWebdriver() throws MalformedURLException {
        if (failureCount == 0) {
            super.setupWebdriver();
            driver.manage().deleteAllCookies();
        }
    }

    @Before(order = 1)
    public void setUpScenario(Scenario scenario) {
        BasePage.scenario = scenario;
        if (FAIL_FAST_ENABLED) {
            if (failureCount > 0) {
                throw new PendingException();
            }
        }
    }

    @AfterStep
    public void checkAccessibility() {
        AxeStepDef.thereAreNoAccessibilityViolations();
    }

    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure screenshot");

            super.closeWebdriver();
            if (FAIL_FAST_ENABLED) {
                failureCount++;
            }
        }
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Hooks extends BasePage {

    private static int failureCount = 0;

    protected static final Boolean FAIL_FAST_ENABLED =
            Boolean.parseBoolean(TEST_CONFIG_SERVICE.getOrDefault("FAIL_FAST_ENABLED", "false"));

    @Before(value = "@API", order = 1)
    public void setUpScenario(Scenario scenario) {
        BasePage.scenario = scenario;
        if (FAIL_FAST_ENABLED && failureCount > 0) {
            throw new PendingException();
        }
    }

    @AfterStep("@API")
    public void checkAccessibility() {
        AxeStepDef.thereAreNoAccessibilityViolations();
    }

    @After("@API")
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {

            WebDriver driver = Driver.get();

            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(
                    screenshot,
                    "image/png",
                    "Failure screenshot - "
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                    .format(Calendar.getInstance().getTime()));

            if (FAIL_FAST_ENABLED) {
                failureCount++;
            }
        }
        Driver.closeDriver();
    }
}

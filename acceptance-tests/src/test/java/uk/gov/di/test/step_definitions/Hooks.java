package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static uk.gov.di.test.step_definitions.UserLifecycleStepDef.userLifecycleService;

public class Hooks extends BasePage {
    private static final Logger LOG = LogManager.getLogger(Hooks.class);
    private final World world;
    private static int failureCount = 0;

    protected static final Boolean FAIL_FAST_ENABLED =
            Boolean.parseBoolean(TEST_CONFIG_SERVICE.getOrDefault("FAIL_FAST_ENABLED", "false"));

    public Hooks(World world) {
        this.world = world;
    }

    @Before(value = "@UI", order = 1)
    public void setUpScenario(Scenario scenario) {
        BasePage.scenario = scenario;
        if (FAIL_FAST_ENABLED && failureCount > 0) {
            throw new PendingException();
        }
    }

    @AfterStep("@UI")
    public void checkAccessibility() {
        AxeStepDef.thereAreNoAccessibilityViolations();
    }

    @After("@UI")
    public void afterEachUI() {
        Driver.get().manage().deleteAllCookies();
    }

    @After("@UI")
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

    @After("@API")
    public void theUserIsDeleted() {
        if (world.userProfile != null) {
            LOG.info(
                    "After API hook: Deleting user profile with email {}",
                    world.userProfile.getEmail());
            userLifecycleService.deleteUserProfileFromDynamodb(world.userProfile);
        }
        if (world.userCredentials != null) {
            LOG.info(
                    "After API hook: Deleting user credentials with email {}",
                    world.userCredentials.getEmail());
            userLifecycleService.deleteUserCredentialsFromDynamodb(world.userCredentials);
        }
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;

public class Hooks extends BasePage {

    @Before
    public void setupWebdriver() {
        // super.setupWebdriver();
        // driver.manage().deleteAllCookies();
    }

    // @AfterStep
    // public void checkAccessibility() {
    //    AxeStepDef.thereAreNoAccessibilityViolations();
    // }

    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {

            WebDriver driver = Driver.get();

            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure screenshot");

            // Driver.closeDriver();
        }
        Driver.closeDriver();
    }
}

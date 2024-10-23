package uk.gov.di.test.step_definitions;

import com.deque.axe.AXE;
import io.cucumber.java.en.And;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Driver;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class AxeStepDef extends BasePage {

    private static final URL scriptUrl = AxeStepDef.class.getResource("/axe.min.js");
    protected static final Boolean ACCESSIBILITY_CHECKS =
            Boolean.parseBoolean(TEST_CONFIG_SERVICE.getOrDefault("ACCESSIBILITY_CHECKS", "false"));

    @And("there are no accessibility violations")
    public static void thereAreNoAccessibilityViolations() {
        //        System.out.println(ACCESSIBILITY_CHECKS);
        if (ACCESSIBILITY_CHECKS) {
            System.out.println("Page in test = " + Driver.get().getTitle());

            JSONObject responseJSON =
                    new AXE.Builder(Driver.get(), scriptUrl)
                            .options("{ runOnly: { type: 'tag', values: ['wcag2a','wcag21aa'] } }")
                            .analyze();

            JSONArray violations = responseJSON.getJSONArray("violations");
            assertEquals(violations.length(), 0);
        }
    }
}

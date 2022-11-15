package uk.gov.di.test.step_definitions;

import com.deque.axe.AXE;
import io.cucumber.java.en.And;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.di.test.utils.SignIn;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class Axe extends SignIn {

    private static final URL scriptUrl = Axe.class.getResource("/axe.min.js");
    protected static final Boolean ACCESSIBILITY_CHECKS =
            Boolean.parseBoolean(System.getenv().getOrDefault("ACCESSIBILITY_CHECKS", "false"));

    @And("there are no accessibility violations")
    public void thereAreNoAccessibilityViolations() {
        System.out.println(ACCESSIBILITY_CHECKS);
        if (ACCESSIBILITY_CHECKS) {
            System.out.println("Page in test = " + driver.getTitle());

            JSONObject responseJSON =
                    new AXE.Builder(driver, scriptUrl)
                            .options("{ runOnly: { type: 'tag', values: ['wcag2a','wcag21aa'] } }")
                            .analyze();

            JSONArray violations = responseJSON.getJSONArray("violations");
            assertEquals(violations.length(), 0);
        }
    }
}

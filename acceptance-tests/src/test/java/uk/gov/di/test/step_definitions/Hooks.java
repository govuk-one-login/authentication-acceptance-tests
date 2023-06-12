package uk.gov.di.test.step_definitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;

import uk.gov.di.test.utils.SignIn;

import java.net.MalformedURLException;

public class Hooks extends SignIn {
    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @AfterStep
    public void checkAccessibility() {
        Axe.thereAreNoAccessibilityViolations();
    }
}

package uk.gov.di.test.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@Test123",
        plugin = {"pretty", "html:target/cucumber-report/index.html"},
        features = {"src/test/resources/uk/gov/di/test/features"},
        glue = {"uk.gov.di.test.step_definitions"})
public class RunCucumberTest {}

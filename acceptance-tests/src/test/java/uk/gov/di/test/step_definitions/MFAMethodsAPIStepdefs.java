package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MFAMethodsAPIStepdefs {
    private static final Logger LOG = LogManager.getLogger(MFAMethodsAPIStepdefs.class);

    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() {
        LOG.info("Retrieving request is made to the API not implemented yet");
    }
}

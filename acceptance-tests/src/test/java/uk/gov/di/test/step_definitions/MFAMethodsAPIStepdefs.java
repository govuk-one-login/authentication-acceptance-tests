package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.di.test.services.UserLifecycleService;

import java.text.ParseException;

public class MFAMethodsAPIStepdefs {
    private static final Logger LOG = LogManager.getLogger(MFAMethodsAPIStepdefs.class);

    private final World world;

    private static final UserLifecycleService userLifecycleService =
            UserLifecycleService.getInstance();

    public MFAMethodsAPIStepdefs(World world) {
        this.world = world;
    }

    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() throws ParseException, JOSEException {
        LOG.info("Retrieving request is made to the API not implemented yet");
    }
}

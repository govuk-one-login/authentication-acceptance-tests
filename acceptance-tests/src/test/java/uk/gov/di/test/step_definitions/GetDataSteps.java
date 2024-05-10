package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Given;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.Dynamo;

public class GetDataSteps extends BasePage {
    public Dynamo dynamo = new Dynamo();

    @Given("get info for email address {string}")
    public void getInfoForEmailAddress(String emailAddress) {
        // dynamo.getUser(emailAddress);
        // dynamo.createOrUpdateUser(emailAddress);
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Given;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.TestData;

public class GetDataSteps extends BasePage {

    public TestData testData = new TestData();


    @Given("set up data for user {string}")
    public void setupDataForTest(String emailAddressDataTag) {
        testData.SetupUserData(emailAddressDataTag);
    }
}

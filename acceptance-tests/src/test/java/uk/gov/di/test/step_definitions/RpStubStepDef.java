package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.StubStartPage;

public class RpStubStepDef extends BasePage {

    StubStartPage stubStartPage = StubStartPage.getStubStartPage();

    @ParameterType("\\[((?:[\\w-_]+,?)+)\\]")
    public String[] rpStubOptions(String opts) {
        if (opts == null || opts.isEmpty() || opts.equalsIgnoreCase("default")) {
            return null;
        }
        return opts.split(",");
    }

    // DEFAULT OPTIONS
    @When(
            "the user comes from the stub relying party with default options and is taken to the {string} page")
    public void theUserVisitsTheStubRelyingPartyAndIsTakenToPage(String pageTitle) {
        stubStartPage.useRpStubAndWaitForPage(pageTitle);
    }

    // MULTIPLE OPTIONS
    @When(
            "the user comes from the stub relying party with options: {rpStubOptions} and is taken to the {string} page")
    public void theUserVisitsTheStubRelyingPartyWithOptionsAndIsTakenToThePage(
            String[] rpStubOptions, String pageTitle) {
        stubStartPage.useRpStubAndWaitForPage(pageTitle, rpStubOptions);
    }

    // SINGLE OPTION
    @When(
            "the user comes from the stub relying party with option {word} and is taken to the {string} page")
    public void theUserVisitsTheStubRelyingPartyWithOneOptionAndIsTakenToPage(
            String option, String pageTitle) {
        theUserVisitsTheStubRelyingPartyWithOptionsAndIsTakenToThePage(
                new String[] {option}, pageTitle);
    }

    @Then("the user is forcibly logged out")
    @Then("the logged-in User is forcibly logged out")
    public void theUserIsLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }

    @When("the user comes from the stub relying party with {word} options and is taken to the {string} page")
    public void theUserComesFromTheStubRelyingPartyWithStrategicAppOptionsAndIsTakenToThePage(
            String[] rpStubOptions, String pageTitle) {
        stubStartPage.useRpStubAndWaitForPage(pageTitle, rpStubOptions);
    }
}

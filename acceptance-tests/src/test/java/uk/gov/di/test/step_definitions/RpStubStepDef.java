package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriverException;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.pages.StubOrchestrationPage;
import uk.gov.di.test.pages.StubStartPage;
import uk.gov.di.test.utils.RetryHelper;
import uk.gov.di.test.utils.SessionContextExceptions;

public class RpStubStepDef extends BasePage {

    public StubStartPage rpStubPage =
            USE_STUB_ORCH ? new StubOrchestrationPage() : new RpStubPage();

    private static final RetryHelper retryHelper = RetryHelper.getInstance();
    private static final Integer RETRY_LIMIT = 5;

    public static class RPStubRetryException extends RuntimeException {
        public RPStubRetryException(Throwable e) {
            super(
                    String.format(
                            "Failed to complete RP stub things after %s attempts", RETRY_LIMIT),
                    e);
        }
    }

    public void doRpStubAndWaitForSignInPage(
            String rpStubOptions, Throwable lastException, String pageTitle) {
        if (lastException != null) {
            Integer currentRetries = retryHelper.getAndIncrement("come-from-rp-stub");
            if (currentRetries > RETRY_LIMIT) {
                throw new RPStubRetryException(lastException);
            }
            System.out.printf("Retrying RP stub, retry %s of %s%n", currentRetries, RETRY_LIMIT);
        }

        try {
            rpStubPage.goToRpStub();
            rpStubPage.selectRpOptionsByIdAndContinue(rpStubOptions);
            setAnalyticsCookieTo(false);
            waitForPageLoad(pageTitle);
        } catch (SessionContextExceptions.SessionContextException | WebDriverException e) {
            doRpStubAndWaitForSignInPage(rpStubOptions, e, pageTitle);
        }
    }

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        doRpStubAndWaitForSignInPage(options, null, "Create your GOV.UK One Login or sign in");
    }

    @When(
            "the user comes from the stub relying party with options: {string} and the user is taken to the {string} page")
    public void theExistingUserVisitsTheStubRelyingPartyAndReturnsToPage(
            String option, String pageTitle) {
        doRpStubAndWaitForSignInPage(option, null, pageTitle);
    }

    @Then("the user is forcibly logged out")
    public void theUserIsLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }

    @When("the user uplifts having already logged in")
    public void whenTheUserUpliftsHavingAlreadyLoggedIn() {
        rpStubPage.uplift("");
    }
}

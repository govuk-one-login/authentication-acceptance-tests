package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriverException;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.RpStubPage;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.RetryHelper;
import uk.gov.di.test.utils.SessionContextExceptions;

public class RpStubStepDef extends BasePage {

    RpStubPage rpStubPage = new RpStubPage();

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

    public void doRpStubAndWaitForSignInPage(String rpStubOptions, Throwable lastException) {
        Integer attempt = retryHelper.getAndIncrement("come-from-rp-stub");
        if (attempt > RETRY_LIMIT) {
            throw new RPStubRetryException(lastException);
        }
        if (attempt > 1) {
            Driver.get().manage().deleteAllCookies();
            System.out.printf("Retrying RP stub, attempt %s%n", attempt);
        }

        try {
            rpStubPage.goToRpStub();
            rpStubPage.selectRpOptionsByIdAndContinue(rpStubOptions);
            setAnalyticsCookieTo(false);
            waitForPageLoad("Create your GOV.UK One Login or sign in");
        } catch (SessionContextExceptions.SessionContextException | WebDriverException e) {
            doRpStubAndWaitForSignInPage(rpStubOptions, e);
        }
    }

    public void doRpStubAndWaitForSignInPage(String rpStubOptions) {
        doRpStubAndWaitForSignInPage(rpStubOptions, null);
    }

    @When("the user comes from the stub relying party with options: {string}")
    public void theExistingUserVisitsTheStubRelyingParty(String options) {
        doRpStubAndWaitForSignInPage(options);
    }

    @Then("the user is forcibly logged out")
    public void theUserIsLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }
}

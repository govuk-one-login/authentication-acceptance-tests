package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import uk.gov.di.test.utils.Driver;
import uk.gov.di.test.utils.RetryHelper;
import uk.gov.di.test.utils.SessionContextExceptions;

import java.util.Objects;

public abstract class StubStartPage extends BasePage {
    protected static final RetryHelper RETRY_HELPER = RetryHelper.getInstance();
    protected static final Integer RETRY_LIMIT = 5;

    protected static final String RP_URL = TEST_CONFIG_SERVICE.get("RP_URL");

    public void goToRpStub() {
        System.out.println("Go to RP stub page: " + RP_URL);
        Driver.get().get(RP_URL);
        waitForStubToLoad();
    }

    public enum StubType {
        ORCHESTRATION,
        LEGACY
    }

    public abstract void waitForStubToLoad();

    public abstract By getReauthIdField();

    public abstract By getContinueButton();

    public abstract void selectRpOptionsById(String[] opts);

    public abstract void reauthRequired(String idToken);

    public static class RPStubRetryException extends RuntimeException {

        public RPStubRetryException(Throwable e) {
            super(
                    String.format(
                            "Failed to complete RP stub things after %s attempts", RETRY_LIMIT),
                    e);
        }
    }

    public void useRpStubAndWaitForPage(
            String pageTitle, String[] rpStubOptions, Throwable lastException) {
        if (lastException != null) {
            Integer currentRetries = RETRY_HELPER.getAndIncrement("come-from-rp-stub");
            if (currentRetries > RETRY_LIMIT) {
                throw new LegacyStubStartPage.RPStubRetryException(lastException);
            }
            System.out.printf("Retrying RP stub, retry %s of %s%n", currentRetries, RETRY_LIMIT);
        }

        try {
            goToRpStub();
            selectRpOptionsByIdAndContinue(rpStubOptions);
            setAnalyticsCookieTo(false);
            waitForPageLoad(pageTitle);
        } catch (SessionContextExceptions.SessionContextException | WebDriverException e) {
            useRpStubAndWaitForPage(pageTitle, rpStubOptions, e);
        }
    }

    public void useRpStubAndWaitForPage(String pageTitle) {
        useRpStubAndWaitForPage(pageTitle, new String[] {}, null);
    }

    public void useRpStubAndWaitForPage(String pageTitle, String[] rpStubOptions) {
        useRpStubAndWaitForPage(pageTitle, rpStubOptions, null);
    }

    public void useDefaultOptionsAndContinue() {
        selectRpOptionsByIdAndContinue(new String[] {});
    }

    public void selectRpOptionsByIdAndContinue(String[] opts) {
        selectRpOptionsById(opts);
        findAndClickButton(getContinueButton());
    }

    protected void enterReauthIdToken(String token) {
        Driver.get().findElement(getReauthIdField()).clear();
        Driver.get().findElement(getReauthIdField()).sendKeys(token);
    }

    public static StubType getStubType() {
        return Objects.equals(TEST_CONFIG_SERVICE.get("STUB_RP_TYPE"), "ORCHESTRATION")
                ? StubType.ORCHESTRATION
                : StubType.LEGACY;
    }

    public static StubStartPage getStubStartPage() {
        if (Objects.requireNonNull(getStubType()) == StubType.ORCHESTRATION) {
            return new OrchestrationStubStartPage();
        }
        return new LegacyStubStartPage();
    }
}

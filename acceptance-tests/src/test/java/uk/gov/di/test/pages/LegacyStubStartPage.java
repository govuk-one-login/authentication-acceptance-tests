package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.util.HashSet;
import java.util.List;

public class LegacyStubStartPage extends StubStartPage {
    List<String> ignoredOptions =
            List.of(
                    "authenticated-2",
                    "authenticated-level"); // Options only used by orchestration stub

    protected LegacyStubStartPage() {
        // protected constructor to prevent direct instantiation
    }

    @Override
    public void waitForStubToLoad() {
        waitForThisText("Request Object");
    }

    @Override
    public By getReauthIdField() {
        return By.id("reauth-id-token");
    }

    @Override
    public By getContinueButton() {
        return By.xpath("//button[text()[normalize-space() = 'Continue']]");
    }

    @Override
    public void selectRpOptionsById(String[] opts) {
        HashSet<String> set = new HashSet<>(List.of(opts));
        for (String id : set) {
            if (ignoredOptions.contains(id)) {
                continue;
            }
            Driver.get().findElement(By.id(id)).click();
        }
    }

    @Override
    public void reauthRequired(String reauthCorrelationToken) {
        goToRpStub();
        enterReauthIdToken(reauthCorrelationToken);
        selectRpOptionsByIdAndContinue(new String[] {"2fa-on", "prompt-login", "request-object"});
        setAnalyticsCookieTo(false);
    }

    @Override
    public void waitForForciblyLoggedOut() {
        waitForThisText("Error in Callback");
        waitForThisText("Error: login_required");
        waitForThisText("Error description: Login required");
    }
}

package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.util.HashSet;
import java.util.List;

public class OrchestrationStubStartPage extends StubStartPage {

    public static final String TITLE =
            "GOV.UK - The best place to find government services and information";

    protected OrchestrationStubStartPage() {
        // protected constructor to prevent direct instantiation
    }

    @Override
    public void waitForStubToLoad() {
        waitForPageLoad(TITLE);
    }

    @Override
    public By getReauthIdField() {
        return By.id("reauthenticate");
    }

    @Override
    public By getContinueButton() {
        return By.xpath("//button[text()[normalize-space() = 'Submit']]");
    }

    @Override
    public void useDefaultOptionsAndContinue() {
        selectRpOptionsByIdAndContinue(new String[] {"prompt-none"});
    }

    @Override
    public void selectRpOptionsById(String[] opts) {
        HashSet<String> set = new HashSet<>(List.of(opts));
        for (String id : set) {
            switch (id) {
                case "2fa-on":
                    id = "level";
                    break;
                case "2fa-off":
                    id = "level-2";
                    break;
                case "prompt-login":
                    id = "authenticated";
                    break;
                case "prompt-none":
                    id = "authenticated-2";
                    break;
                default:
                    break;
            }
            Driver.get().findElement(By.id(id)).click();
        }
    }

    @Override
    public void reauthRequired(String idToken) {
        goToRpStub();
        enterReauthIdToken(idToken);
        findAndClickButton(getContinueButton());
        setAnalyticsCookieTo(false);
    }
}

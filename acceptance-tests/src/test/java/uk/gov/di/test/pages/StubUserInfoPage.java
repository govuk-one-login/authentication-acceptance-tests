package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import uk.gov.di.test.utils.Driver;

import java.util.Objects;

public abstract class StubUserInfoPage extends BasePage {

    public abstract String getIdToken();

    public abstract String getAccessToken();

    public abstract void logoutOfAccount();

    public abstract void waitForReturnToTheService();

    protected void waitForReturnToTheServiceDismissingPasskeyRegistration(
            String expectedPageTitle) {
        new WebDriverWait(Driver.get(), DEFAULT_PAGE_LOAD_WAIT_TIME)
                .until(
                        driver ->
                                driver.getCurrentUrl().contains("/create-passkey")
                                        || driver.getTitle().contains(expectedPageTitle));
        if (Driver.get().getCurrentUrl().contains("/create-passkey")) {
            Driver.get()
                    .findElement(By.xpath("//button[contains(text(), 'Skip for now')]"))
                    .click();
            waitForPageLoad(expectedPageTitle);
        }
    }

    public static StubUserInfoPage getStubUserInfoPage() {
        if (Objects.requireNonNull(StubStartPage.getStubType())
                == StubStartPage.StubType.ORCHESTRATION) {
            return new OrchestrationStubUserInfoPage();
        }
        return new LegacyStubUserInfoPage();
    }
}

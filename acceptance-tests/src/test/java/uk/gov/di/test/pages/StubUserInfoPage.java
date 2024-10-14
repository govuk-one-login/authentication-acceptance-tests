package uk.gov.di.test.pages;

import java.util.Objects;

public abstract class StubUserInfoPage extends BasePage {

    public abstract String getIdToken();

    public abstract String getAccessToken();

    public abstract void logoutOfAccount();

    public abstract void waitForReturnToTheService();

    public static StubUserInfoPage getStubUserInfoPage() {
        if (Objects.requireNonNull(StubStartPage.getStubType())
                == StubStartPage.StubType.ORCHESTRATION) {
            return new OrchestrationStubUserInfoPage();
        }
        return new LegacyStubUserInfoPage();
    }
}

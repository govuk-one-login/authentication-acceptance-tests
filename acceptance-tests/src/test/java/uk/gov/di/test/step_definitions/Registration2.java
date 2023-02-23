package uk.gov.di.test.step_definitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import uk.gov.di.test.utils.SignIn;
import uk.gov.di.test.utils.StepData;

import java.net.MalformedURLException;

public class Registration2 extends SignIn {
    private StepData stepData;

    public Registration2(StepData stepData) {
        this.stepData = stepData;
    }

    // LoginPage loginPage = new LoginPage();
    // AccountManagementPage accountManagementPage = new AccountManagementPage();
    // RegistrationPage registrationPage = new RegistrationPage();

    @Before
    public void setupWebdriver() throws MalformedURLException {
        super.setupWebdriver();
    }

    @And("a new user has valid credentials")
    public void theNewUserHasValidCredential() {
        stepData.emailAddress = System.getenv().get("TEST_USER_EMAIL");
        stepData.password = System.getenv().get("TEST_USER_PASSWORD");
        stepData.phoneNumber = System.getenv().get("TEST_USER_PHONE_NUMBER");
        stepData.sixDigitCodeEmail = System.getenv().get("TEST_USER_EMAIL_CODE");
        stepData.sixDigitCodePhone = System.getenv().get("TEST_USER_PHONE_CODE");
        stepData.tcEmailAddress = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_EMAIL");
        stepData.tcPassword = System.getenv().get("TERMS_AND_CONDITIONS_TEST_USER_PASSWORD");
        stepData.internationalPhoneNumber =
                System.getenv().get("TEST_USER_INTERNATIONAL_PHONE_NUMBER");
    }
}

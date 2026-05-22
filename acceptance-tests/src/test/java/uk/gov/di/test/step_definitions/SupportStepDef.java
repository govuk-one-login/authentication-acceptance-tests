package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.SupportPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupportStepDef extends BasePage {

    SupportPage supportPage = new SupportPage();

    @When("the user selects radio button {string}")
    public void theUserSelectsRadioButtonAndProceeds(String option) {
        supportPage.selectRadioButtonAndProceed(option);
    }

    @Then("User see updated text If you cannot access the phone number for your GOV.UK One Login")
    public void userSeeUpdatedTextIfYouCannotAccessThePhoneNumberForYourGOVUKOneLogin() {
        assertEquals(
                "If you no longer have access to this phone number, check if you can change how you get security codes.",
                supportPage.getLinkText());
    }

    @Then(
            "User see updated text If you no longer have access to your authenticator app, check if you can change how you get security codes")
    public void
            userSeeUpdatedTextIfYouNoLongerHaveAccessToYourAuthenticatorAppCheckIfYouCanChangeHowYouGetSecurityCodes() {
        assertEquals(
                "If you no longer have access to your authenticator app, check if you can change how you get security codes.",
                supportPage.getLinkAppText());
    }

    @And("the user selects radio button {string} and {string}")
    public void theUserSelectsRadioButtonAnd(String text, String mobilenumber) {
        supportPage.selectRadioButtonForPhoneAndProceed(text, mobilenumber);
    }
}

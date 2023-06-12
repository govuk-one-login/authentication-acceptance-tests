package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.SupportPage;
import uk.gov.di.test.utils.SignIn;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class Support extends SignIn {

    SupportPage supportPage = new SupportPage();

    @When("the user clicks the support link having not received their security code")
    @When("the user clicks the support link due to their security code not working")
    public void theUserClicksTheSupportLinkHavingNotReceivedTheirSecurityCode() {
        supportPage.clickSupportLink();
    }

    @Then("the user is taken to the Contact us page in a new tab")
    public void theUserIsTakenToTheContactUsPageInANewTab() {
        supportPage.checkForNewTabAndGoToIt("Contact us - GOV.UK One Login");
    }

    @When("the user selects {string}")
    public void theUserSelectsRadioButtonAndProceeds(String option) {
        supportPage.selectRadioButtonAndProceed(option);
    }

    @Then("the user is taken to the {string} page")
    public void theUserIsTakenToThePage(String pageTitle) {
        waitForPageLoad(pageTitle);
    }

    @And("the user enters more details")
    public void theUserEntersMoreDetails() {
        supportPage.enterMoreDetails("More detail text");
    }

    @And("the user selects {string} for email reply")
    public void theUserSelectsYesNoForEmailReply(String yesNo) {
        supportPage.canWeReplyViaEmail(yesNo);
    }

    @And("the user submits their message")
    public void theUserSubmitsTheirMessage() {
        supportPage.pressSendMessage();
    }

    @Then("the user receives confirmation that their message has been submitted")
    public void theUserReceivesConfirmationThatTheirMessageHasBeenSent() {
        assertTrue(supportPage.isSuccessMessageDisplayed());
        driver.close(); // close support tab (current tab)
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0)); // switch back to main tab
    }
}

package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.FooterPage;
import uk.gov.di.test.pages.SupportPage;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupportStepDef extends BasePage {

    SupportPage supportPage = new SupportPage();
    FooterPage footer = new FooterPage();

    @When("the user clicks on the support link")
    public void theUserClicksOnTheSupportLink() throws MalformedURLException {
        supportPage.clickSupportLink();
    }

    @When("the user selects the Welsh support link in the footer")
    public void theUserSelectsTheWelshSupportLink() throws MalformedURLException {
        footer.selectWelshSupportLink();
    }

    @Then("the user is taken to the Contact us page in a new tab")
    public void theUserIsTakenToTheContactUsPageInANewTab() throws MalformedURLException {
        supportPage.checkForNewTabAndGoToIt("Contact us - GOV.UK One Login");
    }

    @Then("the contact us page is displayed in Welsh")
    public void theContactUsPageIsDisplayedInWelsh() throws MalformedURLException {
        switchToTabWithTitleContaining("Cysylltu â ni - GOV.UK One Login");
        assertEquals("Cyfrif GOV.UK: rhoi gwybod am broblem neu roi adborth", getPageHeading());
        closeActiveTab();
        switchToTabWithTitleContaining("Creu eich GOV.UK One Login neu fewngofnodi");
    }

    @When("the user selects radio button {string}")
    public void theUserSelectsRadioButtonAndProceeds(String option) throws MalformedURLException {
        supportPage.selectRadioButtonAndProceed(option);
    }

    @And("the user enters more details")
    public void theUserEntersMoreDetails() throws MalformedURLException {
        supportPage.enterMoreDetails("More detail text");
    }

    @And("the user selects {string} for email reply")
    public void theUserSelectsYesNoForEmailReply(String yesNo) throws MalformedURLException {
        supportPage.canWeReplyViaEmail(yesNo);
    }

    @And("the user submits their message")
    public void theUserSubmitsTheirMessage() throws MalformedURLException {
        supportPage.pressSendMessage();
    }

    @Then("the user receives confirmation that their message has been submitted")
    public void theUserReceivesConfirmationThatTheirMessageHasBeenSent()
            throws MalformedURLException {
        assertTrue(supportPage.isSuccessMessageDisplayed());
        closeActiveTab();
        ArrayList<String> tabs = new ArrayList<String>(Driver.get().getWindowHandles());
        Driver.get().switchTo().window(tabs.get(0)); // switch back to main tab
    }
}

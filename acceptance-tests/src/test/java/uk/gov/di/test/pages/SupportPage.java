package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

public class SupportPage extends BasePage {

    By supportLink = By.xpath("//*[contains(text(), 'Support (opens in new tab)')]");
    By moreDetailsField = By.id("moreDetailDescription");
    By successMessage = By.cssSelector(".govuk-panel--confirmation");
    By mfaSupportLinkText = By.cssSelector("form[id='form-tracking'] p:nth-child(2)");
    By mfaSupportLinkAppText =
            By.cssSelector("div[class='govuk-details__text'] p[class='govuk-body']");

    public void clickSupportLink() {
        Driver.get().findElement(supportLink).click();
    }

    public void selectSupportRadioButtonByLabelText(String option) {
        Driver.get()
                .findElement(By.xpath("//div[label[contains(text(), '" + option + "')]]/input"))
                .click();
    }

    public void enterMoreDetails(String details) {
        Driver.get().findElement(moreDetailsField).sendKeys(details);
    }

    public void canWeReplyViaEmail(String option) {
        selectSupportRadioButtonByLabelText(option);
    }

    public void pressSendMessage() {
        findAndClickButtonByText("Send message");
    }

    public boolean isSuccessMessageDisplayed() {
        return Driver.get().findElement(successMessage).isDisplayed();
    }

    public void selectRadioButtonAndProceed(String radioButtonLabel) {
        selectSupportRadioButtonByLabelText(radioButtonLabel);
        if (radioButtonLabel.equals("Text message to a phone number from another country")) {
            enterMoreDetails("More details text");
            canWeReplyViaEmail("No");
            // pressSendMessage(); //removed so as not to submit case to support desk!
            closeActiveTab();
            switchToTabByIndex(0); // switch back to main tab
        } else {
            findAndClickContinue();
        }
    }

    public String getLinkText() {
        return Driver.get().findElement(mfaSupportLinkText).getText();
    }

    public String getLinkAppText() {
        return Driver.get().findElement(mfaSupportLinkAppText).getText();
    }
}

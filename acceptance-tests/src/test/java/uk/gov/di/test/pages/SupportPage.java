package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.Driver;

import java.net.MalformedURLException;

public class SupportPage extends BasePage {

    By supportLink = By.xpath("//*[contains(text(), 'Support (opens in new tab)')]");
    By moreDetailsField = By.id("moreDetailDescription");
    By successMessage = By.cssSelector(".govuk-panel--confirmation");

    public void clickSupportLink() throws MalformedURLException {
        Driver.get().findElement(supportLink).click();
    }

    public void selectSupportRadioButtonByLabelText(String option) throws MalformedURLException {
        Driver.get()
                .findElement(By.xpath("//div[label[contains(text(), '" + option + "')]]/input"))
                .click();
    }

    public void enterMoreDetails(String details) throws MalformedURLException {
        Driver.get().findElement(moreDetailsField).sendKeys(details);
    }

    public void canWeReplyViaEmail(String option) throws MalformedURLException {
        selectSupportRadioButtonByLabelText(option);
    }

    public void pressSendMessage() throws MalformedURLException {
        findAndClickButtonByText("Send message");
    }

    public boolean isSuccessMessageDisplayed() throws MalformedURLException {
        return Driver.get().findElement(successMessage).isDisplayed();
    }

    public void selectRadioButtonAndProceed(String radioButtonLabel) throws MalformedURLException {
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
}

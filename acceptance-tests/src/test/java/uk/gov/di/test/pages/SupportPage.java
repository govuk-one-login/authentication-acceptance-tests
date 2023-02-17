package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.utils.SignIn;

import java.util.ArrayList;

public class SupportPage extends SignIn {

    By supportLink = By.xpath("//*[contains(text(), 'Support (opens in new tab)')]");
    By moreDetailsField = By.id("moreDetailDescription");
    By successMessage = By.cssSelector(".govuk-panel--confirmation");

    public void clickSupportLink() {
        driver.findElement(supportLink).click();
    }

    public void selectSupportRadioButtonByLabelText(String option) {
        driver.findElement(By.xpath("//div[label[contains(text(), '" + option + "')]]/input"))
                .click();
    }

    public void enterMoreDetails(String details) {
        driver.findElement(moreDetailsField).sendKeys(details);
    }

    public void canWeReplyViaEmail(String option) {
        selectSupportRadioButtonByLabelText(option);
    }

    public void pressSendMessage() {
        findAndClickButtonByText("Send message");
    }

    public boolean isSuccessMessageDisplayed() {
        return driver.findElement(successMessage).isDisplayed();
    }

    public void selectRadioButtonAndProceed(String radioButtonLabel) {
        selectSupportRadioButtonByLabelText(radioButtonLabel);
        if (radioButtonLabel.equals("Text message to a phone number from another country")) {
            enterMoreDetails("More details text");
            canWeReplyViaEmail("No");
            pressSendMessage();
        } else {
            findAndClickContinue();
        }
    }

    public void checkForNewTabAndGoToIt(String newTabTitle) {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        waitForPageLoad(newTabTitle);
    }
}

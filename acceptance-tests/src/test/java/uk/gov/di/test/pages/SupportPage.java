package uk.gov.di.test.pages;

import org.openqa.selenium.By;

public class SupportPage extends BasePage {

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
            // pressSendMessage(); //removed so as not to submit case to support desk!
            closeActiveTab();
            switchToTabByIndex(0); // switch back to main tab
        } else {
            findAndClickContinue();
        }
    }
}

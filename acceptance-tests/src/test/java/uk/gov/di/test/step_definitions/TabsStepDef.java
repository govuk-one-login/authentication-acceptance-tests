package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;
import uk.gov.di.test.utils.BrowserTabs;

public class TabsStepDef {

    @When("the user creates a new tab")
    public void createNewTab() {
        BrowserTabs.createNewTab();
    }

    @ParameterType("first|second|third")
    public String ordinalNumber(String ordinalNumber) {
        return ordinalNumber;
    }

    @When("the user switches back to the {ordinalNumber} tab")
    @When("the user switches to the {ordinalNumber} tab")
    public void switchesToTheOrdinalNumberTab(String ordinalNumber) {
        BrowserTabs.switchToTabByOrdinalNumber(ordinalNumber);
    }

    @When("the user switches to tab {int}")
    public void switchToTab(int index) {
        BrowserTabs.switchToTabByIndex(index);
    }
}

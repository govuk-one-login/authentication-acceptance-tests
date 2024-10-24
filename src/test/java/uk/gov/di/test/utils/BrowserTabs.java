package uk.gov.di.test.utils;

import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;

public class BrowserTabs {
    public static void createNewTab() {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("window.open()");
    }

    public static void switchToTabByOrdinalNumber(String ordinalNumber) {
        switch (ordinalNumber) {
            case "first" -> switchToTabByIndex(0);
            case "second" -> switchToTabByIndex(1);
            case "third" -> switchToTabByIndex(2);
            default -> throw new RuntimeException("Invalid ordinal number");
        }
    }

    public static void switchToTabByIndex(int index) {
        ArrayList<String> tabs = new ArrayList<>(Driver.getDriver().getWindowHandles());
        Driver.getDriver().switchTo().window(tabs.get(index));
    }
}

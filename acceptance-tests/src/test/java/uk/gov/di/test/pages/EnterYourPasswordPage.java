package uk.gov.di.test.pages;

import org.openqa.selenium.By;
import uk.gov.di.test.step_definitions.World;
import uk.gov.di.test.utils.Driver;

import static uk.gov.di.test.utils.Constants.NEW_VALID_PASSWORD;

public class EnterYourPasswordPage extends BasePage {
    private final World world;

    By passwordField = By.id("password");
    By forgottenPasswordLink = By.xpath("//a[@href='/reset-password-request']");

    public EnterYourPasswordPage(World world) {
        this.world = world;
    }

    public void enterPassword(String pw) {
        clearFieldAndEnter(passwordField, pw);
    }

    public void clickForgottenPasswordLink() {
        Driver.get().findElement(forgottenPasswordLink).click();
    }

    public void enterPasswordAndContinue(String pw) {
        enterPassword(pw);
        findAndClickContinue();
    }

    public void enterIncorrectPasswordNumberOfTimes(Integer numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            waitForPageLoad("Enter your password");
            enterPasswordAndContinue("IncorrectPassword");
            System.out.println("Incorrect password entry count: " + (index + 1));
        }
    }

    public void enterCorrectPasswordAndContinue() {
        enterPasswordAndContinue(world.getUserPassword());
    }

    public void enterNewPasswordAndContinue() {
        enterPasswordAndContinue(NEW_VALID_PASSWORD);
    }
}

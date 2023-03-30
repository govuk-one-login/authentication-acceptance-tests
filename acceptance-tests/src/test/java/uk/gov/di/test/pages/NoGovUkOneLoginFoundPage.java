package uk.gov.di.test.pages;

import uk.gov.di.test.utils.SignIn;

public class NoGovUkOneLoginFoundPage extends SignIn {

    public void clickCreateGovUkOneLoginButton() {
        findAndClickButtonByText("Create a GOV.UK One Login");
    }
}

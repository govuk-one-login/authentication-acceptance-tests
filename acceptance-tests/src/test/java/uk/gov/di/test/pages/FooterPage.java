package uk.gov.di.test.pages;

import java.net.MalformedURLException;

public class FooterPage extends BasePage {

    public void selectWelshSupportLink() throws MalformedURLException {
        selectLinkByText("Cefnogaeth (agor mewn tab newydd)");
    }
}

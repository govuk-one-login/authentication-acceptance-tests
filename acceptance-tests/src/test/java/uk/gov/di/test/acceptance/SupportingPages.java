package uk.gov.di.test.acceptance;

public enum SupportingPages {
    ACCESSIBILITY_STATEMENT(
            "/accessibility-statement", "Accessibility statement for GOV.UK accounts"),
    GOV_UK_COOKIES("/help/cookies", "Cookies on GOV.UK"),
    TERMS_AND_CONDITIONS("/terms-and-conditions", "Terms and conditions"),
    PRIVACY_NOTICE("/privacy-notice", "Privacy notice");

    private static final String PRODUCT_NAME = "GOV.UK account";

    private String route;
    private String shortTitle;

    SupportingPages(String route, String shortTitle) {
        this.route = route;
        this.shortTitle = shortTitle;
    }

    public String getRoute() {
        return route;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getFullTitle() {
        return getShortTitle() + " - " + PRODUCT_NAME;
    }
}

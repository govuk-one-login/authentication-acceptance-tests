package uk.gov.di.test.utils;

public enum SupportingPages {
    ACCESSIBILITY_STATEMENT(
            "/accessibility-statement", "Accessibility statement for GOV.UK One Login"),
    GOV_UK_ACCOUNTS_COOKIES("/cookies", "GOV.UK One Login cookies policy"),
    TERMS_AND_CONDITIONS("/terms-and-conditions", "Terms and conditions"),
    PRIVACY_NOTICE(
            "/government/publications/govuk-one-login-privacy-notice",
            "GOV.UK One Login privacy notice - GOV.UK");

    private static final String PRODUCT_NAME = "GOV.UK One Login";

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

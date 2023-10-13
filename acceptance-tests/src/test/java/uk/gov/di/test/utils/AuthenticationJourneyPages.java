package uk.gov.di.test.utils;

public enum AuthenticationJourneyPages {
    ENTER_EMAIL_CREATE("/enter-email-create", "Enter your email address"),
    CHECK_YOUR_EMAIL("/check-your-email", "Check your email"),
    CREATE_PASSWORD("/create-password", "Create your password"),
    GET_SECURITY_CODES("/get-security-codes", "Choose how to get security codes"),
    ENTER_PHONE_NUMBER("/enter-phone-number", "Enter your mobile phone number"),
    CHECK_YOUR_PHONE("/check-your-phone", "Check your phone"),
    ACCOUNT_CREATED("/account-created", "You’ve created your GOV.UK One Login"),
    ENTER_PASSWORD("/enter-password", "Enter your password"),
    ENTER_CODE_UPLIFT("/enter-code", "You need to enter a security code"),
    ENTER_EMAIL_EXISTING_USER(
            "/enter-email", "Enter your email address to sign in to your GOV.UK One Login"),
    RESEND_SECURITY_CODE("/resend-code", "Get security code");

    private static final String PRODUCT_NAME = "GOV.UK One Login";

    private String route;
    private String shortTitle;

    AuthenticationJourneyPages(String route, String shortTitle) {
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

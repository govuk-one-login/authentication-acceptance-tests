package uk.gov.di.test.acceptance;

public enum AuthenticationJourneyPages {
    SIGN_IN_OR_CREATE("/sign-in-or-create", "Create a GOV.UK account or sign in"),
    ENTER_EMAIL("/enter-email", "Enter your email address"),
    ENTER_EMAIL_CREATE("/enter-email-create", "Enter your email address"),
    ACCOUNT_NOT_FOUND("/account-not-found", "No GOV.UK account found"),
    CHECK_YOUR_EMAIL("/check-your-email", "Check your email"),
    CREATE_PASSWORD("/create-password", "Create your password"),
    ENTER_PHONE_NUMBER("/enter-phone-number", "Enter your mobile phone number"),
    CHECK_YOUR_PHONE("/check-your-phone", "Check your phone"),
    ACCOUNT_CREATED("/account-created", "You've created your GOV.UK account"),
    ENTER_PASSWORD("/enter-password", "Enter your password"),
    ENTER_CODE("/enter-code", "Check your phone"),
    ENTER_EMAIL_EXISTING_USER("/enter-email", "Sign in to your GOV.UK account"),
    SHARE_INFO("/share-info", "Share information from your GOV.UK account"),
    SECURITY_CODE_INVALID(
            "/security-code-invalid", "You entered the wrong security code too many times");

    private static final String PRODUCT_NAME = "GOV.UK account";

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

package uk.gov.di.test.utils;

public enum AuthenticationJourneyPages {
    SIGN_IN_OR_CREATE("/sign-in-or-create", "Create a GOV.UK One Login or sign in"),
    ENTER_EMAIL("/enter-email", "Enter your email address"),
    ENTER_EMAIL_CREATE("/enter-email-create", "Enter your email address"),
    ACCOUNT_NOT_FOUND("/account-not-found", "No GOV.UK One Login found"),
    CANNOT_GET_NEW_SECURITY_CODE(
            "/resend-code", "You cannot get a new security code at the moment"),
    CHECK_YOUR_EMAIL("/check-your-email", "Check your email"),
    CREATE_PASSWORD("/create-password", "Create your password"),
    GET_SECURITY_CODES("/get-security-codes", "Choose how to get security codes"),
    FINISH_CREATING_YOUR_ACCOUNT_GET_SECURITY_CODES(
            "/get-security-codes", "Finish creating your account"),
    SETUP_AUTHENTICATOR_APP("/setup-authenticator-app", "Set up an authenticator app"),
    ENTER_AUTHENTICATOR_APP_CODE(
            "/enter-authenticator-app-code", "You need to enter a security code"),
    ENTER_PHONE_NUMBER("/enter-phone-number", "Enter your mobile phone number"),
    FINISH_CREATING_YOUR_ACCOUNT("/enter-phone-number", "Finish creating your account"),
    CHECK_YOUR_PHONE("/check-your-phone", "Check your phone"),
    ACCOUNT_CREATED("/account-created", "You’ve created your GOV.UK One Login"),
    ENTER_PASSWORD("/enter-password", "Enter your password"),
    ENTER_CODE("/enter-code", "You need to enter a security code"),
    ENTER_EMAIL_EXISTING_USER(
            "/enter-email", "Enter your email address to sign in to your GOV.UK One Login"),
    SHARE_INFO("/share-info", "Share information from your GOV.UK One Login"),
    SECURITY_CODE_INVALID(
            "/security-code-invalid", "You entered the wrong security code too many times"),
    RESEND_EMAIL_CODE("/resend-email-code", "Get security code"),
    RESEND_SECURITY_CODE("/resend-code", "Get security code"),
    RESEND_SECURITY_CODE_TOO_MANY_TIMES(
            "/security-code-requested-too-many-times",
            "You asked to resend the security code too many times");

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

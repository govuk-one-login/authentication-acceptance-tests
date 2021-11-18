package uk.gov.di.test.acceptance;

public enum AccountJourneyPages {
    MANAGE_YOUR_ACCOUNT("/manage-your-account", "Manage your account"),
    ENTER_PASSWORD_CHANGE_PASSWORD("/enter-password", "Enter your current password"),
    CHANGE_PASSWORD("/change-password", "Enter your new password"),
    PASSWORD_UPDATED_CONFIRMATION(
            "/password-updated-confirmation", "You have changed your password"),
    ENTER_PASSWORD_DELETE_ACCOUNT("/enter-password", "Enter your password"),
    DELETE_ACCOUNT("/delete-account", "Are you sure you want to delete your account?"),
    ACCOUNT_DELETED_CONFIRMATION(
            "/account-deleted-confirmation", "You have deleted your GOV.UK account");

    private static final String PRODUCT_NAME = "GOV.UK account";

    private String route;
    private String shortTitle;

    AccountJourneyPages(String route, String shortTitle) {
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

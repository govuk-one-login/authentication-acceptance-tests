@AccountManagement @build @staging
  Feature: Account Management
    Scenario: Account Management API throws "An account with this email address does not exist" when a user sends an unregistered email to the API, after signing in
      Given the user comes from the stub relying party with options: "scopes-account-management"
      When the user enters "TEST_USER_ACCOUNT_MANAGEMENT_EMAIL" email address, password and six digit SMS OTP
      Then the user is returned to the service
      When the test calls the Account Management Update Password API with the access token and validates response
      Then the user clicks logout
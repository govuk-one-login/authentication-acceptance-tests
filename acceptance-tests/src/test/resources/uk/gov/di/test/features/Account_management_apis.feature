@AccountManagement @build @staging @under-development
  Feature: Account Management
    Scenario: Account Management API throws "An account with this email address does not exist" when a user sends an unregistered email to the API, after signing in
      Given a user with SMS MFA exists
      When the user comes from the stub relying party with option scopes-account-management and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is taken to the "Check your phone" page
      When the user enters the six digit security code from their phone
      Then the user is returned to the service
      When the Account Management Update Password API is called with an email address that does not exist, an error is returned
      Then the user clicks logout

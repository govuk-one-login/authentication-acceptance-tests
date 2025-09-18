@AUT-4388 @UI @dev @build @staging
Feature: Create account - Experian email check

  Experian is checking the submitted email address and a decision is made before the User is asked to create a password.
  The scenarios check that email blocking is applied correctly.  See registration_journey.feature for full creation scenarios.

  Scenario: User successfully create an account with valid email using sms as OTP
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    # Pick one
    Then the user email is accepted and the "Create your password" page is presented
    Then the user email is not blocked

  @AUT-4389
  Scenario: User successfully create an account with valid email using sms as OTP
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When User enters an email "stubemailassessmenterror@test.null"
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user email is blocked and the "Sorry" page is presented
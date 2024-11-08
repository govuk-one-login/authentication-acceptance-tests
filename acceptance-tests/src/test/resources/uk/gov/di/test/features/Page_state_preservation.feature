@build @staging @build-sp @staging-sp
Feature: Page state is preserved when using back link

  Scenario: User has chosen text message as their auth method and moved on to the next page. When clicking the Back link, the user is returned to the auth method selection page and their previous choice of text message remains selected.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user clicks the Back link
    Then the user is taken to the "Choose how to get security codes" page
    And their previously chosen text message auth method remains selected

  Scenario: User has chosen auth app their auth method and moved on to the next page. When clicking the Back link, the user is returned to the auth method selection page and their previous choice of auth app remains selected.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user clicks the Back link
    Then the user is taken to the "Choose how to get security codes" page
    And their previously chosen auth app auth method remains selected

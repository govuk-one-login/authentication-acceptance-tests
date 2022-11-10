@AuthApp2FA
Feature: Authentication App Journeys
  New user creates an account and logs in using an auth app

  Scenario: User successfully registers with auth app 2FA and login with 2fa-on
    Given the auth app user has valid credentials
    And the user visits the stub relying party
    And the existing user clicks "2fa-off"
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user creates a password
    Then the new user is taken to the get security codes page
    When the new user chooses "mfaOptions-2" to get security codes
    Then the new user is taken to the setup authenticator app page
    When the new user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the new user is not shown an error message in field "code-error"
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    Then the user is returned to the service
    When the user clicks logout
    Then the new user is taken to the signed out page
    And the user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the existing auth app user selects sign in
    Then the existing auth app user is taken to the enter your email page
    When the existing auth app user enters their email address
    Then the existing auth app user is prompted for their password
    When the existing auth app user enters their password
    Then the existing user is taken to the enter authenticator app code page
    When the user enters the security code from the auth app
    Then the user is returned to the service
    When the user clicks logout
    Then the new user is taken to the signed out page

  Scenario: User successfully login without 2FA
    Given the auth app user has valid credentials
    When the user visits the stub relying party
    And the existing user clicks "2fa-off"
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing auth app user selects sign in
    Then the existing auth app user is taken to the enter your email page
    When the existing auth app user enters their email address
    Then the existing auth app user is prompted for their password
    When the existing auth app user enters their password
    Then the user is returned to the service
    When the user clicks logout
    Then the new user is taken to the signed out page

  Scenario: User signs in auth app without 2FA, then uplifts
    Given the auth app user has valid credentials
    When the user visits the stub relying party
    And the existing user clicks "2fa-off"
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing auth app user selects sign in
    Then the existing auth app user is taken to the enter your email page
    When the existing auth app user enters their email address
    Then the existing auth app user is prompted for their password
    When the existing auth app user enters their password
    Then the user is returned to the service
    When the user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the enter authenticator app code page
Feature: Authentication App Journeys
  New user creates an account and logs in using an auth app

  @AuthApp2FA
  Scenario: User successfully registers with auth app 2FA, logs out then signs in again
    Given the registration services are running
    And the auth app user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    And there are no accessibility violations
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    And there are no accessibility violations
    Then the new user is taken to the create your password page
    When the new user creates a password
    And there are no accessibility violations
    Then the new user is taken to the get security codes page
    When the new user chooses "mfa-options-auth-app" to get security codes
    Then the new user is taken to the setup authenticator app page
    When the new user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    And there are no accessibility violations
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    And there are no accessibility violations
    Then the new user is taken the the share info page
    When the new user agrees to share their info
    Then the user is returned to the service
    When the new user clicks by name "logout"
    And there are no accessibility violations
    Then the new user is taken to the signed out page
    When the user visits the stub relying party
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
    When the new user clicks by name "logout"
    And there are no accessibility violations
    Then the new user is taken to the signed out page
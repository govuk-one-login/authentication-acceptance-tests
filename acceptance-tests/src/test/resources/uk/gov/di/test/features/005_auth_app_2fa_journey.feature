@AuthApp2FA @build @staging @build-sp
Feature: Authentication App Journeys
  New user creates an account and logs in using an auth app

  Scenario: User successfully registers with auth app 2FA and login with 2fa-on
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_AUTH_APP_EMAIL" email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses "Auth App" to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is not shown any error messages
    And the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service
    And the user clicks logout
    And the users cookies are cleared

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_AUTH_APP_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is returned to the service
    And the user clicks logout

  Scenario: User successfully login without 2FA
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_AUTH_APP_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    And the user clicks logout

  Scenario: User signs in auth app without 2FA, then uplifts
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_AUTH_APP_EMAIL_3" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    When the user uplifts having already logged in
    Then the user is taken to the "Enter a security code to continue" page

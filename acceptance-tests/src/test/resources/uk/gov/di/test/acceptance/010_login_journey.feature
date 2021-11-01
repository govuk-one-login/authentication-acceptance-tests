Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user is correctly prompted to login
    Given the login services are running
    And the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is taken to the enter code page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service
    
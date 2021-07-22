Feature: Registration Journey
  User walks through a registration journey

  Scenario: User successfully registers
    Given the services are running
    And a new user has valid credentials
    When the user visit the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
    When the user enters their email address
    Then the user is asked to check their email

  Scenario: User registers with an insecure password
    Given the services are running
    And a new user has an insecure password
    When the user visit the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
#    When the user enters their email address
#    Then the user is asked to create a password
#    When the user registers their password
#    And the user clicks "continue"
#    Then the user is shown an error message
#    And the user is asked again to create a password

  Scenario: User registers with an invalid email
    Given the services are running
    And the user has an invalid email format
    When the user visit the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
    When the user enters their email address
    Then the user is shown an error message

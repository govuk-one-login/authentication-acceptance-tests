Feature: Registration Journey
  New user walks through a registration journey

  Scenario: User successfully registers
    Given the registration services are running
    And a new user has valid credentials
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
#    When the new user enters their email address
#    Then the user is asked to check their email

  Scenario: User registers with an insecure password
    Given the registration services are running
    And a new user has an insecure password
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
#    When the user enters their email address
#    Then the user is asked to create a password
#    When the user registers their password
#    And the user clicks "continue"
#    Then the user is shown an error message
#    And the user is asked again to create a password

  Scenario: User registers with an invalid email
    Given the registration services are running
    And the new user has an invalid email format
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
#    When the new user enters their email address
#    Then the new user is shown an error message

Feature: Login Journey
  User walks through a login journey

  Scenario: User is correctly prompted to login
    Given the services are running
    And the user has valid credentials
    When the user visit the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
#    When the user enters their email address
#    Then the user is prompted for password
#    When the user enters their password
#    And the user clicks "continue"
#    Then the user is taken to the Success page
#    When the user clicks "continue"
#    Then the user is taken to the Service User Info page

#  Scenario: User enters incorrect password
#    Given the services are running
#    And the user has invalid credentials
#    When the user visit the stub relying party
#    And the user clicks "govuk-signin-button"
#    Then the user is taken to the Identity Provider Login Page
#    When the user enters their email address
#    Then the user is prompted for password
#    When the user enters their password
#    And the user clicks "continue"
#    Then the user is taken to the Identity Provider Login Page
#    And the user is shown an error message
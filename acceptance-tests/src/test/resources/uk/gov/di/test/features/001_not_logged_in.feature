Feature: Not logged in
  User launches the stub

  Scenario: User launches the stub
    Given the not logged in services are running
    When the not logged in user visits the stub relying party
    And the not logged in user clicks "govuk-signin-button"
    Then the not logged in user is taken to the Identity Provider Login Page
Feature: Not logged in
  User launches account management

  Scenario: User launches account management
    Given the not logged in services are running
    When the not logged in user navigates to account management
    Then the not logged in user is taken to the Identity Provider Login Page


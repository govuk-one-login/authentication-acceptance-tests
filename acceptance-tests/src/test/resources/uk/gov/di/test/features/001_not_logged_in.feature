Feature: Not logged in
  User launches the stub

  Scenario: User launches the stub
    Given the not logged in services are running
    When a not logged in user tries to sign in
    Then the not logged in user is taken to the Identity Provider Login Page
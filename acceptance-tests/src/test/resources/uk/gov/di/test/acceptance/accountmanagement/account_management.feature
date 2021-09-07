Feature: Account Management
  User launches account management

  Scenario: User launches account management
    Given the account management services are running
    And the existing account management user has valid credentials
    When the existing account management user navigates to account management
    Then the existing account management user is taken to the Identity Provider Login Page


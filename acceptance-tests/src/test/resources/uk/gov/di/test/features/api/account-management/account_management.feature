@API @under-development
Feature: Account Management

  Scenario: Authenticated User successfully changes their Phone Number
    Given a User exists
    And the User is Authenticated
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then the User's Phone Number is updated to "07700900111"

@API @under-development
Feature: Account Management

  Scenario Outline: Authenticated User successfully changes their Phone Number
    Given a User exists
    And the User is Authenticated
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then the User's Phone Number is updated to "<Mobile Number>"

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

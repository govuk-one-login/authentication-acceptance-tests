@API @under-development
Feature: Account Management

  Scenario: Authenticated User successfully changes their Phone Number
    Given a User exists
    And the User is Authenticated
    When the User adds Backup Phone as their SMS Backup MFA
    Then the system sends an OTP to backup Phone Number
    When the User provides the correct otp
    Then Phone Number is added as a verified Backup MFA Method

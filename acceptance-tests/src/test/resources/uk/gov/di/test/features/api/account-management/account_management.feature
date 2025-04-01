@API @under-development
Feature: Account Management

  Scenario: Authenticated User successfully changes their Phone Number
    Given a User exists
    And the User is Authenticated
    When the User requests an OTP to change their Phone Number to "07700900111"
    And the User receives the OTP code
    And the User submits the OTP code to confirm the Phone Number change
    Then the User's Phone Number is updated to "07700900111"

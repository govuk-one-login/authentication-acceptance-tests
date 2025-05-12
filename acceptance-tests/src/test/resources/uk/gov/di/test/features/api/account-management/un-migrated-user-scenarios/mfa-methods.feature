@under-development @API
Feature: Auth App MFA User manages their MFA methods via the Method Management API

  As an Authenticated User using an Auth App as Default MFA
  I want to manage my MFA methods
  So that I can regain access to my Account if my Default method is unavailable

  Background:
    Given a User exists
    And the User is Authenticated

  Scenario: Adding a Phone Number as a Backup MFA
    Given the User has "079009000" as their Phone Number
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method
    And the User is now a migrated User without a phone number in their user profile
    And the User has Default of "07700900000"
    And the User has a Backup of "07700900111"

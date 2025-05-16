@under-development @API
Feature: Auth App MFA User manages their MFA methods via the Method Management API

  As an Authenticated User using an Auth App as Default MFA
  I want to manage my MFA methods
  So that I can regain access to my Account if my Default method is unavailable

  Background:
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated

  Scenario: Retrieve a logged in Users MFA Methods
    When a retrieve request is made to the API
    # Enter steps here

  Scenario: Adding a Phone Number as a Backup MFA
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method

  Scenario: Deleting a Backup MFA
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method
    When the User requests to delete backup MFA Method
    Then the User's backup MFA Method is deleted

  Scenario: Changing Default MFA method from Auth App to SMS
    When the User updates their Default MFA to SMS of "07700900111"
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is the new verified Default MFA

  Scenario: Changing Default MFA method from Auth App to new Auth App
    Given the User does not have a Backup MFA method
    When the User updates their Default MFA to an Auth App
    Then the Users Default MFA is an Auth App

  Scenario: Prevented from changing Default method to Auth App when Backup is Auth App
    And the User does not have a Backup MFA method
    And the User adds "07700900111" as their SMS Backup MFA
    And the system sends an OTP to "07700900111"
    And the User provides the correct otp
    Then the User switches their BACKUP and DEFAULT methods
    And the User is prevented from updating the Default MFA to an Auth App

  Scenario: Switch MFA methods
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900112" is added as a verified Backup MFA Method
    And the User switches their BACKUP and DEFAULT methods

  Scenario: Switch MFA methods Error for changing SMS back to default
    And the User does not have a Backup MFA method
    And the User adds "07700900111" as their SMS Backup MFA
    And the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method
    And the User switches their BACKUP and DEFAULT methods

  Scenario: Prevented from adding backup method to Auth App when Default is Auth App
    And the User does not have a Backup MFA method
    When the User cannot add an Auth App as Backup

  Scenario: Rejects invalid request format
    And the User does not have a Backup MFA method
    Then appropriate error is returned for invalid request to add Phone Number as SMS Backup MFA

  Scenario: Appropiate Response code Unauthorized - user credentials
    And the User does not have a Backup MFA method
    Then the user will not be able to add Backup MFA


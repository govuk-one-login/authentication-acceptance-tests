@under-development @API
Feature: SMS MFA User manages their MFA methods via the Method Management API

  As an Authenticated User with a Default MFA of SMS
  I want to manage my MFA methods
  So that I can regain access to my Account if my Default method is unavailable

  Background:
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated

  Scenario: Adds a Backup Auth App
    And the User does not have a Backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated

  Scenario: Switches to an Auth App
    And the User does not have a Backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated
    And the User switches their BACKUP and DEFAULT methods

  Scenario: Adds a Backup SMS
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method

  Scenario: Switches SMS MFA
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    And the User switches their BACKUP and DEFAULT methods

  Scenario: Prevented from changing Default method to Auth App when Backup is Auth App
    Given the User does not have a Backup MFA method
    And the User requests to add a backup MFA Auth App
    When the User cannot update their Default MFA to an Auth App

  Scenario: Use backup SMS to log in.
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method

    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user clicks logout

    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page

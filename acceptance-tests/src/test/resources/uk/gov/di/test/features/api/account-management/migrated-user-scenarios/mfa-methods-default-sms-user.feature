@API
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

  Scenario Outline: Adds a Backup SMS
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Updated Mobile Number>" is added as a verified Backup MFA Method

    Examples:
      | Mobile Number | Updated Mobile Number |
      | 07700900111   | +447700900111         |

    @AcceptInternationalNumbers
    Examples:
      | Mobile Number | Updated Mobile Number |
      | +61412123123  | +61412123123          |

  Scenario Outline: Switches SMS MFA
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    And the User switches their BACKUP and DEFAULT methods

    Examples:
      | Mobile Number |
      | 07700900111   |

    @AcceptInternationalNumbers
    Examples:
      | Mobile Number |
      | +61412123123  |

  Scenario: Prevented from changing Default method to Auth App when Backup is Auth App
    Given the User does not have a Backup MFA method
    And the User requests to add a backup MFA Auth App
    When the User cannot update their Default MFA to an Auth App

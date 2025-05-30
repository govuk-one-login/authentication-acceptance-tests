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

  Scenario Outline: Adding a Phone Number as a Backup MFA
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Mobile Number>" is added as a verified Backup MFA Method

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario Outline: Deleting a Backup MFA
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Mobile Number>" is added as a verified Backup MFA Method
    When the User requests to delete backup MFA Method
    Then the User's backup MFA Method is deleted

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario Outline: Changing Default MFA method from Auth App to SMS
    When the User updates their Default MFA to SMS of "<Mobile Number>"
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Mobile Number>" is the new verified Default MFA

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario: Changing Default MFA method from Auth App to new Auth App
    Given the User does not have a Backup MFA method
    When the User updates their Default MFA to an Auth App
    Then the Users Default MFA is an Auth App

  Scenario Outline: Prevented from changing Default method to Auth App when Backup is Auth App
    And the User does not have a Backup MFA method
    And the User adds "<Mobile Number>" as their SMS Backup MFA
    And the system sends an OTP to "<Mobile Number>"
    And the User provides the correct otp
    Then the User switches their BACKUP and DEFAULT methods
    And the User is prevented from updating the Default MFA to an Auth App

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario Outline: Switch MFA methods
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Mobile Number>" is added as a verified Backup MFA Method
    And the User switches their BACKUP and DEFAULT methods

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario Outline: Switch MFA methods Error for changing SMS back to default
    And the User does not have a Backup MFA method
    And the User adds "<Mobile Number>" as their SMS Backup MFA
    And the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Mobile Number>" is added as a verified Backup MFA Method
    And the User switches their BACKUP and DEFAULT methods

    Examples:
      | Mobile Number |
      | 07700900111   |
      | +61412123123  |

  Scenario: Prevented from adding backup method to Auth App when Default is Auth App
    And the User does not have a Backup MFA method
    When the User cannot add an Auth App as Backup

  Scenario: Rejects invalid request format
    And the User does not have a Backup MFA method
    Then appropriate error is returned for invalid request to add Phone Number as SMS Backup MFA

  Scenario: Appropiate Response code Unauthorized - user credentials
    And the User does not have a Backup MFA method
    Then the user will not be able to add Backup MFA

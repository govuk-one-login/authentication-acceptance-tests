@under-development @API
Feature: Login Using Back Up MFA

  Scenario Outline: Mfa User choose a Back Up Phone Number when they choose ‘Try another way to get security code’ option
    Given a Migrated User with a Auth App Default MFA
    And the User is Authenticated
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp


    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "Try another way to get security code" link
    And the user is taken to the "How do you want to get a security code?" page
    Examples:
      | Mfa Type | Link Text                | Page             |
      | SMS      | Problems with the code?  | Check your phone |
      | SMS      | Problems with the code?  | Check your phone |

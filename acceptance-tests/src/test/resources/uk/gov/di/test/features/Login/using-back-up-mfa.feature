@under-development @API
Feature: Login Using Back Up MFA

  Background:
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated

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
    And the user clicks the continue button

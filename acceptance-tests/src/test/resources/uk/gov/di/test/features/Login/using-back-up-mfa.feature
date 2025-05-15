@under-development @API
Feature: Login Using Back Up MFA


  Scenario: User uses SMS for MFA backup and SMS for default MFA
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method
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
    When the user clicks the continue button without selecting any radio button option
    Then the user is taken to the "Error - How do you want to get a security code?" page


  Scenario: User uses SMS for MFA backup and Auth App for MFA default
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "07700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user clicks the continue button without selecting any radio button option
    Then the user is taken to the "Error - How do you want to get a security code?" page

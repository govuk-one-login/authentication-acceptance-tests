@under-development @API @Login
Feature: Login Using Back Up MFA

@AUT-1416
  Scenario Outline: User with SMS as default MFA attempts authentication using a backup SMS number
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then Phone Number is added as a verified Backup MFA Method
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

  Examples:
    | Mobile Number |
    | 07700900111   |
    | +61412123123  |

  Scenario: User with SMS as default MFA attempts alternative MFA journey but submits without choosing a method
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

@AUT-4008
  Scenario: User with Auth App as default MFA attempts authentication using a backup SMS number
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

  @AUT-4256
  Scenario: Add ‘try another way to get a security code’ link on check your phone screen for Password Reset when Default and backup is SMS
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then Phone Number is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    Then the link "change how you get security codes" is not available
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page

  @AUT-4256
  Scenario: Add ‘try another way to get a security code’ link on check your phone screen for Password Reset when Default SMS and backup SMS
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then Phone Number is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    Then the link "change how you get security codes" is not available
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user clicks the continue button without selecting any radio button option
    Then the user is taken to the "Error - How do you want to get a security code?" page

  @AUT-4256
  Scenario: Add ‘try another way to get a security code’ link on check your Auth App screen for Password Reset when Default Auth App and backup SMS
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then Phone Number is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page

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

  @AUT-4247
  Scenario: User with SMS as the default MFA method attempts authentication using correct OTP sent to a backup SMS number
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service

  @AUT-4247
  Scenario: User with SMS as the default MFA method attempts authentication and request OTP more than five times
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  @AUT-4247
  Scenario: User with SMS as the default MFA method attempts authentication and enters incorrect OTP more than five times
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  @AUT-4248
  Scenario: User with Auth App as the default MFA method attempts authentication using correct OTP sent to a backup SMS number
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service

  @AUT-4248
  Scenario: User with Auth App as the default MFA method attempts authentication and request OTP more than five times
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  @AUT-4248.
  Scenario: User with Auth App as the default MFA method attempts authentication and enters incorrect OTP more than five times
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  @AUT-4183
  Scenario: User with SMS as the default MFA method attempts password reset authentication using correct OTP sent an backup Auth App
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user selects radio button "Use your authenticator app"
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user enters the security code from backup MFA auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service

  @AUT-4183
  Scenario: User with Auth App as the default MFA method attempts password reset authentication using correct OTP sent an backup SMS
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
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service

  @AUT-4183
  Scenario: User with Auth App as the default MFA method attempts reset password authentication with backup SMS MFA and request OTP more than five times
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
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  @AUT-4183
  Scenario: User with SMS as the default MFA method attempts password reset authentication with Auth App and enters incorrect OTP more than five times
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user selects radio button "Use your authenticator app"
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user enters an incorrect auth app security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

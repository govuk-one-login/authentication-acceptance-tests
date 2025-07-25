@Login @API
Feature: Login Using Back Up MFA

  @AUT-1416
  Scenario Outline: User authenticates using a backup SMS MFA
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "<Updated Mobile Number>" is added as a verified Backup MFA Method
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
    | Mobile Number | Updated Mobile Number |
    | 07700900111   | +447700900111         |
    | +61412123123  | +61412123123          |

  Scenario: User with SMS as default MFA attempts alternative MFA journey but submits without choosing a method
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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

  @AUT-4256
  Scenario: Add ‘try another way to get a security code’ link on check your phone screen for Password Reset when Default SMS and backup SMS
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user clicks the continue button without selecting any radio button option
    Then the user is taken to the "Error - How do you want to get a security code?" page

  @AUT-4256
  Scenario: Add ‘try another way to get a security code’ link on check your Auth App screen for Password Reset when Default Auth App and backup SMS
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
  Scenario: User authenticates with their Backup SMS MFA
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
  Scenario: User requests too many OTPs when authenticating with a Backup SMS MFA
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    When the user requests the phone otp code a further 4 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  @AUT-4247
  Scenario: User enters too many incorrect OTPs when authenticating with a Backup SMS MFA
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
  Scenario: User authenticates using a Backup SMS MFA
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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

  @AUT-4248 @AUT-4378 @AUT-4252
  Scenario: A User loses access to their Default Auth App and requests too many OTPs when authenticating with a Backup SMS MFA and lockout
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you asked to resend the security code too many times"

  @AUT-4348 @AUT-4278 @AUT-4252
  Scenario: User loses access to their Default Auth App and enters too many incorrect OTPs when authenticating with a Backup SMS MFA and lockout
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you entered the wrong security code too many times"

  @AUT-4248 @AUT-4378 @AUT-4252
  Scenario: User loses access to their Default SMS and enters too many incorrect OTPs when authenticating with a Backup SMS MFA and lockout
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you entered the wrong security code too many times"

  @AUT-4248 @AUT-4378 @AUT-4252
  Scenario: User loses access to their Default SMS and requests OTPs too many time when authenticating with a Backup SMS MFA and lockout
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    When the user requests the phone otp code a further 4 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you asked to resend the security code too many times the last time"

  @AUT-4248 @AUT-4378 @AUT-4252
  Scenario: User loses access to their Default SMS and enters too many incorrect OTPs when authenticating with a Backup Auth App MFA and lockout
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
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user selects radio button "Use your authenticator app"
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters an incorrect auth app security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you entered the wrong security code too many times"

  @AUT-4183
  Scenario: User resets password using a Backup Auth App MFA
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
  Scenario: User resets password using a Backup SMS MFA
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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

  @AUT-4183 @AUT-4377 @AUT-4252
  Scenario: User with Auth App Default MFA requests too many OTPs when resetting their password with a Backup SMS MFA and lockout
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you asked to resend the security code too many times"
    * the lockout duration is 2 hours

  @AUT-4183 @AUT-4377 @AUT-4252
  Scenario: User with Auth App Default MFA enters too many incorrect OTPs when resetting their password with a Backup SMS MFA and lockout
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    When the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you entered the wrong security code too many times"
    * the lockout duration is 2 hours

  @AUT-4183 @AUT-4377 @AUT-4252
  Scenario: User with SMS as Default MFA requests too many OTPs resetting their password using a Backup SMS MFA and lockout
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    Then the user is taken to the "Check your phone" page
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 4 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout duration is 2 hours
    * the lockout reason is "you asked to resend the security code too many times"

  @AUT-4183 @AUT-4377 @AUT-4252
  Scenario: User with SMS as Default MFA requests too many incorrect OTPs resetting their password using a Backup SMS and lockout
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
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
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you entered the wrong security code too many times"
    * the lockout duration is 2 hours

  @AUT-4183 @AUT-4377 @AUT-4252
  Scenario: User with SMS as Default MFA requests too many incorrect OTPs resetting their password using a Backup Auth App and lockout
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
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you entered the wrong security code too many times"
    * the lockout duration is 2 hours

  @AUT-4199
  Scenario: User with SMS as Default MFA and SMS as backup MFA switches MFA in uplift journey
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with option 2fa-off and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    When the user comes from the stub relying party with options: [2fa-on,authenticated-2] and is taken to the "Enter a security code to continue" page
    Then the user is taken to the "Enter a security code to continue" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Enter a security code to continue" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service

  @AUT-4199
  Scenario: User with Auth App as Default MFA and SMS as backup MFA switches MFA in uplift journey
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "07700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with option 2fa-off and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    When the user comes from the stub relying party with options: [2fa-on,authenticated-2] and is taken to the "Enter a security code to continue" page
    Then the user is taken to the "Enter a security code to continue" page
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Enter a security code to continue" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service

  @AUT-4198
  Scenario: Sms user successfully reauthenticate with backup phone number
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Given the RP requires the user to reauthenticate
    When the user enters the same email address for reauth as they used for login
    And the user enters the correct password
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is successfully reauthenticated and returned to the service

  @AUT-4198
  Scenario: SMS user successfully reauthenticate with backup Auth App
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Given the RP requires the user to reauthenticate
    When the user enters the same email address for reauth as they used for login
    And the user enters the correct password
    Then the user is taken to the "Check your phone" page
    And the user selects "Problems with the code?" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    When the user selects radio button "Use your authenticator app"
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from backup MFA auth app
    Then the user is successfully reauthenticated and returned to the service

  @AUT-4198
  Scenario: Auth App user force log off while reauthenticate with backup phone number enters wrong code too many times
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Given the RP requires the user to reauthenticate
    When the user enters the same email address for reauth as they used for login
    And the user enters the correct password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    And the user selects "try another way to get a security code" link
    Then the user is taken to the "How do you want to get a security code?" page
    And the user selects radio button "Text message to your phone number ending with" and "111"
    When the user clicks the continue button
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating


  @AUT-4315
  Scenario: Migrated User with SMS as Default MFA and SMS as backup MFA to validate default phone number for claim
    Given a Migrated User with a Default MFA of SMS
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user info JSON is extracted from the stub page
    And the "phone_number_verified" is true

  @AUT-4315
  Scenario: Migrated User with SMS as Default MFA and Auth App as backup MFA to validate default phone number for claim
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
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user info JSON is extracted from the stub page
    And the "phone_number_verified" is true

  @AUT-4315
  Scenario: Migrated User with Auth APP as Default MFA and SMS as backup MFA to validate default phone number for claim
    Given a Migrated User with an Auth App Default MFA
    And the User is Authenticated
    And the User does not have a Backup MFA method
    When the User adds "+447700900111" as their SMS Backup MFA
    Then the system sends an OTP to "07700900111"
    When the User provides the correct otp
    Then "+447700900111" is added as a verified Backup MFA Method
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the six digit code for "App"
    Then the user is returned to the service
    And the user info JSON is extracted from the stub page
    And the "phone_number_verified" is false

  @AUT-4315
  Scenario: Non-Migrated User with APP as Default MFA and no backup to validate default phone number for claim
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the six digit code for "App"
    Then the user is returned to the service
    And the user info JSON is extracted from the stub page
    And the "phone_number_verified" is false


  @AUT-4315
  Scenario: Non-Migrated User with SMS as Default MFA and no backup to validate default phone number for claim
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user info JSON is extracted from the stub page
    And the "phone_number_verified" is true

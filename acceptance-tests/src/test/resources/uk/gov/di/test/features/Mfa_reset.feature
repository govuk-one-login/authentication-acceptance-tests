@mfa-reset

Feature: The MFA reset process.
  Begins in Authentication, when a user initiates an MFA reset,
  they are redirected to Identity to determine their verification status. Identity verifies
  whether the user is classified as authentication-only or identity-verified.

# ************************* SMS Section *************************
  @AUT-3825 @under-development
  Scenario: SMS User selects SMS OTP for MFA reset after successful identity verification
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service


  @AUT-3825 @under-development
  Scenario: AUTH app User selects SMS OTP for MFA reset after successful identity verification
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

# ************************* AUTH APP Section *************************
  @AUT-3825 @under-development
  Scenario: Auth app User selects Authenticate app for MFA reset after successful identity verification
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service


  @AUT-3825 @under-development
  Scenario: SMS User selects Authenticate app for MFA reset after successful identity verification
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

# ************************* Negative tests when for unsuccessful IPV responses **********************
  @AUT-3930 @AUT-3920 @under-development
  Scenario Outline: Mfa User choose to reset their MFA but are unsuccessful in identity verification test
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user enters an incorrect "<Mfa Type>" code 2 times
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    And the user is taken to the "You cannot change how you get security codes" page
    Then the URL is present with suffix "cannot-change-security-codes-identity-fail"
    When "Try entering a security code again with the method you already have set up" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | IPV Response           | Page                                                            |
      | App      | I do not have access to the authenticator app | Identity check failed  | Enter the 6 digit security code shown in your authenticator app |
      | SMS      | Problems with the code?                       | Identity did not match | Check your phone                                                |
      | SMS      | Problems with the code?                       | Identity check failed  | Check your phone                                                |
      | App      | I do not have access to the authenticator app | Identity did not match | Enter the 6 digit security code shown in your authenticator app |


  @AUT-3931 @under-development
  Scenario Outline: Mfa User Error scenario - get help to delete your account
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "You cannot change how you get security codes" page
    When "Get help to delete your GOV.UK One Login from the support team" radio option selected
    And the user clicks the continue button
    Then the URL is present with suffix "cannot-change-security-codes"
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              |
      | App      | I do not have access to the authenticator app | Identity check failed     |
      | SMS      | Problems with the code?                       | Identity did not match    |
      | SMS      | Problems with the code?                       | Identity check failed     |
      | App      | I do not have access to the authenticator app | Identity did not match    |
      | App      | I do not have access to the authenticator app | No identity available     |
      | SMS      | Problems with the code?                       | Identity check incomplete |
      | SMS      | Problems with the code?                       | No identity available     |
      | App      | I do not have access to the authenticator app | Identity check incomplete |

# ************************* Additional tests for IPV MFA journey **********************
  @AUT-3997 @under-development
  Scenario Outline: Mfa User choose to use Back button when choosing the ‘Try entering a security code again’ option
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    And the user is taken to the "You cannot change how you get security codes" page
    Then the URL is present with suffix "cannot-change-security-codes-identity-fail"
    When "Try entering a security code again with the method you already have set up" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    And the user navigates to the previous page
    And the user is taken to the "You cannot change how you get security codes" page
    Then "Try entering a security code again with the method you already have set up" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | IPV Response           | Page                                                            |
      | App      | I do not have access to the authenticator app | Identity check failed  | Enter the 6 digit security code shown in your authenticator app |
      | SMS      | Problems with the code?                       | Identity did not match | Check your phone                                                |
      | SMS      | Problems with the code?                       | Identity check failed  | Check your phone                                                |
      | App      | I do not have access to the authenticator app | Identity did not match | Enter the 6 digit security code shown in your authenticator app |


  @AUT-3997 @under-development
  Scenario Outline: Mfa User choose to use Back-button from contact screen
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "You cannot change how you get security codes" page
    When "Get help to delete your GOV.UK One Login from the support team" radio option selected
    And the user clicks the continue button
    Then the URL is present with suffix "cannot-change-security-codes"
    And the user navigates to the previous page
    And the user is taken to the "You cannot change how you get security codes" page
    Then "Try entering a security code again with the method you already have set up" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              | Page                                                            |
      | App      | I do not have access to the authenticator app | No identity available     | Enter the 6 digit security code shown in your authenticator app |
      | SMS      | Problems with the code?                       | Identity check incomplete | Check your phone                                                |
      | SMS      | Problems with the code?                       | No identity available     | Check your phone                                                |
      | App      | I do not have access to the authenticator app | Identity check incomplete | Enter the 6 digit security code shown in your authenticator app |


  @AUT-3993 @under-development
  Scenario Outline: MFA reset is switched on and APP user can reset their MFA method after resetting their password
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    And the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "<Page>" page
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    And the user clicks the continue button
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | IPV Response | Page                                                            |
      | App      | I do not have access to the authenticator app | Success      | Enter the 6 digit security code shown in your authenticator app |


  @AUT-3993 @under-development
  Scenario Outline: MFA reset is switched on and SMS user can reset their MFA method after resetting their password
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "<Page>" page
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text               | IPV Response | Page             |
      | SMS      | Problems with the code? | Success      | Check your phone |


  @AUT-3993 @old-mfa-without-ipv
  Scenario Outline: MFA reset is switched off and SMS user cannot reset their MFA method after resetting their password
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user logs out
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "<Page>" page
    And the user selects "<Link Text>" link
    Then the link "change how you get security codes" is not available
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text               | Page             |
      | SMS      | Problems with the code? | Check your phone |


  @AUT-3993 @old-mfa-without-ipv
  Scenario Outline: MFA reset is switched off and APP user cannot reset their MFA method after resetting their password
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    And the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user logs out
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "<Page>" page
    And the link "I do not have access to the authenticator app" is not available
    When the user enters the six digit code for "<Mfa Type>"
    Then the user is returned to the service
    Examples:
      | Mfa Type | Page                                                            |
      | App      | Enter the 6 digit security code shown in your authenticator app |

  @AUT-4051 @under-development
  Scenario Outline: User is signing in via the one login v2 app
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with option channel-strategic-app and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the "<Page>" page
    Then the URL is present with suffix "open-one-login-in-web-browser"
    Examples:
      | Mfa Type | Link Text                                     | Page                                               |
      | App      | I do not have access to the authenticator app | Open GOV.UK One Login in a web browser to continue |
      | SMS      | Problems with the code?                       | Open GOV.UK One Login in a web browser to continue |

@lockouts @lockout_sign_in
Feature: Sign in lockouts

  # ENTER INCORRECT PASSWORD TOO MANY TIMES DURING SIGN IN - 2060
  Scenario: A user is blocked when they enter too many incorrect passwords during sign in.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters an incorrect password 6 times
    Then the 2hr You entered the wrong password too many times screen is displayed
    When the user "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT" with a lockout for too many incorrect passwords attempts to sign in during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong password is displayed

  # USER BLOCKED FOR TOO MANY INCORRECT PASSWORDS CAN RESET THEIR PASSWORD AND BLOCK IS LIFTED
  Scenario: When a user is blocked due to entering too many incorrect passwords during sign in they can reset their password.
    Given the user "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT_RESET_PW" is on the blocked page for entering too many incorrect passwords
    When the user resets their password
    Then the block is lifted and the user "SIGN_IN_INCORRECT_PASSWORD_LOCKOUT_RESET_PW" can login

  # ENTER INCORRECT SMS SECURITY CODE TOO MANY TIMES DURING SIGN IN - 2064
  Scenario: A user is blocked when they enter too many incorrect sms codes during sign in.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "SIGN_IN_INCORRECT_SMS_CODE_LOCKOUT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "SIGN_IN_INCORRECT_SMS_CODE_LOCKOUT" with a lockout for too many incorrect sms security codes attempts to sign in during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed

  # REQUEST TOO MANY SMS SECURITY CODE TOO MANY TIMES DURING SIGN IN - 2378
  Scenario: A user is blocked when they request too many sms codes during sign in.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "SIGN_IN_REQUEST_SMS_CODE_LOCKOUT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the 2hr You asked to resend the security code too many times screen is displayed
    When the user "SIGN_IN_REQUEST_SMS_CODE_LOCKOUT" with a lockout for requesting too many sms security code resends attempts to sign in during the lockout period
    Then the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed

  # ENTER TOO MANY INCORRECT AUTH APP CODES TOO MANY TIMES DURING SIGN IN - 2061
  Scenario: A user is blocked when they enter an incorrect auth app security code more than 5 times during sign in.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "SIGN_IN_AUTH_APP_CODE_LOCKOUT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    And the user enters an incorrect auth app security code 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "SIGN_IN_AUTH_APP_CODE_LOCKOUT" with a lockout for too many incorrect auth app codes reattempts to sign in during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed
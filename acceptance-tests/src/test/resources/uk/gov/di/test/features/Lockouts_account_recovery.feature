@lockouts @lockout_acc_rec
Feature: Account recovery lockouts

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING ACCOUNT RECOVERY - 2081 - PASS
  Scenario: A user is blocked when they enter an incorrect email OTP code 6 times during a change of auth method.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "ACCOUNT_RECOVERY_INCORRECT_EMAIL_CODE" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "ACCOUNT_RECOVERY_INCORRECT_EMAIL_CODE" with a lockout for too many incorrect email security codes attempts to change the way they get security codes during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed

  # REQUEST EMAIL OTP TOO MANY TIMES DURING ACCOUNT RECOVERY - 2382
  Scenario: A user with sms authentication is blocked when they request their email OTP code 6 times during a change of auth method.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_3" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the 2hr You asked to resend the security code too many times screen is displayed
    When the user "TEST_USER_ACCOUNT_RECOVERY_EMAIL_3" with a lockout for requesting too many email security codes attempts to change the way they get security codes during the lockout period
    Then the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed

  # ENTER INCORRECT SMS SECURITY CODE TOO MANY TIMES DURING ACCOUNT RECOVERY - REG
  Scenario: A user is blocked when they enter too many incorrect sms codes during a change of auth method.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "ACCOUNT_RECOVERY_INCORRECT_SMS_CODE" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the 15min You entered the wrong security code too many times screen is displayed
    When the user "ACCOUNT_RECOVERY_INCORRECT_SMS_CODE" with a lockout for too many incorrect sms security codes attempts to change the way they get security codes during the lockout period
    #Then the 15min You cannot get a new security code at the moment screen is displayed
    Then the 15min You entered the wrong security code too many times screen is displayed

  # REQUEST SMS CODE TOO MANY TIMES DURING ACCOUNT RECOVERY - 2380
  Scenario: A user is blocked when they request sms code more than 5 times during a change of auth method.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "ACCOUNT_RECOVERY_REQUEST_SMS_CODE_LOCKOUT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the 2hr You asked to resend the security code too many times screen is displayed
    When the user "ACCOUNT_RECOVERY_REQUEST_SMS_CODE_LOCKOUT" with a lockout for requesting too many sms security codes attempts to change the way they get security codes during the lockout period
    Then the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed

  # NO LIMIT ON ENTERING INCORRECT AUTH APP CODES DURING ACCOUNT RECOVERY - REG
  Scenario: A user is not blocked when they enter an incorrect auth app code during a change of auth method.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "ACCOUNT_RECOVERY_INCORRECT_AUTH_APP_CODE_NO_LIMIT" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Authenticator app for smartphone, tablet or computer"
    Then the user is taken to the "Set up an authenticator app" page
    When the user enters an incorrect auth app security code a further 10 times
    Then no lockout is triggered and the user remains on the "Set up an authenticator app" page
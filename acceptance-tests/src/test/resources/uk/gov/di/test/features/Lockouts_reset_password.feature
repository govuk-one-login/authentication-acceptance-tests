@lockouts @lockout_reset_pw @build @staging
Feature: Reset password lockouts

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING PASSWORD RESET - 2071
  @build-sp
  Scenario: A user is blocked when they enter an incorrect email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL" with a lockout for wrong email security codes reattempts to change their password during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed

  # REQUEST EMAIL OTP TOO MANY TIMES DURING PASSWORD RESET - 2381
  @build-sp
  Scenario: A user is blocked when they request an email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the 2hr You asked to resend the security code too many times screen is displayed
    When the user "TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL" with a lockout for requesting too many email security codes reattempts to change their password during the lockout period
    Then the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed

  # ENTER INCORRECT SMS CODE TOO MANY TIMES DURING PASSWORD RESET - 2070
  @build-sp
  Scenario: A user is blocked when they enter an incorrect sms security code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_SMS_USER_1" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    And the user enters an incorrect phone security code 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "PASSWORD_RESET_SMS_USER_1" with a lockout for wrong sms security codes reattempts to change their password during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed

  # REQUEST SMS CODE TOO MANY TIMES DURING PASSWORD RESET - 2379
  @build-sp
  Scenario: A user is blocked when they request an sms code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_SMS_USER_2" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the 2hr You asked to resend the security code too many times screen is displayed
    When the user "PASSWORD_RESET_SMS_USER_2" with a lockout for requesting too many sms security codes reattempts to change their password during the lockout period
    Then the 2hr You cannot sign in at the moment screen for requesting too many security code resends is displayed

  # ENTER INCORRECT AUTH APP CODE TOO MANY TIMES DURING PASSWORD RESET - 2072
  @build-sp-fail
  Scenario: A user is blocked when they enter an incorrect auth app security code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_AUTH_APP_USER_1" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user enters an incorrect auth app security code 6 times
    Then the 2hr You entered the wrong security code too many times screen is displayed
    When the user "PASSWORD_RESET_AUTH_APP_USER_1" with a lockout for too many incorrect auth app codes reattempts to change their password during the lockout period
    Then the 2hr You cannot sign in at the moment screen for wrong security codes is displayed
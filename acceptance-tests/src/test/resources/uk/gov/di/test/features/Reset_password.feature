@ResetPassword @2fa
Feature: Reset password

  Scenario: An sms user can successfully reset their password
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "RESET_PW_USER_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    Then the link "change how you get security codes" is not available
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

  Scenario: An auth app user can successfully reset their password
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "RESET_PW_USER_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the link "I do not have access to the authenticator app" is not available
    And the link "change how you get security codes" is not available
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING PASSWORD RESET
  Scenario: A user is blocked when they enter an incorrect email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  # REQUEST EMAIL OTP TOO MANY TIMES DURING PASSWORD RESET
  Scenario: A user is blocked when they request an email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  # ENTER INCORRECT SMS CODE TOO MANY TIMES DURING PASSWORD RESET
  Scenario: A user is blocked when they enter an incorrect sms security code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_SMS_USER_1" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    And the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  # REQUEST SMS CODE TOO MANY TIMES DURING PASSWORD RESET
  Scenario: A user is blocked when they request an sms code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_SMS_USER_2" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  # ENTER INCORRECT AUTH APP CODE TOO MANY TIMES DURING PASSWORD RESET
  Scenario: A user is blocked when they enter an incorrect auth app security code more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PASSWORD_RESET_AUTH_APP_USER_1" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user enters an incorrect auth app security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
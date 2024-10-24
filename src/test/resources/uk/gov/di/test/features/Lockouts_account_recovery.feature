@lockouts @lockout_acc_rec @build @staging
Feature: Account recovery lockouts

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING ACCOUNT RECOVERY - 2081 - PASS
  Scenario: A user is blocked when they enter an incorrect email OTP code 6 times during a change of auth method.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you entered the wrong security code too many times"
    * the lockout duration is 2 hours

  # REQUEST EMAIL OTP TOO MANY TIMES DURING ACCOUNT RECOVERY - 2382
  Scenario: A user with sms authentication is blocked when they request their email OTP code 6 times during a change of auth method.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the "You asked to resend the security code too many times" lockout screen is displayed
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you asked to resend the security code too many times"
    * the lockout duration is 2 hours

  # ENTER INCORRECT SMS SECURITY CODE TOO MANY TIMES DURING ACCOUNT RECOVERY - REG
  Scenario: A user is blocked when they enter too many incorrect sms codes during a change of auth method.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    * the lockout duration is 15 minutes

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    * the lockout duration is 15 minutes

  # REQUEST SMS CODE TOO MANY TIMES DURING ACCOUNT RECOVERY - 2380
  Scenario: A user is blocked when they request sms code more than 5 times during a change of auth method.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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
    Then the "You asked to resend the security code too many times" lockout screen is displayed
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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
    Then the "You cannot sign in at the moment" lockout screen is displayed
    * the lockout reason is "you asked to resend the security code too many times"
    * the lockout duration is 2 hours


  # NO LIMIT ON ENTERING INCORRECT AUTH APP CODES DURING ACCOUNT RECOVERY - REG
  Scenario: A user is not blocked when they enter an incorrect auth app code during a change of auth method.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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

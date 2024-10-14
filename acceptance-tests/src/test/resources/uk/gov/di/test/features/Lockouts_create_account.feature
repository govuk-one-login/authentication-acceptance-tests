@lockouts @lockout_create_account @build @staging
Feature: Create account lockouts

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING CREATE ACCOUNT - 2690
  Scenario: A user is not blocked when they enter an incorrect email OTP code more than 5 times during create account but must request a new code to continue.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the non blocking "You entered the wrong security code too many times" lockout screen is displayed
    When the user selects to get a new code
    Then the user is able to complete account creation

  # ENTER INCORRECT EMAIL OTP TOO MANY TIMES DURING CREATE ACCOUNT VIA ACCOUNT NOT FOUND SCREEN - 2690
  Scenario: A user is not blocked when they enter an incorrect email OTP code more than 5 times during create account via account not found page, but must request a new code to continue.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email address to sign in" page
    When the user enters their email address
    Then the user is taken to the "You do not have a GOV.UK One Login" page
    When the user chooses to create an account
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the non blocking "You entered the wrong security code too many times" lockout screen is displayed
    When the user selects to get a new code
    Then the user is able to complete account creation

  # REQUEST TOO MANY EMAIL CODES DURING CREATE ACCOUNT - 2445
  Scenario: A user is blocked when they request their email OTP code 6 times during create account.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the "You asked to resend the security code too many times" lockout screen is displayed
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the "You cannot create a GOV.UK One Login at the moment" lockout screen is displayed
    * the lockout reason is "you asked to resend the security code too many times"
    * the lockout duration is 2 hours

  # ENTER INCORRECT SMS SECURITY CODE TOO MANY TIMES DURING CREATE ACCOUNT - REG
  Scenario: A user is blocked when they enter an incorrect sms security code more than 5 times during create account.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    * the lockout duration is 15 minutes

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "You have a GOV.UK One Login" page
    When the user enters their password
    Then the user is taken to the "Finish creating your GOV.UK One Login" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    * the lockout duration is 15 minutes

  # REQUEST SMS CODE TOO MANY TIMES DURING ACCOUNT RECOVERY - 2377
  Scenario: A user is blocked when they request too many sms security codes during create account.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code a further 5 times
    Then the "You asked to resend the security code too many times" lockout screen is displayed
    * the lockout duration is 2 hours

    Given the lockout has not yet expired
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "You have a GOV.UK One Login" page
    When the user enters their password
    Then the user is taken to the "Finish creating your GOV.UK One Login" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the "You cannot create a GOV.UK One Login at the moment" lockout screen is displayed
    * the lockout reason is "you asked to resend the security code too many times"
    * the lockout duration is 2 hours

  # NO LIMIT ON ENTERING INCORRECT AUTH APP CODES DURING CREATE ACCOUNT - REG
  Scenario: A user is not blocked when they enter an incorrect auth app code during create account.
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user enters an incorrect auth app security code a further 10 times
    Then no lockout is triggered and the user remains on the "Set up an authenticator" page

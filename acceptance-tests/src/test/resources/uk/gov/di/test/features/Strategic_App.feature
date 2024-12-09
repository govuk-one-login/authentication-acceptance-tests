@lockouts @lockout_sign_in  @build-sp @staging-sp
Feature: Sign in lockouts for Strategic App

  # ENTER INCORRECT PASSWORD TOO MANY TIMES DURING SIGN IN - 2060
  Scenario: A user is blocked when they enter too many incorrect passwords during sign in.
    Given a user exists
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters an incorrect password 6 times
    Then the "You entered the wrong password too many times" lockout screen is displayed
    And the "wait 2 hours, then try again" lockout Text is displayed
    * the lockout duration is 2 hours

    When the lockout has not yet expired
    And the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the "You cannot sign in at the moment" lockout screen is displayed
    And the "[Reset your password, ., , You can also wait 2 hours, then try again.]" retry Text is displayed
    * the lockout duration is 2 hours

  Scenario: A user is blocked when they enter too many incorrect sms codes during sign in.
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters an incorrect phone security code 6 times
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    And the "Wait 2 hours, then try again." lockout Text for SMS is displayed
    * the lockout duration is 2 hours

    When the lockout has not yet expired
    And the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the "You cannot sign in at the moment" lockout screen is displayed
    And the "Wait 2 hours, then try again." lockout Text for SMS is displayed
    * the lockout duration is 2 hours
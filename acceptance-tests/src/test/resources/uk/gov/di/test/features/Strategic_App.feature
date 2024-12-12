@lockouts @lockout_sign_in  @build-sp
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


#  Update MFA reset content for Lockout Scenarios
  @AUT-3839-AUT-3838
  Scenario: Strategic app user is blocked when user enters incorrect email OTP 6x with SMS as their secondary method
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    And the lockout duration is 2 hours
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed
    Given the lockout has not yet expired
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the "You cannot sign in at the moment" lockout screen is displayed
    And the lockout reason is "you entered the wrong security code too many times"
    And the lockout duration is 2 hours
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed


  @AUT-3839-AUT-3838
  Scenario: Strategic app user is blocked when user enters incorrect email OTP 6x with Auth App as their secondary method
    Given a user with App MFA exists
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the "You entered the wrong security code too many times" lockout screen is displayed
    And the lockout duration is 2 hours
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed
    Given the lockout has not yet expired
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "change how you get security codes" link
    Then the "You cannot sign in at the moment" lockout screen is displayed
    And the lockout reason is "you entered the wrong security code too many times"
    And the lockout duration is 2 hours
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed



  @AUT-3839-AUT-3838
  Scenario: Strategic app user is blocked when they request sms code more than 5 times during a change of auth method
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
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
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed
    Given the lockout has not yet expired
    When the user comes from the stub relying party with option Strategic-App and is taken to the "Sign in to GOV.UK One Login" page
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
    And the "Wait 2 hours, then try again." message regarding what can you do is displayed

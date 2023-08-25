@AccountRecovery
Feature: Account recovery

  Scenario: A user with text message authentication changes password and logs in with their MFA. Changes their auth method to auth app, then logs in with the new password and auth app.
    Given the existing user has sms authentication method and their account is not blocked
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    Then the link "change how you get security codes" is not available
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their new password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters their "email" code
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects "Authenticator app for smartphone, tablet or computer"
    Then the user is taken to the "Set up an authenticator app" page
    When the existing user adds the secret key on the screen to their auth app
    And the existing user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    And confirmation that the user will get security codes via "auth app" is displayed
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their new password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the existing user enters the security code from the auth app
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page


  Scenario: A user with auth app authentication changes password and logs in with their MFA. Changes their auth method to text message, then logs in with the new password and text message auth method.
    Given the existing user has auth app authentication method and their account is not blocked
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    And the link "I do not have access to the authenticator app" is not available
    And the link "change how you get security codes" is not available
    When the existing user enters the security code from the auth app
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their new password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects link "I do not have access to the authenticator app"
    And the user selects link "change how you get security codes"
    Then the user is taken to the "Check your email" page
    When the user enters their "email" code
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    And confirmation that the user will get security codes via "text message" is displayed
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their new password
    Then the user is taken to the "Check your phone" page
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page


  Scenario: A user with sms authentication is blocked when they request their email OTP code 6 times (including the initial send on entry to screen) during a change of auth method.
    Given the existing user has sms authentication method
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    #NOTE: The 5th resend request is the 6th sending of the email code as one is sent on initial entry to the Check Your Email page
    Then the user is taken to the "You asked to resend the security code too many times" page
    When the user selects "get a new code" link
    Then the user is taken to the "You cannot get a new security code at the moment" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "You asked to resend the security code too many times" page


  Scenario: A user with auth app authentication is blocked when they enter an incorrect email OTP 6 times during a change of auth method.
    Given the existing user has auth app authentication method
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
    When the user selects "get a new code" link
    Then the user is taken to the "You cannot get a new security code at the moment" page

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the "You entered the wrong security code too many times" page
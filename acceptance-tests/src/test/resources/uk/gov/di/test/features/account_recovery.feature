@AccountRecovery @build @staging
Feature: Account recovery

  Scenario: An sms user can change how they get security codes and log in with new method
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
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
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    And confirmation that the user will get security codes via "auth app" is displayed
    Then the user is returned to the service
    And the user logs out

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is returned to the service
    And the user logs out


  Scenario: An auth app user can change how they get security codes and log in with new method
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects link "I do not have access to the authenticator app"
    And the user selects link "change how you get security codes"
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    And confirmation that the user will get security codes via "text message" is displayed
    Then the user is returned to the service
    And the user logs out

    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out
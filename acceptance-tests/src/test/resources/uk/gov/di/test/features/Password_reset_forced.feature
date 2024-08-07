@PasswordResetForced
Feature: User forced to reset their One Login password
  An auth app user
  Scenario: An sms app user has to reset their password when password reset intervention is placed on their account
    Given TICF then "reset password" user account: "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You need to reset your password" page
    When user selects continue
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Example - GOV.UK - User Info" page



  Scenario: An auth app user has to reset their password when password reset intervention is placed on their account
    Given TICF then "reset password" user account: "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "You need to reset your password" page
    When user selects continue
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Example - GOV.UK - User Info" page


  Scenario: An auth app user can log in when the intervention team has been removed password reset from their One Login account
    Given the user comes from the stub relying party with options: "2fa-on"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    And user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_3" email address
    Then the user is taken to the "Enter your password" page
    And the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    And the link "I do not have access to the authenticator app" is available
    And the link "change how you get security codes" is available
    When the user enters the security code from the auth app
    Then the user is signed in to their One Login



  Scenario: An auth app user resets their password using the I have forgotten my password link
    Given TICF then "reset password" user account: "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Example - GOV.UK - User Info" page


  Scenario: An sms app user is forced to resets their password and they choose password in top 100k password list
    Given TICF then "reset password" user account: "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You need to reset your password" page
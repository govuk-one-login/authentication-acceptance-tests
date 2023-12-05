@PasswordResetForced
Feature: User forced to reset their One Login password

  Scenario: Sms app user has to reset their password when password reset intervention is placed on their account (happy path)
#  Scenario : User resets their password from the intervention ‘You need to reset your password’ screen
    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
#Start of new functionality
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You need to reset your password" page
    When user selects continue
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
#End of new functionality
    Then the user is taken to the "Example - GOV.UK - User Info" page



  Scenario: Auth app user has to reset their password when password reset intervention is placed on their account (happy path)
#  Scenario : User resets their password from the intervention ‘You need to reset your password’ screen
    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2"
    And the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
#Start of new functionality
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "You need to reset your password" page
    When user selects continue
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
#End of new functionality
    Then the user is taken to the "Example - GOV.UK - User Info" page


  Scenario: Auth app user can log in when their One Login account password reset intervention has been removed
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




#  Scenario : User is signed in to an 1FA RP via One Login, TICF put in place a password reset intervention, then the user accesses a 2FA RP (uplift) (for auth user)
#    Given the user comes from the stub relying party with options: "2fa-off"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
#    Then the user is taken to the "Example - GOV.UK - User Info" page
#    When TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "You need to enter a security code" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "Your Welcome to Welcome to GOV.UK  One Login has been permanently locked" page
#
#
#  Scenario: User resets their password using the I have forgotten my password link (for auth user)
#    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2" email address
#    Then the user is taken to the "Enter your password" page
#    When the user clicks the forgotten password link
#    Then the user is taken to the "Check your email" page
#    When the user enters the six digit security code from their email
##Start of new functionality
#    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
#    When the user enters the security code from the auth app
#    Then the user is taken to the "Reset your password" page
#    When the user enters valid new password and correctly retypes it
##End of new functionality
#    Then the user is taken to the "Example - GOV.UK - User Info" page
#
#
#  Scenario: User resets their password as they have a password on the unacceptable password list (for sms user)
#    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password which is on the top 100k password list
##Start of new functionality
#    Then the user is taken to the "Check your phone" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "You need to reset your password" page
##End of new functionality
#
#
#
#  Scenario: User has a reset their password intervention and accepts the latest Terms and Conditions
#    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
##Start of new functionality
#    Then the user is taken to the "Check your phone" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "terms of use update" page
#    When the user agrees to the updated terms and conditions
#    Then the user is taken to the "You need to reset your password" page
##End of new functionality
#
#
#  Scenario: User clicks ‘change how you get security codes’ on the ‘Check your phone/Enter the 6 digit code you see in your authenticator app’ screen
#    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
##Start of new functionality
#    Then the user is taken to the "Check your phone" page
#    When the user selects "Problems with the code?" link
#    And the user selects "change how you get security codes" link
#    Then the user is taken to the "Check your email" page
#    When the user enters the six digit security code from their email
#    Then the user is taken to the "You need to reset your password" page
#    When user selects continue
#    Then the user is taken to the "Check your email" page
#    When the user enters the six digit security code from their email
#    Then the user is taken to the "Reset your password" page
#    When the user enters valid new password and correctly retypes it
##End of new functionality
##Account recovery screens continues
#    Then the user is taken to the "How do you want to get security codes" page
#    When the user selects radio button "Text message"
#    Then the user is taken to the "Enter your mobile phone number" page
#    When the user enters their mobile phone number
#    Then the user is taken to the "Check your phone" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "You’ve changed how you get security codes" page
#    And confirmation that the user will get security codes via "text message" is displayed
#    Then the user is taken to the "Example - GOV.UK - User Info" page
#
#
#
#
#
#  Scenario: User resets their password from the ‘You need to reset your password’ screen and has a password reset AND identity reset intervention
#    Given TICF then "reset password" user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2"
#    And identity reset intervention is applied to user account: "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2"
#    And the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_2" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
##Start of new functionality
#    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
#    When the user enters the security code from the auth app
#    Then the user is taken to the "You need to reset your password" page
#    When user selects continue
#    Then the user is taken to the "Check your email" page
#    When the user enters the six digit security code from their email
#    Then the user is taken to the "Reset your password" page
#    When the user enters valid new password and correctly retypes it
#    Then Auth will send the user to Orchestration (via backend)
##  this need to be handled by Orchestration team
##End of new functionality

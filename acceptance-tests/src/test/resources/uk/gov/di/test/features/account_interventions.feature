@AccountInterventions @build
Feature: Account interventions

  @suspended @build-sp-fail
  Scenario: Sms user cannot log in when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp-fail
  Scenario: Auth app user cannot log in when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp-fail
  Scenario: Sms user with outdated terms and conditions cannot log in when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_4" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp-fail
  Scenario: Sms user cannot change their password when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp
  Scenario: Auth app user with a current password on the top 100k unacceptable password list cannot change their password when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_3" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user enters the security code from the auth app
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp-fail
  Scenario: Sms user cannot change the way they get security codes when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a temporarily suspended account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEMPORARILY_SUSPENDED_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Sorry, there is a problem" page

  @locked @build-sp
  Scenario: Sms user cannot log in when they have a permanently locked account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Auth app user cannot log in when they have a permanently locked account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Auth app user with outdated terms and conditions cannot log in when they have a permanently locked account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_6" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp-fail
  Scenario: Auth app user cannot create a new account using the email address of a permanently locked address
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_5" email address
    Then the user is taken to the "You have a GOV.UK One Login" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Sms user cannot change their password when they have a permanently locked account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_3" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp-fail
  Scenario: Sms user with a current password on the top 100k unacceptable password list cannot change their password when they have a permanently locked account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_7" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Sms user cannot change the way they get security codes when they have a permanently locked account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_4" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a permanently locked account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "PERMANENTLY_LOCKED_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

#  @reset_password # commented out due to an unidentified data issue that causes failure in th pipeline. Runs fine locally.
#  Scenario: Sms user forced to reset their password when a password reset intervention has been placed on their account
#    Given the user comes from the stub relying party with options: "default"
#    When the user is taken to the "Create your GOV.UK One Login or sign in" page
#    And the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_1" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
#    Then the user is taken to the "Check your phone" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "You need to reset your password" page
#    When the user clicks the continue button
#    Then the user is taken to the "Check your email" page
#    When the user enters the six digit security code from their email
#    Then the user is taken to the "Reset your password" page
#    The test needs to stop here in Build, due to there being static data in the AIS stub the reset pw flag is not removed and so the correct path is not followed
#    When the user enters valid new password and correctly retypes it
#    Then the user is returned to the service
#    And the user clicks logout

  @reset_password @build-sp
  Scenario: Auth app user forced to reset their password when a password reset intervention has been placed on their account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_2" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "You need to reset your password" page
    When the user clicks the continue button
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user clicks logout

  @reset_password @build-sp
  Scenario: Auth app user with a password reset intervention on their account is able to use the I have forgotten my password link
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_4" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user clicks logout

  @reset_password @build-sp
  Scenario: Sms user is forced to reset their password when they have a password reset intervention on their account and their existing password is on top 100k password list
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_5" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user clicks logout

  @reset_password @build-sp
  Scenario: Auth app user with outdated terms and conditions cannot log in when they have a password reset intervention on their account
    Given the user comes from the stub relying party with options: "default"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_6" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "You need to reset your password" page
    When the user clicks the continue button
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user clicks logout

  @reset_password @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a password reset intervention on their account
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_PASSWORD_RESET_ACCOUNT_EMAIL_7" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "You need to reset your password" page
    When the user clicks the continue button
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user clicks logout

  @locked @suspended @reset_password
  Scenario: Auth app user can log in when their One Login account intervention has been removed
    Given the user comes from the stub relying party with options: "2fa-on"
    When the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "ACCOUNT_INTERVENTION_REMOVED_EMAIL_1" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is returned to the service
    And the user clicks logout
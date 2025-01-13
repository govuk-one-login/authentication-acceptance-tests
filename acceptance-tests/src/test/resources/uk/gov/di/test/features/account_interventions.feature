@AccountInterventions @build
Feature: Account interventions

  @suspended @tw-test @focusme @build-sp-fail
  Scenario: Sms user cannot log in when they have a temporarily suspended account
    Given a user with SMS MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @tw-test @build-sp-fail
  Scenario: Auth app user cannot log in when they have a temporarily suspended account
    Given a user with App MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @tw-test @build-sp-fail
  Scenario: Sms user with outdated terms and conditions cannot log in when they have a temporarily suspended account
    Given a user with SMS MFA exists
    And the user has a temporarily suspended intervention
    And the user has not yet accepted the latest terms and conditions
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp @dev
  Scenario: Sms user cannot change their password when they have a temporarily suspended account
    Given a user with SMS MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp @dev
  Scenario: Auth app user with a current password on the top 100k unacceptable password list cannot change their password when they have a temporarily suspended account
    Given a user with App MFA exists
    And the user's password is on the top 100k unacceptable password list
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the user enters the security code from the auth app
    Then the user is taken to the "Sorry, there is a problem" page

  @suspended @build-sp
  Scenario: Sms user cannot change the way they get security codes when they have a temporarily suspended account
    Given a user with SMS MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page


  @suspended @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a temporarily suspended account
    Given a user with App MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "There’s a problem with this service - GOV.UK One Login" page
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              |
      | App      | I do not have access to the authenticator app | Identity check failed     |

  @locked @build-sp @dev
  Scenario: Sms user cannot log in when they have a permanently locked account
    Given a user with SMS MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp @dev
  Scenario: Auth app user cannot log in when they have a permanently locked account
    Given a user with App MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp @dev
  Scenario: Auth app user with outdated terms and conditions cannot log in when they have a permanently locked account
    Given a user with App MFA exists
    And the user has a permanently locked intervention
    And the user has not yet accepted the latest terms and conditions
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp @dev
  Scenario: Auth app user cannot create a new account using the email address of a permanently locked account
    Given a user with App MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters the email address
    Then the user is taken to the "You have a GOV.UK One Login" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp @dev
  Scenario: Sms user cannot change their password when they have a permanently locked account
    Given a user with SMS MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp @dev
  Scenario: Sms user with a current password on the top 100k unacceptable password list cannot change their password when they have a permanently locked account
    Given a user with SMS MFA exists
    And the user's password is on the top 100k unacceptable password list
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Your GOV.UK One Login has been permanently locked" page

  @locked @build-sp
  Scenario: Sms user cannot change the way they get security codes when they have a permanently locked account
    Given a user with SMS MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "There’s a problem with this service" page
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              |
      | SMS      | I do not have access to the authenticator app | Identity check failed     |


  @locked @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a permanently locked account
    Given a user with App MFA exists
    And the user has a permanently locked intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "There’s a problem with this service" page
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              |
      | App      | I do not have access to the authenticator app | Identity check failed     |

#  @reset_password # commented out due to an unidentified data issue that causes failure in th pipeline. Runs fine locally.
#  Scenario: Sms user forced to reset their password when a password reset intervention has been placed on their account
#    Given a user with SMS MFA exists
#    And the user has a password reset intervention
#    When the user comes from the stub relying party with default options
#    When the user is taken to the "Create your GOV.UK One Login or sign in" page
#    And the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When the user enters their email address
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

  @reset_password @build-sp @dev
  Scenario: Auth app user forced to reset their password when a password reset intervention has been placed on their account
    Given a user with App MFA exists
    And the user has a password reset intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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

  @reset_password @build-sp @dev
  Scenario: Auth app user with a password reset intervention on their account is able to use the I have forgotten my password link
    Given a user with App MFA exists
    And the user has a password reset intervention
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service

  @reset_password @build-sp @dev
  Scenario: Sms user is forced to reset their password when they have a password reset intervention on their account and their existing password is on top 100k password list
    Given a user with SMS MFA exists
    And the user has a password reset intervention
    And the user's password is on the top 100k unacceptable password list
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service

  @reset_password @build-sp @dev
  Scenario: Auth app user with outdated terms and conditions cannot log in when they have a password reset intervention on their account
    Given a user with App MFA exists
    And the user has a password reset intervention
    And the user has not yet accepted the latest terms and conditions
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
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

  @reset_password @build-sp
  Scenario: Auth app user cannot change the way they get security codes when they have a password reset intervention on their account
    Given a user with App MFA exists
    And the user has a password reset intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user selects "I do not have access to the authenticator app" link
    When the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user selects radio button "Text message"
    Then the user is taken to the "Enter your mobile phone number" page
    And the user enters their mobile phone number
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page

  @suspended @reset_password @tw-test @build-sp-fail
  Scenario: Auth app user can log in when their One Login account intervention has been removed
    Given a user with App MFA exists
    And the user has a temporarily suspended intervention
    When the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Sorry, there is a problem" page

    When the user's interventions have been removed
    And the user comes from the stub relying party with option 2fa-on and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is returned to the service

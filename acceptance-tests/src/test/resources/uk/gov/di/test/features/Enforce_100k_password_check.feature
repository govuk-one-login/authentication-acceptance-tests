@EnforcePasswordReset @build @staging @build-sp @staging-sp
Feature: Enforce 100k password check
  If an existing user has a current password that is in the list of top 100k passwords, they are forced to change their password

  Scenario: An sms user is forced to reset their top 100k password
    Given a user with SMS MFA exists
    And the user's password is on the top 100k unacceptable password list
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user logs out

  Scenario: An auth app user is forced to reset their top 100k password
    Given a user with App MFA exists
    And the user's password is on the top 100k unacceptable password list
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Enter a security code from your authenticator app" page
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service
    And the user logs out

@ResetPassword @build @staging @build-sp @staging-sp @dev
Feature: Reset password

  Scenario: An sms user can successfully reset their password
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Check your phone" page
    When the user selects "Problems with the code?" link
    Then the link "change how you get security codes" is not available
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service

  Scenario: An auth app user can successfully reset their password
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Enter a security code from your authenticator app" page
    And the link "I do not have access to the authenticator app" is not available
    And the link "change how you get security codes" is not available
    When the user enters the security code from the auth app
    Then the user is taken to the "Reset your password" page
    When the user enters valid new password and correctly retypes it
    Then the user is returned to the service


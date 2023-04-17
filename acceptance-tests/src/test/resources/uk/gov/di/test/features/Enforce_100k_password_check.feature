@EnforcePasswordReset
Feature: Enforce 100k password check
  If an existing user has a current password that is in the list of top 100k passwords, they are forced to change their password

  Scenario: Existing user forced to reset their top 100k password
    Given the existing user has a password which is on the top 100k passwords list
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password which is on the top 100k password list
    Then the user is taken to the "Reset your password" page
    When the user resets their password to an invalid one
    Then the "Your password must be at least 8 characters long and must include letters and numbers" error message is displayed
    When the user resets their password to one that is on the list of top 100k passwords
    Then the "Enter a stronger password. Do not use very common passwords, such as ‘password’ or a sequence of numbers." error message is displayed
    When the user resets their password but enters mismatching new passwords
    Then the "Enter the same password in both fields" error message is displayed
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "You need to enter a security code" page
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page
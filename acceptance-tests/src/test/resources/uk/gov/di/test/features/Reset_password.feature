@ResetPassword
Feature: Reset password

  Scenario: User can successfully reset their password
    Given the existing user has valid credentials and wants to reset their password
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    When the user resets their password to be the same as their current password
    Then the "You are already using that password. Enter a different password" error message is displayed
    When the user resets their password to an invalid one
    Then the "Your password must be at least 8 characters long and must include letters and numbers" error message is displayed
    When the user resets their password to one that is on the list of top 100k passwords
    Then the "Enter a stronger password. Do not use very common passwords, such as ‘password’ or a sequence of numbers." error message is displayed
    When the user resets their password but enters mismatching new passwords
    Then the "Enter the same password in both fields" error message is displayed
    When the user enters valid new password and correctly retypes it
    Then the user is taken to the "Check your phone" page
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

# REQUEST OTP TOO MANY TIMES DURING PASSWORD RESET --- AUT-1274
  Scenario: A user is blocked when they request an email OTP more than 5 times during a password reset.
    Given the existing user wants to change their password
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the user is taken to the "Enter your email" page
    When the existing user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    When the user selects link "get a new code"
    Then the user is taken to the "You cannot get a new security code at the moment" page


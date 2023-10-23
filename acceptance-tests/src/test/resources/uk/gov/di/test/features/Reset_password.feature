@ResetPassword
Feature: Reset password

  Scenario: User can successfully reset their password
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "RESET_PW_USER_EMAIL" email address
    Then the user is taken to the "Enter your password" page
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
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page

# REQUEST OTP TOO MANY TIMES DURING PASSWORD RESET --- AUT-1274
  Scenario: A user is blocked when they request an email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TOO_MANY_EMAIL_OTP_REQUESTS_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user enters an incorrect email OTP 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page
    When the user selects link "get a new code"
    Then the user is taken to the "You cannot get a new security code at the moment" page

# ENTER INCORRECT OTP TOO MANY TIMES DURING PASSWORD RESET  --- AUT-1283
  Scenario: A user is blocked when they enter an incorrect email OTP more than 5 times during a password reset.
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "INCORRECT_EMAIL_OTP_FOR_PW_RESET_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user requests the email OTP code be sent again a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    When the user selects link "get a new code"
    Then the user is taken to the "You cannot get a new security code at the moment" page
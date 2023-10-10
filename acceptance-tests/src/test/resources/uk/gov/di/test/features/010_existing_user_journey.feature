@Existing
Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user requests phone OTP code 5 times
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "RESEND_CODE_TEST_USER_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the existing user enters their password
    Then the user is taken to the "Check your phone" page
    When the existing user requests the phone otp code 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    When the existing user clicks the get a new code link
    Then the user is taken to the "You cannot get a new security code at the moment" page



  Scenario: Existing user tries to create an account with the same email address
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters "TEST_EXISTING_USER" email address
    Then the user is taken to the "You have a GOV.UK One Login" page


  Scenario: Existing user is correctly prompted to login using sms
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_EXISTING_USER" email address
    Then the user is taken to the "Enter your password" page
    When the existing user enters their password
    Then the user is taken to the "Check your phone" page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service
    And the user clicks logout
    Then the user is taken to the "Signed out" page

  Scenario: Existing user switches content to Welsh
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    And the existing user switches to "Welsh" language
    Then the existing user is taken to the Identity Provider Welsh Login Page
    When the existing user selects sign in
    Then the existing user is taken to the Welsh enter your email page
    When user enters "TEST_EXISTING_USER" email address in Welsh
    Then the existing user is prompted for their password in Welsh
    When the user clicks link "Yn ôl"
    Then the existing user is taken to the Welsh enter your email page
    When the user clicks link "Yn ôl"
    Then the existing user is taken to the Identity Provider Welsh Login Page
    When the existing user switches to "English" language
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page

  Scenario: Existing user logs in without 2FA then uplift with 2FA
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When user enters "TEST_EXISTING_USER" email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is returned to the service
    When the user comes from the stub relying party with options: "default"
    Then the existing user is taken to the enter code uplifted page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service
    When the user clicks logout
    Then the user is taken to the "Signed out" page

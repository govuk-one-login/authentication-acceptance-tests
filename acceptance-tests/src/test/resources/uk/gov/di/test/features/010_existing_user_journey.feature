@Existing
Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user requests phone OTP code 5 times
    Given the existing resend code user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is taken to the enter code page
    When the existing user requests the phone otp code 5 times
    Then the existing user is taken to the you asked to resend the security code too many times page
    When the existing user clicks the get a new code link
    Then the existing user is taken to the you cannot get a new security code page

  Scenario: Existing user tries to create an account with the same email address
    Given the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the existing user enters their email address
    Then the user is taken to the "You have a GOV.UK One Login" page

  Scenario: Existing user is correctly prompted to login using sms
    Given the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is taken to the enter code page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service
    And the user clicks logout
    Then the existing user is taken to the you have signed out page

  Scenario: Existing user switches content to Welsh
    Given the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    And the existing user switches to "Welsh" language
    Then the existing user is taken to the Identity Provider Welsh Login Page
    When the existing user selects sign in
    Then the existing user is taken to the Welsh enter your email page
    When the existing user enters their email address in Welsh
    Then the existing user is prompted for their password in Welsh
    When the user clicks link "Yn ôl"
    Then the existing user is taken to the Welsh enter your email page
    When the user clicks link "Yn ôl"
    Then the existing user is taken to the Identity Provider Welsh Login Page
    When the existing user switches to "English" language
    Then the existing user is taken to the Identity Provider Login Page

  Scenario: Existing user logs in without 2FA then uplift with 2FA
    Given the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "2fa-off"
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is returned to the service
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the enter code uplifted page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service
    When the user clicks logout
    Then the existing user is taken to the you have signed out page

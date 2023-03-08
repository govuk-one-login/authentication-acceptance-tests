@alex
Feature: alex
  User enters a 2FA code wrong six times

  Scenario: Existing user enters the wrong 2FA code 6 times
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
    When the existing user enters the phone otp code "123456" 6 times
    Then the existing user is taken to the entered security code to many times page
    When the existing user clicks the get a new code link
    Then the existing user is taken to the cannot get a new code just now page
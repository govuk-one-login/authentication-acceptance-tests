Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user tries to create an account with the same email address
    Given the account management services are running
    And the existing account management user has valid credentials
    When the existing account management user navigates to account management
    And the existing account management user selects create an account
    Then the exiting account management user is asked to enter their current email address
    When the existing account management user enters their current email address
    Then the existing account management user is taken to the account exists page

  Scenario: Existing user is correctly prompted to login
    Given the login services are running
    And the existing user has valid credentials
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
    And the existing user clicks by name "logout"
    Then the existing user is taken to the you have signed out page

  @UserRequestsCode5Times
  Scenario: Existing user requests phone OTP code 5 times
    Given the login services are running
    And the existing user has valid credentials
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

  Scenario: Existing user logs in without 2FA
    Given the login services are running
    And the existing user has valid credentials
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

  Scenario: Existing user logs in with 2FA
    Given the login services are running
    And the existing user has valid credentials
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the enter code page
    When the existing user enters the six digit security code from their phone
    Then the existing user is returned to the service

  Scenario: Existing user attempts to change their phone number using their existing one
    Given the account management services are running
    And the existing account management user has valid credentials
    When the existing account management user navigates to account management
    Then the existing account management user is taken to the your gov uk account page
    When the existing account management user clicks link by href "/enter-password?type=changePhoneNumber"
    Then the existing account management user is asked to enter their password
    When the existing account management user enter their current password
    Then the existing account management user is taken to the enter your new mobile phone number page
    When the existing account management user enters their existing mobile phone number
    Then the existing account management user is shown an error message
    When the existing account management user clicks link by href "/manage-your-account"
    Then the existing account management user is taken to the your gov uk account page

  Scenario: Existing user updates their cookie preferences
    Given the account management services are running
    And the existing account management user has valid credentials
    When the existing account management user navigates to account management
    Then the existing account management user is taken to the your gov uk account page
    When the existing account management user clicks link by href "/enter-password?type=changePhoneNumber"
    Then the existing account management user is asked to enter their password
    When the existing account management user clicks link by href "https://signin.build.account.gov.uk/cookies"
    And the existing account management user accepts the cookie policy
    And the existing account management user clicks the go back link
    Then the existing account management user is taken to the GOV.UK accounts cookies policy page
    When the existing account management user rejects the cookie policy
    And the existing account management user clicks the go back link
    Then the existing account management user is taken to the GOV.UK accounts cookies policy page

  Scenario: User changes their password
      Given the account management services are running
      And the existing account management user has valid credentials
      When the existing account management user navigates to account management
      Then the existing account management user is taken to the your gov uk account page
      When the existing account management user clicks link by href "/enter-password?type=changePassword"
      Then the existing account management user is asked to enter their current password
      When the existing account management user enter their current password
      Then the existing account management user is taken to the change password page
      When the existing account management user uses their updated password
      And the existing account management user enters their updated password
      Then the existing account management user is taken to password updated confirmation page
      When the existing account management user clicks link by href "/manage-your-account"
      Then the existing account management user is taken to the your gov uk account page

  Scenario: User fails deleting their account due to invalid password
    Given the account management services are running
    And the existing account management user has valid credentials
    When the existing account management user navigates to account management
    Then the existing account management user is taken to the your gov uk account page
    When the existing account management user clicks link by href "/enter-password?type=deleteAccount"
    Then the existing account management user is asked to enter their password
    When the existing account management user enters an invalid password to delete account
    Then the existing account management user is shown an error message
    And the existing account management user is asked to enter their password again

  Scenario: User deletes their account
      Given the account management services are running
      And the existing account management user has valid credentials
      When the existing account management user navigates to account management
      Then the existing account management user is taken to the your gov uk account page
      When the existing account management user clicks link by href "/enter-password?type=deleteAccount"
      Then the existing account management user is asked to enter their password
      When the existing account management user uses their updated password
      And the existing account management user enters their updated password to delete account
      Then the existing account management user is taken to the delete account page
      When the user clicks the delete your GOV.UK account button
      Then the existing account management user is taken to the account deleted confirmation page
      When the not logged in user navigates to account root
      Then the not logged in user is taken to the Identity Provider Login Page
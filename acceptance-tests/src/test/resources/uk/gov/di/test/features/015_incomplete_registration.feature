Feature: Incomplete registration
  New user leaves the registration journey before completing it

  Scenario: Abandon before 2FA setup
    Given the registration services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user creates a password
    Then the new user is taken to the get security codes page
    When the new user chooses text message security codes
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number
    Then the new user is taken to the check your phone page
    When the new user visits the stub relying party
    And a new user has valid credentials
    And the new user clears cookies
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    When the new user enters their email address
    When the new user enters their password
    Then the new user is taken to the finish creating your account get security codes page
    When the new user chooses text message security codes
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number
    Then the new user is taken to the check your phone page
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    Then the new user is taken the the share info page
    When the new user does not agree to share their info
    Then the user is returned to the service
    When the new user clicks link by href "https://build.account.gov.uk"
    When the new user clicks link by href "/enter-password?type=deleteAccount"
    When the new user enters their password
    When the user clicks the delete your GOV.UK account button
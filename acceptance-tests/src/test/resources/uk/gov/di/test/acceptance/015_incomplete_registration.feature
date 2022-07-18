Feature: Incomplete registration
  New user leaves the registration journey before completing it

  Scenario: New user requests new security code too many times
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
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user clicks link by href "/enter-email-create"
    Then the new user is shown an error message
    When the new user clicks link by href "/enter-email-create"
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is shown an error message

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
    Then the new user is taken to the finish creating your account page
    When the new user enters their mobile phone number
    Then the new user is taken to the check your phone page
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    Then the new user is taken the the share info page
    When the new user agrees to share their info
    Then the new user is returned to the service
    When the new user clicks link by href "https://build.account.gov.uk"
    When the new user clicks link by href "/enter-password?type=deleteAccount"
    When the new user enters their password
    When the user clicks button by text Delete account

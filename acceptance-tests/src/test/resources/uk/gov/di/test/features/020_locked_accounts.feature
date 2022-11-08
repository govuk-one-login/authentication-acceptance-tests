@Lock
Feature: Locked accounts
  Existing users get locked out of their accounts

  Scenario: New user enters incorrect email code 6 times
    Given the login services are running
    And the new email code lock user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code incorrectly 5 times
    When the new user enters an incorrect email code one more time
    Then the new user is taken to the security code invalid page
    When the new user clicks link by href "/resend-email-code?requestNewCode=true"
    Then the new user is taken to the resend email code page
    When the new user clicks "resend-email-code"
    Then the new user is asked to check their email
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    Then the new user is taken to the account not found page
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page

  Scenario: New user enters incorrect phone code 6 times
    Given the login services are running
    And the new phone code lock user has valid credentials
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
    When the new user enters the six digit security code incorrectly 5 times
    When the new user enters an incorrect phone code one more time
    Then the new user is taken to the security code invalid page
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    When the new user enters their password
    Then the new user is taken to the finish creating your account get security codes page
    When the new user chooses text message security codes
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number
    Then the new user is taken to the security code invalid page

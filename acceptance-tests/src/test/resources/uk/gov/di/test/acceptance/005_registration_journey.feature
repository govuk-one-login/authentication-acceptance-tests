Feature: Registration Journey
  New user walks through a registration journey

  Scenario: User selects sign in without having an account
    Given the registration services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    Then the new user is taken to the account not found page
    When the new user clicks link by href "/enter-email?type=sign-in"
    Then the new user is taken to the sign in to your account page

  Scenario: User registration unsuccessful with invalid email, six digit code and password
    Given the registration services are running
    And the new user has an invalid email format
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is shown an error message
    When the new user has a valid email address
    And the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code incorrectly 5 times
#    -- Not testing the limit at the moment as this locks the account --
#    Then the new user is taken to the security code invalid page
    When a new user has valid credentials
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user has an invalid password
    And the new user creates a password
    Then the new user is shown an error message

  Scenario: User successfully registers
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
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the account created page
    When the new user clicks the go back to gov.uk link
    Then the new user is taken the the share info page
    When the new user agrees to share their info
    Then the new user is returned to the service
    When the new user clicks by name "logout"
    Then the new user is taken to the signed out page
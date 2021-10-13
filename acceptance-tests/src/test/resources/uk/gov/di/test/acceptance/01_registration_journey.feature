Feature: Registration Journey
  New user walks through a registration journey

  Scenario: User successfully registers
    Given the registration services are running
    And a new user has valid credentials
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user creates a valid password
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number
    Then the new user is taken to the check your phone page
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the account created page
    When the new user clicks the go back to gov.uk link
    Then the new user is taken the the share info page
    When the new user agrees to share their info
    Then the new user is returned to the service
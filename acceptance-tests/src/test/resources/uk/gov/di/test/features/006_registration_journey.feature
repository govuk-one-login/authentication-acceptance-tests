@RegistrationJourney
Feature: Registration Journey
  New user walks through a registration journey

  Scenario: User selects sign in without having an account
    Given the registration services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    Then the new user is taken to the account not found page
    And there are no accessibility violations
    When the new user clicks link by href "/enter-email"
    Then the new user is taken to the sign in to your account page

  Scenario: User redirects to Sign-in to a service from No GOV.UK Account found page
    Given the registration services are running
    And a new user has valid credentials
    When the user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    Then the new user is taken to the account not found page
    When the new user clicks the sign in to a service button
    Then the new user is taken to the sign in to a service page

  Scenario: User registration unsuccessful with invalid email, six digit code and password
    Given the registration services are running
    And the new user has an invalid email format
    And the new user clears cookies
    When the user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is shown an error message
    When the new user has a valid email address
    And the new user enters their email address
    Then the new user is asked to check their email
    When a new user has valid credentials
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user has an invalid password
    And the new user creates a password
    And there are no accessibility violations
    Then the new user is shown an error message
    When the new user has a weak password
    And the new user creates a password
    Then the new user is shown an error message
    When the new user has a short digit only password
    And the new user creates a password
    Then the new user is shown an error message
    When the new user has a sequence of numbers password
    And the new user creates a password
    Then the new user is shown an error message

  Scenario: User successfully registers using sms
    Given the registration services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects create an account
    And there are no accessibility violations
    Then the new user is taken to the enter your email page
    When the new user enters their email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    And there are no accessibility violations
    Then the new user is taken to the create your password page
    When the new user creates a password
    And there are no accessibility violations
    Then the new user is taken to the get security codes page
    When the new user chooses text message security codes
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number using an international dialling code
    Then the new user is taken to the check your phone page
    When the new user clicks link by href "/enter-phone-number"
    Then the new user is taken to the enter phone number page
    When the new user enters their mobile phone number
    Then the new user is taken to the check your phone page
    When the new user enters the six digit security code from their phone
    And there are no accessibility violations
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    And there are no accessibility violations
    Then the user is returned to the service
    When the user clicks logout
    And there are no accessibility violations
    Then the new user is taken to the signed out page
@RegistrationJourney
Feature: Registration Journey
  New user walks through a registration journey

  Scenario: User selects sign in without having an account
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email address to sign in to your GOV.UK One Login" page
    When user enters "TEST_USER_EMAIL" email address
    Then the user is taken to the "No GOV.UK One Login found" page
    When the user clicks link "Try another email address"
    Then the user is taken to the "Enter your email address to sign in to your GOV.UK One Login" page

# commented this test out for now as the stub config has been changed. may be able to use at a later date when the whole stub strategy has been reviewed

#  Scenario: User redirects to Sign-in to a service from No GOV.UK Account found page
#    Given a new user has valid credentials
#    When the not logged in user visits the stub relying party
#    And the not logged in user clicks "govuk-signin-button"
#    Then the new user is taken to the Identity Provider Login Page
#    When the new user selects sign in
#    Then the new user is taken to the sign in to your account page
#    When the new user enters their email address
#    Then the new user is taken to the account not found page
#    When the new user clicks the sign in to a service button
#    Then the new user is taken to the sign in to a service page

  Scenario: User is taken to Check your email page from No GOV.UK One Login found page when Create selected
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email address to sign in to your GOV.UK One Login" page
    When user enters "TEST_USER_EMAIL" email address
    Then the user is taken to the "No GOV.UK One Login found" page
    When the user selects create an account
    Then the user is taken to the "Check your email" page

  Scenario: User registration unsuccessful with invalid email, six digit code and password
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters invalid email address
    Then the user is shown an error message
    When user enters "TEST_USER_EMAIL" email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    And the user creates and enters an invalid password
    And there are no accessibility violations
    Then the user is shown an error message
    And the user creates and enters a weak password
    Then the user is shown an error message
    When the user creates and enters short digit only password
    Then the user is shown an error message
    When the user creates and enters a sequence of numbers password
    Then the user is shown an error message

  Scenario: User successfully registers using sms
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters "TEST_USER_EMAIL" email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses "Text message" to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number using an international dialling code
    Then the user is taken to the "Check your phone" page
    When the user clicks the Back link
    Then the user is taken to the "Enter your mobile phone number" page
    When the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service
    When the user clicks logout
    Then the user is taken to the "Signed out" page
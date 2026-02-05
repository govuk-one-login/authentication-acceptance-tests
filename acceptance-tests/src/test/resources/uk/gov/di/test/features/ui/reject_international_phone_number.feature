@UI @IPN @build @staging @build-sp @staging-sp @dev @RejectInternationalNumbers
Feature: Reject international phone numbers

  As a user creating a GOV.UK One Login
  I want to enter only a UK mobile phone number
  So that international phone numbers and invalid numbers are not accepted

  Scenario: User cannot register using an international or invalid mobile phone number
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    When the user submits a blank UK phone number
    Then the "Enter a UK mobile phone number" error message is displayed
    When the user submits an international phone number in the UK phone number field
    Then the "Enter a UK mobile phone number" error message is displayed
    When the user submits an incorrectly formatted UK phone number
    Then the "Enter a UK mobile phone number, like 07700 900000" error message is displayed
    When the user submits a UK phone number containing non-digit characters
    Then the "Enter a UK mobile phone number using only numbers or the + symbol" error message is displayed


  Scenario: User is not given the option to enter an international phone number
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    Then the option "I do not have a UK mobile number" is not displayed


  Scenario: User can successfully register using a valid UK mobile phone number
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects create an account
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    When the user enters the six digit security code from their email
    Then the user is taken to the "Create your password" page
    When the user creates a password
    Then the user is taken to the "Choose how to get security codes" page
    When the user chooses text message to get security codes
    Then the user is taken to the "Enter your mobile phone number" page
    And the user enters their mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service

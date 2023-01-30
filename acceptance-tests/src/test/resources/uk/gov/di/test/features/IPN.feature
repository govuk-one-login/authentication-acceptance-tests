@IPN
Feature: International Phone Numbers

  Scenario: User cannot register using an invalid UK mobile phone number
    Given a new user has an invalid UK mobile phone number
    And the new user clears cookies
    When the user visits the stub relying party
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
    When the new user submits a blank UK phone number
    Then the "Enter a UK mobile phone number" error message is displayed
    When the new user submits an international phone number in the UK phone number field
    Then the "Enter a UK mobile phone number" error message is displayed
    When the new user submits an incorrectly formatted UK phone number
    Then the "Enter a UK mobile phone number, like 07700 900000" error message is displayed
    When the new user submits a UK phone number containing non-digit characters
    Then the "Enter a UK mobile phone number using only numbers or the + symbol" error message is displayed


  Scenario: User can successfully complete registration using an international phone number
    Given a new user has an invalid international mobile phone number
    And the new user clears cookies
    When the user visits the stub relying party
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
    When the new user ticks I do not have a UK mobile number
    Then the International mobile number field is displayed
    When the new user submits a blank international mobile phone number
    Then the "Enter a mobile phone number" error message is displayed
    When the new user submits an incorrectly formatted international mobile phone number
    Then the "Enter a mobile phone number in the correct format, including the country code" error message is displayed
    When the new user submits an international mobile phone number containing non-digit characters
    Then the "Enter a mobile phone number using only numbers or the + symbol" error message is displayed
    When the new user enters a valid international mobile phone number
    Then the new user is taken to the check your phone page
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the account created page
    When the new user clicks the continue button
    Then the user is returned to the service
    When the user clicks logout
    Then the new user is taken to the signed out page


  Scenario: New user with an international phone number reports that they did not receive their security code
    Given a new user has a valid international mobile phone number
    And the new user clears cookies
    When the user visits the stub relying party
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
    When the new user ticks I do not have a UK mobile number
    Then the International mobile number field is displayed
    When the new user enters a valid international mobile phone number
    Then the new user is taken to the check your phone page
    When the user clicks the support link having not received their security code
    Then the user is taken to the Contact us page in a new tab
    When the user selects "A problem creating a GOV.UK One Login" and proceeds
    Then the user is taken to the "A problem creating a GOV.UK One Login" page
    When the user selects "You did not get a security code" and proceeds
    Then the user is taken to the "You did not get a security code" page
    When the user selects "Text message to a phone number from a different country" and proceeds
    Then the user receives confirmation that their message has been submitted


  Scenario: Existing user with an international phone number reports that their security code did not work
    Given the existing user has a phone code that does not work
    And the new user clears cookies
    When the existing user visits the stub relying party
    And the existing user clicks "govuk-signin-button"
    Then the existing user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the existing user is taken to the enter your email page
    When the existing user enters their email address
    Then the existing user is prompted for their password
    When the existing user enters their password
    Then the existing user is taken to the enter code page
    When the user enters an incorrect phone code
    Then the user is shown an error message
    When the user clicks the support link due to their security code not working
    Then the user is taken to the Contact us page in a new tab
    When the user selects "A problem signing in to your GOV.UK One Login" and proceeds
    Then the user is taken to the "A problem signing in to your GOV.UK One Login" page
    When the user selects "The security code did not work" and proceeds
    Then the user is taken to the "The security code does not work" page
    When the user selects "Text message to a phone number from a different country" and proceeds
    Then the user receives confirmation that their message has been submitted
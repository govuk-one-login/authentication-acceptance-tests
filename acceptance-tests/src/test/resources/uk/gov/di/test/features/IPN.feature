@IPN @build @staging @build-sp @staging-sp
Feature: International Phone Numbers

  Scenario: User cannot register using an invalid UK mobile phone number
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

  Scenario: User can successfully complete registration using an international phone number
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
    When the user ticks I do not have a UK mobile number
    Then the International mobile number field is displayed
    When the user submits a blank international mobile phone number
    Then the "Enter a mobile phone number" error message is displayed
    When the user submits an incorrectly formatted international mobile phone number
    Then the "Enter a mobile phone number in the correct format, including the country code" error message is displayed
    When the user submits an international mobile phone number containing non-digit characters
    Then the "Enter a mobile phone number using only numbers or the + symbol" error message is displayed
    When the user enters a valid international mobile phone number
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

#  Scenario: User with an international phone number reports that they did not receive their security code
#    When the user comes from the stub relying party with default options
#    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
#    When the user clicks on the support link
#    Then the user is taken to the Contact us page in a new tab
#    When the user selects radio button "A problem creating a GOV.UK One Login"
#    Then the user is taken to the "A problem creating a GOV.UK One Login" page
#    When the user selects radio button "You did not get a security code"
#    Then the user is taken to the "You did not get a security code" page
#    And the user selects radio button "Text message to a phone number from another country"
#    #Test stopped here so as not to submit case to the actual support desk
#
#
#  Scenario: User with an international phone number reports that their security code did not work
#    When the user comes from the stub relying party with default options
#    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
#    When the user clicks on the support link
#    Then the user is taken to the Contact us page in a new tab
#    When the user selects radio button "A problem signing in to your GOV.UK One Login"
#    Then the user is taken to the "A problem signing in to your GOV.UK One Login" page
#    When the user selects radio button "The security code did not work"
#    Then the user is taken to the "The security code does not work" page
#    And the user selects radio button "Text message to a phone number from another country"
#    #Test stopped here so as not to submit case to the actual support desk

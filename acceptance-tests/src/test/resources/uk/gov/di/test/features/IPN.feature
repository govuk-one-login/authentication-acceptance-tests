@IPN
Feature: International Phone Numbers


  Scenario: User cannot register using an invalid UK mobile phone number
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the new user is taken to the enter your email page
    When user enters "IPN1_NEW_USER_EMAIL" email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user creates a password
    Then the new user is taken to the get security codes page
    When the new user chooses "Text message" to get security codes
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
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the new user is taken to the enter your email page
    When user enters "IPN2_NEW_USER_EMAIL" email address
    Then the new user is asked to check their email
    When the new user enters the six digit security code from their email
    Then the new user is taken to the create your password page
    When the new user creates a password
    Then the new user is taken to the get security codes page
    When the new user chooses "Text message" to get security codes
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


#  Scenario: User with an international phone number reports that they did not receive their security code
#    Given the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
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
#    Given the user comes from the stub relying party with options: "default"
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user clicks on the support link
#    Then the user is taken to the Contact us page in a new tab
#    When the user selects radio button "A problem signing in to your GOV.UK One Login"
#    Then the user is taken to the "A problem signing in to your GOV.UK One Login" page
#    When the user selects radio button "The security code did not work"
#    Then the user is taken to the "The security code does not work" page
#    And the user selects radio button "Text message to a phone number from another country"
#    #Test stopped here so as not to submit case to the actual support desk
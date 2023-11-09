@Existing
Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user requests phone OTP code 5 times
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "RESEND_CODE_TEST_USER_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user requests the phone otp code 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page
    When the user clicks the get a new code link
    Then the user is taken to the "You cannot get a new security code at the moment" page

  Scenario: Existing user tries to create an account with the same email address
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "You have a GOV.UK One Login" page

  Scenario: Existing user is correctly prompted to login using sms
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out

  Scenario: Existing user switches content to Welsh
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    And the user switches to "Welsh" language
    Then the user is taken to the Identity Provider Welsh Login Page
    When the user selects sign in
    Then the user is taken to the Welsh enter your email page
    When user enters "TEST_USER_2_EMAIL" email address in Welsh
    Then the user is prompted for their password in Welsh
    When the user clicks link "Yn ôl"
    Then the user is taken to the Welsh enter your email page
    When the user clicks link "Yn ôl"
    Then the user is taken to the Identity Provider Welsh Login Page
    When the user switches to "English" language
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page

  Scenario: Existing user logs in without 2FA then uplift with 2FA
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "You need to enter a security code" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out

  Scenario: Existing user logs in with only openid scope
    Given the user comes from the stub relying party with options: "scopes-email,scopes-phone"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out

  Scenario: Existing user logs in without address claim and with driving permit claim
    Given the user comes from the stub relying party with options: "claims-address,claims-driving-permit"
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out

  Scenario: Existing user logs in with HTTP POST method
    Given the user comes from the stub relying party with options: "method-post"
    Then the user is taken to the "Sample Service" page
    When the user clicks the continue button
    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user logs out

#  This feature isn't turned on for the build environment
#  Scenario: Existing user logs in with level P2 (MVP) confidence
#    Given the user comes from the stub relying party with options: "loc-P2"
#    Then the user is taken to the "Prove your identity with GOV.UK One Login" page
#    When the user clicks the continue button
#    Then the user is taken to the "Create a GOV.UK One Login or sign in" page
#    When the user selects sign in
#    Then the user is taken to the "Enter your email" page
#    When user enters "TEST_USER_2_EMAIL" email address
#    Then the user is taken to the "Enter your password" page
#    When the user enters their password
#    Then the user is taken to the "Check your phone" page
#    When the user enters the six digit security code from their phone
#    Then the user is taken to the "Tell us if you have one of the following types of photo ID" page
#    When the user selects radio button "journey"
#    And the user clicks the continue button
#    Then the user is taken to the "Are you on a computer or a tablet right now?" page
#    When the user selects radio button "select-device-choice"
#    And the user clicks the continue button
#    Then the user is taken to the "Do you have a smartphone you can use?" page
#    When the user selects radio button "smartphone-choice"
#    And the user clicks the continue button
#    Then the user is taken to the "Do you have a valid passport?" page
#    When the user selects radio button "select-option"
#    And the user clicks the continue button
#    Then the user is taken to the "Does your passport have this symbol on the cover?" page
#    When the user selects radio button "select-option"
#    And the user clicks the continue button
#    Then the user is taken to the "Which iPhone model do you have?" page
#    When the user selects radio button "select-option"
#    And the user clicks the continue button
#    Then the user is taken to the "Use your passport and a GOV.UK app to confirm your identity" page
#    When the user clicks the continue button
#    Then the user is taken to the "Does your smartphone have a working camera?" page
#    When the user selects radio button "working-camera-choice"
#    And the user clicks the continue button
#    Then the user is taken to the "The app uses flashing colours. Do you want to continue?" page
#    When the user selects radio button "flashing-colours-choice"
#    And the user clicks the continue button
#    Then the user is taken to the "Scan the QR code to continue confirming your identity on your phone" page

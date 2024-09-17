@Existing @build @staging @build-sp @staging-sp
Feature: Login Journey
  Existing user walks through a login journey

  Scenario: Existing user tries to create an account with the same email address
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When user enters "TEST_USER_2_EMAIL" email address
    Then the user is taken to the "You have a GOV.UK One Login" page

  Scenario: Existing user is correctly prompted to login using sms
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_4_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user clicks logout

  Scenario: Existing user switches content to Welsh
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    And the user switches to "Welsh" language
    Then the user is taken to the Identity Provider Welsh Login Page
    When the user selects sign in
    Then the user is taken to the Welsh enter your email page
    When user enters "TEST_USER_5_EMAIL" email address in Welsh
    Then the user is prompted for their password in Welsh
    When the user clicks link "Yn ôl"
    Then the user is taken to the Welsh enter your email page
    When the user clicks link "Yn ôl"
    Then the user is taken to the Identity Provider Welsh Login Page
    When the user switches to "English" language
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page

  Scenario: Existing user logs in without 2FA then uplift with 2FA
    Given the user comes from the stub relying party with options: "2fa-off"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TEST_USER_6_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is returned to the service
    When the user uplifts having already logged in
    Then the user is taken to the "Enter a security code to continue" page
    When the user enters the six digit security code from their phone
    Then the user is returned to the service
    And the user clicks logout
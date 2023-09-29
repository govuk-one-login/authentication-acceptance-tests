Feature: Test
  New user test

#AUTH APP Scenario starts from here
  @Auth1faTo2fa
  Scenario: User with Auth app as their 2FA method goes from 1FA RP to 2FA RP and they’ve signed in to their One Login since resetting their password
    Given the user comes from the stub relying party with options: "2fa-off"
    When user selects the sign in button
    And user enters "TEST_USER_AUTH_APP_EMAIL" email address
    And user enters "TEST_USER_PASSWORD" password
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user comes from the stub relying party with options: "2fa-on"
#    Then the user is taken to the "Enter a security code to continue" page
#    And the users last three digits of "TEST_USER_PHONE_NUMBER" SMS number is displayed
#    When the user selects "Problems with the code?" link
#    Then the link "change how you get security codes" is available


#  @Auth1faTo2fa
  Scenario: User with auth app as their 2FA method goes from 1FA RP to 2FA RP and they’ve not signed in to their One Login since resetting their password
    Given the user comes from the stub relying party with options: "2fa-off"
    When user selects the sign in button
    And user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    And the user enters "TEST_USER_EMAIL_CODE" as the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    And the user enters valid new password and correctly retypes it
    Then the user is taken to the "Check your phone" page
    And the existing user enters "TEST_USER_PHONE_CODE" as the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user comes from the stub relying party with options: "2fa-on"
#    Then the user is taken to the "Enter a security code to continue" page
#    And the users last three digits of "TEST_USER_PHONE_NUMBER" SMS number is displayed
#    When the user selects "Problems with the code?" link
#    Then the link "change how you get security codes" is not available




#SMS Scenario starts from here
#  @Auth1faTo2fa
  Scenario: User with SMS as their 2FA method goes from 1FA RP to 2FA RP and they’ve signed in to their One Login since resetting their password
    Given the user comes from the stub relying party with options: "2fa-off"
    When user selects the sign in button
    And user enters "TestSmsUser" email address
    And user enters "TestPassword" password
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user comes from the stub relying party with options: "2fa-on"
#    Then the user is taken to the "Enter a security code to continue" page
    And the users last 3 digits of "TEST_USER_PHONE_NUMBER" SMS number is displayed
#    When the user selects "Problems with the code?" link
#    Then the link "change how you get security codes" is available


#  @Auth1faTo2fa
  Scenario: User with SMS as their 2FA method goes from 1FA RP to 2FA RP and they’ve not signed in to their One Login since resetting their password
    Given the user comes from the stub relying party with options: "2fa-off"
    When user selects the sign in button
    And user enters "TEST_USER_ACCOUNT_RECOVERY_EMAIL_1" email address
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    And the user enters "TEST_USER_EMAIL_CODE" as the six digit security code from their email
    Then the user is taken to the "Reset your password" page
    And the user enters valid new password and correctly retypes it
    Then the user is taken to the "Check your phone" page
    And the existing user enters "TEST_USER_PHONE_CODE" as the six digit security code from their phone
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user comes from the stub relying party with options: "2fa-on"
#    Then the user is taken to the "Enter a security code to continue" page
    And the users last 3 digits of "TEST_USER_PHONE_NUMBER" SMS number is displayed
#    When the user selects "Problems with the code?" link
#    Then the link "change how you get security codes" is not available
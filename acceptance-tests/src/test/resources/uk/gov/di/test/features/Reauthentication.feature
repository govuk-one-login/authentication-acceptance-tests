@Reauth
Feature: Reauthentication of user

  @happy
  Scenario: Sms user successfully reauthenticates
    Given the "sms" user "TEST_USER_REAUTH_SMS_1" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When user enters the same email address "TEST_USER_REAUTH_SMS_1" for reauth as they used for login
    And the user enters the correct password
    And the user enters the six digit security code from their phone
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out

  @happy
  Scenario: Auth app user successfully reauthenticates
    Given the "auth app" user "TEST_USER_REAUTH_AUTH_APP_1" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When user enters the same email address "TEST_USER_REAUTH_AUTH_APP_1" for reauth as they used for login
    And the user enters the correct password
    And the user enters the security code from the auth app
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out

  @reauth-different-email
  Scenario: Sms user enters different email address during reauthentication than the one used for original login
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters a different email address for reauth than they logged in with
    Then the "Enter the same email address you used to sign in" error message is displayed
    When the user enters a different email address for reauth a further 5 times
    Then the user is taken to the "You entered the wrong sign-in details too many times" page

  @reauth-incorrect-pw
  Scenario: Sms user enters incorrect password during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_3" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_3" for reauth as they used for login
    When the user enters an incorrect password 6 times
    Then the user is taken to the "You entered the wrong password too many times" page

  @reauth-pw-reset
  Scenario: User can change their password during reauthenticates
    Given the "sms" user "TEST_USER_REAUTH_SMS_6" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_6" for reauth as they used for login
    And the user is taken to the "Enter your password" page
    When the sms user changes their password during reauthentication
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out

  @reauth-incorrect-sms-code
  Scenario: Sms user enters incorrect phone code during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_4" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_4" for reauth as they used for login
    And the user enters the correct password
    When the user enters an incorrect phone security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  @reauth-request-too-many-sms-codes
  Scenario: Sms user requests phone code resend during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_5" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_5" for reauth as they used for login
    And the user enters the correct password
    When the user requests the phone otp code a further 5 times
    Then the user is taken to the "You asked to resend the security code too many times" page

  @reauth-incorrect-auth-app-code
  Scenario: Auth app user enters incorrect auth app code during reauthentication
    Given the "auth app" user "TEST_USER_REAUTH_AUTH_APP_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_AUTH_APP_2" for reauth as they used for login
    And the user enters the correct password
    When the user enters an incorrect auth app security code 6 times
    Then the user is taken to the "You entered the wrong security code too many times" page

  @reauth-change-security-code-method
  Scenario: Sms user can change how they get security codes during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_7" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_7" for reauth as they used for login
    And the user enters the correct password
    And the user is taken to the "Check your phone" page
    When the sms user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out

  @reauth-change-security-code-method
  Scenario: Auth app user can change how they get security codes during reauthentication
    Given the "auth app" user "TEST_USER_REAUTH_AUTH_APP_3" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_AUTH_APP_3" for reauth as they used for login
    And the user enters the correct password
    And the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the auth app user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out
@Reauth @build @staging
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

  @reauth-same-incorrect-emails @AUT-2790
  Scenario: Sms user enters the same incorrect email address (known to One Login) 6 times during reauthentication and gets logged out. Owner of incorrect email is not locked out.
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters an incorrect email address at reauth that is known to One Login
    Then the "Enter the same email address you used to sign in" error message is displayed
    When the user enters the same incorrect email address for reauth a further 5 times
    Then the user is forcibly logged out
    And the owner of the incorrect email address is not blocked from signing in
    And the owner of the incorrect email address is not blocked from reauthenticating

  @reauth-different-incorrect-emails @AUT-2790
  Scenario: Sms user enters 6 different incorrect email addresses (not known to One Login) during reauthentication and gets logged out. Original user is not locked out.
    Given the "sms" user "TEST_USER_REAUTH_SMS_8" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters 6 different email addresses at reauth that are not known to One Login
    Then the user is forcibly logged out
    And the "sms" user "TEST_USER_REAUTH_SMS_8" is not blocked from signing back in
    And the "sms" user "TEST_USER_REAUTH_SMS_8" is not blocked from reauthenticating

  @reauth-incorrect-pw @AUT-2789
  Scenario: Sms user enters incorrect password during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_3" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_3" for reauth as they used for login
    When the user enters a blank password
    Then the "Enter your password" error message is displayed
    When the user enters an incorrect password 6 times
    Then the user is forcibly logged out
    And the "sms" user "TEST_USER_REAUTH_SMS_3" is not blocked from signing back in
    And the "sms" user "TEST_USER_REAUTH_SMS_3" is not blocked from reauthenticating

  @reauth-incorrect-sms-code @AUT-2788
  Scenario: Sms user enters incorrect phone code during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_4" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_4" for reauth as they used for login
    And the user enters the correct password
    When the user enters an incorrect phone security code 6 times
    Then the user is forcibly logged out
    And the "sms" user "TEST_USER_REAUTH_SMS_4" is not blocked from signing back in
    And the "sms" user "TEST_USER_REAUTH_SMS_4" is not blocked from reauthenticating


  @reauth-incorrect-auth-app-code @AUT-2788
  Scenario: Auth app user enters incorrect auth app code during reauthentication
    Given the "auth app" user "TEST_USER_REAUTH_AUTH_APP_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_AUTH_APP_2" for reauth as they used for login
    And the user enters the correct password
    When the user enters an incorrect auth app security code 6 times
    Then the user is forcibly logged out
    And the "auth app" user "TEST_USER_REAUTH_AUTH_APP_2" is not blocked from signing back in
    And the "auth app" user "TEST_USER_REAUTH_AUTH_APP_2" is not blocked from reauthenticating


  @reauth-request-too-many-sms-codes @AUT-3089
  Scenario: Sms user requests phone code resend during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_5" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_5" for reauth as they used for login
    And the user enters the correct password
    When the user requests the phone otp code a further 5 times during reauth
    Then the user is forcibly logged out
    And the "sms" user "TEST_USER_REAUTH_SMS_5" is not blocked from signing back in
    And the "sms" user "TEST_USER_REAUTH_SMS_5" is not blocked from reauthenticating


  #Silent log in support for multiple RPs re-authenticating (This is only relevant for multiple tabs)
  @reauth-multiple-rp-tab @AUT-3529 @AUT-3530
  Scenario: Sms user verify that silent login does not reset the counts of failed credential entry for the re-authentication journey [invalid email]
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters the same incorrect email address for reauth a further 5 times
    And user opens up new tab in the same browser and performs a silent log in and navigate back to the first tab
    When the user enters the same incorrect email address for reauth a further 1 times
    Then the user is taken to the "Example - GOV.UK - User Info" page


  @reauth-multiple-rp-tab @AUT-3529 @AUT-3530
  Scenario: Sms user verify that silent login does not reset the counts of failed credential entry for the re-authentication journey [invalid password]
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    When the user enters an incorrect password 5 times
    And user opens up new tab in the same browser and performs a silent log in and navigate back to the first tab
    When the user enters an incorrect password 1 times
    Then the user is taken to the "Example - GOV.UK - User Info" page


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when re-authenticating in tab B and then logs out in a tab A they cannot complete the re-authenticate in tab B when they enter a valid email
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    When user opens up new tab and performs a silent log in
    And the RP requires the user to reauthenticate
    And navigate back to the first tab
    And the user logs out
    And navigate back to the second tab
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    Then the user is taken to the "There’s a problem with this service" page


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when re-authenticating in tab B and logs out in a tab A they cannot complete the re-authenticate in tab B when they enter a valid password
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    When user opens up new tab and performs a silent log in
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    And navigate back to the first tab
    And the user logs out
    And navigate back to the second tab
    When the user enters their password
    Then the user is taken to the "There’s a problem with this service" page


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when user starts multiple re-authentications and then completes each one
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    When the RP requires the user to reauthenticate
    And user opens up new tab and performs a silent log in
    And the RP requires the user to reauthenticate
    And navigate back to the first tab
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    And the user enters their password
    And the user enters the six digit security code from their phone
    And navigate back to the second tab
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    Then the user is taken to the "Example - GOV.UK - User Info" page



  @AUT-3613 @AUT-3530
  Scenario: Sms user enters 6 different incorrect email addresses
    Given the "sms" user "TEST_USER_REAUTH_SMS_2" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters 6 different email addresses at reauth that are not known to One Login
    Then the user is forcibly logged out
    When user attempts to restart their reauthentication again
    And user enters "TEST_USER_REAUTH_SMS_2" email address
    Then the user is forcibly logged out
    When the "sms" user "TEST_USER_REAUTH_SMS_2" is signed in to their One Login account
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_2" for reauth as they used for login
    And the user enters the correct password
    And the user enters the six digit security code from their phone
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  @AUT-3613 @AUT-3530
  Scenario: Sms user enters incorrect password during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_3" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_3" for reauth as they used for login
    When the user enters an incorrect password 6 times
    Then the user is forcibly logged out
    When user attempts to restart their reauthentication again
    And user enters "TEST_USER_REAUTH_SMS_3" email address
    Then the user is forcibly logged out
    When the "sms" user "TEST_USER_REAUTH_SMS_3" is signed in to their One Login account
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_3" for reauth as they used for login
    And the user enters the correct password
    And the user enters the six digit security code from their phone
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-change-security-code-method-to-auth-app
  Scenario: Sms user can change how they get security codes during reauthentication
    Given the "sms" user "TEST_USER_REAUTH_SMS_7" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_7" for reauth as they used for login
    And the user enters the correct password
    And the user is taken to the "Check your phone" page
    When the sms user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-change-security-code-method-to-sms
  Scenario: Auth app user can change how they get security codes during reauthentication
    Given the "auth app" user "TEST_USER_REAUTH_AUTH_APP_3" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_AUTH_APP_3" for reauth as they used for login
    And the user enters the correct password
    And the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the auth app user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-pw-reset
  Scenario: User can change their password during reauthenticates
    Given the "sms" user "TEST_USER_REAUTH_SMS_6" is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And user enters the same email address "TEST_USER_REAUTH_SMS_6" for reauth as they used for login
    And the user is taken to the "Enter your password" page
    When the sms user changes their password during reauthentication
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out
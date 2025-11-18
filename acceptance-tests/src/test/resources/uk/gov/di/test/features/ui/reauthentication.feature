@UI @Reauth @build @staging
Feature: Reauthentication of user
  @happy
  Scenario: Sms user successfully reauthenticates
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    When the user enters the same email address for reauth as they used for login
    And the user enters the correct password
    And the user enters the six digit security code from their phone
    Then the user is successfully reauthenticated and returned to the service

  @happy
  Scenario: Auth app user successfully reauthenticates
    Given a user with App MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    When the user enters the same email address for reauth as they used for login
    And the user enters the correct password
    And the user enters the security code from the auth app
    Then the user is successfully reauthenticated and returned to the service

  @reauth-same-incorrect-emails @AUT-2790
  Scenario: Sms user enters the same incorrect email address (known to One Login) 6 times during reauthentication and gets logged out. Owner of incorrect email is not locked out.
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    And Another User with SMS exists

    When the logged in user enters the other users email address
    Then the "Enter the same email address you used to sign in" error message is displayed

    When the logged-in User enters the other Users email address for reauth a further 5 times
    Then the logged-in User is forcibly logged out

    When the other User attempts to use OneLogin
    Then the user is not blocked from signing in
    And the user is not blocked from reauthenticating

  @reauth-different-incorrect-emails @AUT-2790
  Scenario: Sms user enters 6 different incorrect email addresses (not known to One Login) during reauthentication and gets logged out. Original user is not locked out.
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    When the user enters 6 different email addresses at reauth that are not known to One Login
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating

  @reauth-incorrect-pw @AUT-2789
  Scenario: Sms user enters incorrect password during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    When the user enters a blank password
    Then the "Enter your password" error message is displayed
    When the user enters an incorrect password 6 times
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating

  @reauth-incorrect-sms-code @AUT-2788
  Scenario: Sms user enters incorrect phone code during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    When the user enters an incorrect phone security code 6 times
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating


  @reauth-incorrect-auth-app-code @AUT-2788
  Scenario: Auth app user enters incorrect auth app code during reauthentication
    Given a user with App MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    When the user enters an incorrect auth app security code 6 times
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating


  @reauth-request-too-many-sms-codes @AUT-3089
  Scenario: Sms user requests phone code resend during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    When the user requests the phone otp code a further 5 times during reauth
    Then the user is forcibly logged out
    * the user is not blocked from signing in
    * the user is not blocked from reauthenticating


  #Silent log in support for multiple RPs re-authenticating (This is only relevant for multiple tabs)
#  @reauth-multiple-rp-tab @AUT-3529 @AUT-3530
#  Scenario: Sms user verify that silent login does not reset the counts of failed credential entry for the re-authentication journey [invalid email]
#    Given a user with SMS MFA exists
#    And the user is already signed in to their One Login account
#
#    Given the RP requires the user to reauthenticate
#    When the user enters an incorrect email address for reauth 5 times
#    And the user opens up new tab in the same browser, performs a silent log in and switches back to the first tab
#    When the user enters an incorrect email address for reauth 1 times
#    Then the user is forcibly logged out


  @reauth-multiple-rp-tab @AUT-3529 @AUT-3530
  Scenario: Sms user verify that silent login does not reset the counts of failed credential entry for the re-authentication journey [invalid password]
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    When the user enters an incorrect password 5 times
    And the user opens up new tab in the same browser, performs a silent log in and switches back to the first tab
    When the user enters an incorrect password 1 times
    Then the user is returned to the service


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when re-authenticating in tab B and then logs out in a tab A they cannot complete the re-authenticate in tab B when they enter a valid email
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account
    When the user opens a new tab and performs a silent login
    And the RP requires the user to reauthenticate
    And the user switches back to the first tab
    And the user logs out
    And the user switches back to the second tab
    And the user enters their email address for reauth
    Then the user is taken to the "Sorry, the page has expired" page


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when re-authenticating in tab B and logs out in a tab A they cannot complete the re-authenticate in tab B when they enter a valid password
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account
    When the user opens a new tab and performs a silent login
    And the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user switches back to the first tab
    And the user logs out
    And the user switches back to the second tab
    And the user enters the correct password
    Then the user is taken to the "Sorry, the page has expired" page


  @reauth-multiple-rp-tab @AUT-3530
  Scenario: Sms user verify that when user starts multiple re-authentications and then completes each one
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user opens a new tab and performs a silent login
    And the RP requires the user to reauthenticate
    And the user switches back to the first tab
    And the user enters their email address for reauth
    And the user enters their password
    And the user enters the six digit security code from their phone
    And the user switches back to the second tab
    And the user enters their email address for reauth
    Then the user is returned to the service


  @AUT-3613 @AUT-3530
  Scenario: Sms user enters 6 different incorrect email addresses
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account
    And the RP requires the user to reauthenticate
    When the user enters 6 different email addresses at reauth that are not known to One Login
    Then the user is forcibly logged out
    When the user attempts to restart their reauthentication again
    And the user enters their email address for reauth
    Then the user is forcibly logged out

    Given the user is signed in to their One Login account
    * the user is not blocked from reauthenticating


  @AUT-3613 @AUT-3530
  Scenario: Sms user enters incorrect password during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    When the user enters an incorrect password 6 times
    Then the user is forcibly logged out

    When the user attempts to restart their reauthentication again
    And the user enters their email address for reauth
    Then the user is forcibly logged out

    Given the user is signed in to their One Login account
    * the user is not blocked from reauthenticating


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-change-security-code-method-to-auth-app
  Scenario: Sms user can change how they get security codes during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    And the user is taken to the "Check your phone" page
    When the sms user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-change-security-code-method-to-sms
  Scenario: Auth app user can change how they get security codes during reauthentication
    Given a user with App MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    And the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
    When the auth app user changes how they get security codes
    Then the user is successfully reauthenticated and returned to the service
    And the user logs out


  # WILL BE REPLACED WITH NEW PROCESS IN V3
  @reauth-pw-reset
  Scenario: User can change their password during reauthenticates
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account

    Given the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user is taken to the "Enter your password" page
    When the sms user changes their password during reauthentication
    Then the user is successfully reauthenticated and returned to the service
    And the user clicks logout

@UI @build @staging @build-sp @staging-sp @dev
Feature: International phone numbers

  @RejectInternationalNumbers
  Rule: Reject international phone numbers

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

  @InternationalNumbersForcedMFAReset
  Rule: Forced MFA reset during 2FA sign in

    Scenario: User with international SMS MFA successfully completes forced MFA reset during sign in
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      And the user enters the six digit security code from their phone
      Then the user is taken to the "You need to change how you get security codes" page
      When the user clicks the continue button
      Then the user is taken to the "How do you want to get security codes" page
      When the user chooses text message to get security codes
      Then the user is taken to the "Enter your mobile phone number" page
      When the user enters their mobile phone number
      Then the user is taken to the "Check your phone" page
      When the user enters the six digit security code from their phone
      Then the user is taken to the "You’ve changed how you get security codes" page
      When the user clicks the continue button
      Then the user is returned to the service

  @InternationalNumbersForcedMFAReset
  Rule: Forced MFA reset during uplift

    Scenario: User with international SMS MFA completes forced MFA reset during uplift after 1FA sign in
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with option 2fa-off and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is returned to the service
      When the user comes from the stub relying party with options: [2fa-on,authenticated-2] and is taken to the "Enter a security code to continue" page
      When the user enters the six digit security code from their phone
      Then the user is taken to the "You need to change how you get security codes" page
      When the user clicks the continue button
      Then the user is taken to the "How do you want to get security codes" page
      When the user chooses text message to get security codes
      And the user enters their mobile phone number
      And the user enters the six digit security code from their phone
      Then the user is taken to the "You’ve changed how you get security codes" page
      When the user clicks the continue button
      Then the user is returned to the service

  @InternationalNumbersForcedMFAReset
  Rule: Forced MFA reset during password reset

    Scenario: User with international SMS MFA completes forced MFA reset during password reset
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      And the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user clicks the forgotten password link
      Then the user is taken to the "Check your email" page
      When the user enters the six digit security code from their email
      Then the user is taken to the "Check your phone" page
      When the user enters the six digit security code from their phone
      Then the user is taken to the "Reset your password" page
      When the user enters valid new password and correctly retypes it
      Then the user is taken to the "You need to change how you get security codes" page
      When the user clicks the continue button
      Then the user is taken to the "How do you want to get security codes" page
      When the user chooses text message to get security codes
      And the user enters their mobile phone number
      And the user enters the six digit security code from their phone
      Then the user is taken to the "You’ve changed how you get security codes" page
      When the user clicks the continue button
      Then the user is returned to the service

  @InternationalNumbersForcedMFAReset
  Rule: Forced MFA reset during reauthentication

  # Reauth scenarios must start with UK SMS MFA for initial sign-in to succeed,
  # then switch to international number to trigger forced MFA reset during reauth
  Scenario: User with international SMS MFA completes forced MFA reset during reauthentication
    Given a user with SMS MFA exists
    And the user is already signed in to their One Login account
    And the users phone number is changed to an international number
    And the international phone number send limit is reset for the users phone number
    When the RP requires the user to reauthenticate
    And the user enters their email address for reauth
    And the user enters the correct password
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You need to change how you get security codes" page
    When the user clicks the continue button
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is successfully reauthenticated and returned to the service

  @InternationalNumbersIndefiniteLockout
  Rule: Indefinite lockout during 2FA sign in

    @under-development
    Scenario: User with international SMS MFA is indefinitely locked out after requesting code too many times
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is taken to the "Check your phone" page
      When the user requests the phone otp code to the international numbers limit
      Then the user is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page
      When the user clicks the continue button
      Then the user is taken to the "How do you want to get security codes" page
      When the user chooses text message to get security codes
      And the user enters their mobile phone number
      And the user enters the six digit security code from their phone
      Then the user is taken to the "You’ve changed how you get security codes" page
      When the user clicks the continue button
      Then the user is returned to the service

    Scenario: User with international SMS MFA is indefinitely locked out when send limit already set
      Given a user with unique international SMS MFA exists
      And the users phone number has reached the international phone number send limit
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page

  @InternationalNumbersIndefiniteLockout
  Rule: Indefinite lockout during uplift

    @under-development
    Scenario: User with international SMS MFA is indefinitely locked out after requesting code too many times
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with option 2fa-off and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is returned to the service
      When the user comes from the stub relying party with options: [2fa-on,authenticated-2] and is taken to the "Enter a security code to continue" page
      When the user requests the phone otp code to the international numbers limit
      Then the user is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page

    Scenario: User with international SMS MFA is indefinitely locked out when send limit already set
      Given a user with unique international SMS MFA exists
      And the users phone number has reached the international phone number send limit
      When the user comes from the stub relying party with option 2fa-off and is taken to the "Create your GOV.UK One Login or sign in" page
      When the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user enters their password
      Then the user is returned to the service
      When the user comes from the stub relying party with options: [2fa-on,authenticated-2] and is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page

  @InternationalNumbersIndefiniteLockout
  Rule: Indefinite lockout during password reset

    @under-development
    Scenario: User with international SMS MFA is indefinitely locked out after requesting code too many times
      Given a user with unique international SMS MFA exists
      And the international phone number send limit is reset for the users phone number
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      And the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user clicks the forgotten password link
      Then the user is taken to the "Check your email" page
      When the user enters the six digit security code from their email
      Then the user is taken to the "Check your phone" page
      When the user requests the phone otp code to the international numbers limit
      Then the user is taken to the "Sorry, there is a problem" page
      And the link "Check if you can change how you get security codes" is not available

    Scenario: User with international SMS MFA is indefinitely locked out when send limit already set
      Given a user with unique international SMS MFA exists
      And the users phone number has reached the international phone number send limit
      When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
      And the user selects sign in
      Then the user is taken to the "Enter your email" page
      When the user enters their email address
      Then the user is taken to the "Enter your password" page
      When the user clicks the forgotten password link
      Then the user is taken to the "Check your email" page
      When the user enters the six digit security code from their email
      Then the user is taken to the "There’s a problem with this service" page
      And the link "Check if you can change how you get security codes" is not available

  @InternationalNumbersIndefiniteLockout
  Rule: Indefinite lockout during reauthentication

    # Reauth scenarios must start with UK SMS MFA for initial sign-in to succeed,
    # then switch to international number to trigger lockout during reauth
    @under-development
    Scenario: User with international SMS MFA is indefinitely locked out after requesting code too many times during reauthentication
      Given a user with SMS MFA exists
      And the user is already signed in to their One Login account
      And the users phone number is changed to an international number
      And the international phone number send limit is reset for the users phone number
      When the RP requires the user to reauthenticate
      And the user enters their email address for reauth
      And the user enters the correct password
      When the user requests the phone otp code to the international numbers limit
      Then the user is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page

    # Reauth scenarios must start with UK SMS MFA for initial sign-in to succeed,
    # then switch to international number to trigger lockout during reauth
    Scenario: User with international SMS MFA is indefinitely locked out when send limit already set during reauthentication
      Given a user with SMS MFA exists
      And the user is already signed in to their One Login account
      And the users phone number is changed to an international number
      And the users phone number has reached the international phone number send limit
      When the RP requires the user to reauthenticate
      And the user enters their email address for reauth
      And the user enters the correct password
      Then the user is taken to the "Sorry, there’s a problem" page
      When the user selects "Check if you can change how you get security codes" link
      Then the user is taken to the IPV stub page

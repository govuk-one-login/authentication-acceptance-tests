@UI @under-development
Feature: Passkeys

  Scenario: Existing user without a passkey can log in and skip passkey creation successfully
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sign in faster with your face, fingerprint or passcode - GOV.UK One Login" page
    When the user chooses to skip passkey registration
    Then the user is returned to the service

  Scenario: Existing user without a passkey not on latest terms and conditions can log in and skip passkey creation successfully
    Given a user with SMS MFA exists
    And the user has not yet accepted the latest terms and conditions
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "Sign in faster with your face, fingerprint or passcode - GOV.UK One Login" page
    When the user chooses to skip passkey registration
    Then the user is taken to the "We’ve updated our terms of use" page
    When the user agrees to the updated terms and conditions
    Then the user is returned to the service

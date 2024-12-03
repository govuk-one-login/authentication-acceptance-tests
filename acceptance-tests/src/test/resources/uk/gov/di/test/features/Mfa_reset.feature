@mfa-reset @build
Feature: MFA reset

# ************************* SMS Section *************************
  Scenario: SMS User selects SMS OTP for MFA reset after successful identity verification
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
   Then the user is taken to the IPV stub page
   When the user clicks the continue button
    Then the user is taken to the "Choose how to get security codes" page
    And the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service



  Scenario: AUTH app User selects SMS OTP for MFA reset after successful identity verification
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    Then the user is taken to the "Choose how to get security codes" page
    And the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service




# ************************* AUTH APP Section *************************

  Scenario: Auth app User selects Authenticate app for MFA reset after successful identity verification
    Given a user with App MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "I do not have access to the authenticator app" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    And the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service



  Scenario: SMS User selects Authenticate app for MFA reset after successful identity verification
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "Problems with the code?" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When the user clicks the continue button
    And the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve created your GOV.UK One Login" page
    When the user clicks the continue button
    Then the user is returned to the service
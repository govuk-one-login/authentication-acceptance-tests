@mfa-reset

#https://govukverify.atlassian.net/browse/AUT-3825

Feature: The MFA reset process.
  Begins in Authentication, when a user initiates an MFA reset,
  they are redirected to Identity to determine their verification status. Identity verifies
  whether the user is classified as authentication-only or identity-verified.

# ************************* SMS Section *************************
  @AUT-3825
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
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

  @AUT-3825
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
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses text message to get security codes
    And the user enters their mobile phone number
    And the user enters the six digit security code from their phone
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

# ************************* AUTH APP Section *************************
  @AUT-3825
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
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service

  @AUT-3825
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
    Then the user is taken to the "How do you want to get security codes" page
    When the user chooses auth app to get security codes
    Then the user is taken to the "Set up an authenticator app" page
    When the user adds the secret key on the screen to their auth app
    And the user enters the security code from the auth app
    Then the user is taken to the "You’ve changed how you get security codes" page
    When the user clicks the continue button
    Then the user is returned to the service



# ************************* Negative tests when for unsuccessful IPV responses **********************
  Scenario Outline: Mfa User choose to reset their MFA but are unsuccessful in identity verification
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "<IPV Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "There’s a problem with this service" page
    Examples:
      | Mfa Type | Link Text                                     | IPV Response              |
      | App      | I do not have access to the authenticator app | No identity available     |
      | SMS      | Problems with the code?                       | Identity check incomplete |
      | App      | I do not have access to the authenticator app | Identity check failed     |
      | SMS      | Problems with the code?                       | Identity did not match    |

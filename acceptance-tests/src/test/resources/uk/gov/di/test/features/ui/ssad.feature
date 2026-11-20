@UI @SelfServeAccountDeletionEnabled @EnvironmentWithIpvStubDeployed

Feature: Self serve account deletion.
  Begins in the MFA reset process, when Identity reports that a user has no identity available.
  The user can delete their account in the account management component or carry on with their current method.

  Scenario Outline: Mfa User with no identity available is taken to the cannot change security codes no identity page
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "No identity available" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "You cannot change how you get security codes" page
    And the URL is present with suffix "cannot-change-security-codes-no-identity"
    Examples:
      | Mfa Type | Link Text                                     |
      | App      | I do not have access to the authenticator app |
      | SMS      | Problems with the code?                       |

  @EnvironmentWithAMCStubDeployed
  Scenario Outline: Mfa User with no identity available who cannot use their current method is taken to AMC to delete their account
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "No identity available" radio option selected
    And the user clicks the continue button
    Then the URL is present with suffix "cannot-change-security-codes-no-identity"
    When "You cannot use your current method anymore" radio option selected
    And the user clicks the continue button
    Then the user is taken to the AMC stub page account delete page
    When "Success" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "You’ve deleted your GOV.UK One Login" page
    And the URL is present with suffix "account-deleted"
    Examples:
      | Mfa Type | Link Text                                     |
      | App      | I do not have access to the authenticator app |
      | SMS      | Problems with the code?                       |

  @EnvironmentWithAMCStubDeployed
  Scenario Outline: Mfa User with no identity available who leaves AMC without deleting their account is returned to their code page
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "No identity available" radio option selected
    And the user clicks the continue button
    Then the URL is present with suffix "cannot-change-security-codes-no-identity"
    When "You cannot use your current method anymore" radio option selected
    And the user clicks the continue button
    Then the user is taken to the AMC stub page account delete page
    When "<AMC Response>" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    And the URL is present with suffix "<Suffix>"
    When the user enters the six digit code for "<Mfa Type>"
    And the user dismisses the passkey registration page if present
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | AMC Response | Page                                                            | Suffix                       |
      | App      | I do not have access to the authenticator app | Back         | Enter the 6 digit security code shown in your authenticator app | enter-authenticator-app-code |
      | App      | I do not have access to the authenticator app | Abort        | Enter the 6 digit security code shown in your authenticator app | enter-authenticator-app-code |
      | SMS      | Problems with the code?                       | Back         | Check your phone                                                | enter-code                   |
      | SMS      | Problems with the code?                       | Abort        | Check your phone                                                | enter-code                   |

  Scenario Outline: Mfa User with no identity available who wants to try their current method again is returned to the service
    Given a user with "<Mfa Type>" MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    And the user selects sign in
    And the user enters their email address
    And the user enters their password
    And the user selects "<Link Text>" link
    And the user selects "check if you can change how you get security codes" link
    Then the user is taken to the IPV stub page
    When "No identity available" radio option selected
    And the user clicks the continue button
    Then the URL is present with suffix "cannot-change-security-codes-no-identity"
    When "You want to try using your current method again" radio option selected
    And the user clicks the continue button
    Then the user is taken to the "<Page>" page
    When the user enters the six digit code for "<Mfa Type>"
    And the user dismisses the passkey registration page if present
    Then the user is returned to the service
    Examples:
      | Mfa Type | Link Text                                     | Page                                                            |
      | App      | I do not have access to the authenticator app | Enter the 6 digit security code shown in your authenticator app |
      | SMS      | Problems with the code?                       | Check your phone                                                |

@API @VPC @AccountManagementAPI
Feature: Account Management

  Scenario Outline: Authenticated User successfully changes their Phone Number
    Given a User exists
    And the User is Authenticated
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system sends an OTP to "<Mobile Number>"
    When the User provides the correct otp
    Then the User's Phone Number is updated to "<Mobile Number>"

    Examples:
      | Mobile Number |
      | 07700900111   |

    @AcceptInternationalNumbers
    Examples:
      | Mobile Number |
      | +61412123123  |

  @RejectInternationalNumbers
  # Note: Avoid known test numbers to ensure international validation is tested.
  Scenario Outline: Authenticated User cannot change their Phone Number to an international number
    Given a User exists
    And the User is Authenticated
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system rejects the request to send an OTP to "<Mobile Number>"

    Examples:
      | Mobile Number |
      | +33777777777  |

  Scenario Outline: Cannot send an otp to a known number
    Given a User exists
    And the User is Authenticated
    When the User adds "<Mobile Number>" as their SMS Backup MFA
    Then the system rejects the request to send an OTP to "<Mobile Number>"

    Examples:
      | Mobile Number |
      | 07700900000   |
      | +447700900000 |

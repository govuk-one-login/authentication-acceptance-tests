Feature: Testing

  @testTag @suspended
  Scenario: User is created
    Given a user with sms MFA exists
    And and the user has a temporarily suspended intervention

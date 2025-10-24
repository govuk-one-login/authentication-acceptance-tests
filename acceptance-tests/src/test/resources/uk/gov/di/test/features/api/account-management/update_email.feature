@under-development @API
Feature: Update Email

  Scenario: Authenticated User successfully updates their email address
    Given a User exists
    And the User is Authenticated
    Then the User provides a new email address
    And the User waits for 4 seconds
    When the User provides the correct otp for the new email address
    Then the system accepts the request

  Scenario: Authenticated User cannot update to a blocked email address
    Given a User exists
    And the User is Authenticated
    Then the User provides a new high-risk email address
    And the User waits for 4 seconds
    When the User provides the correct otp for the new email address
    Then the system rejects the request with status code 403 and error code 1089

  Scenario: Authenticated User successfully updates to a blocked email address that bypassed the risk assessment
    Given a User exists
    And the User is Authenticated
    Then the User provides a new high-risk email address that will cause an error
    And the User waits for 4 seconds
    When the User provides the correct otp for the new email address
    Then the system accepts the request

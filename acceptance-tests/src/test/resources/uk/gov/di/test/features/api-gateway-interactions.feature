Feature: API Gateway Interactions
  As a test engineer
  I want to test API Gateway endpoints directly
  So that I can verify the API functionality

  @API
  Scenario: Test update-password endpoint
    Given a user exists
    When I invoke the "h3909duiyi" API Gateway endpoint with path "/update-password" using method "POST"
    Then the API Gateway status should be 204

#  @API
#  Scenario: Test authenticate endpoint
#    Given a user exists
#    When I invoke the "h3909duiyi" API Gateway endpoint with path "/authenticate" using method "POST"
#    Then the API Gateway response should contain "statusCode"

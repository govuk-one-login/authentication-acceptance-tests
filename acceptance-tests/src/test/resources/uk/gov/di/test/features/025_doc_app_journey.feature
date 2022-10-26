@Doc
Feature: Doc app journey
  User can perform a Doc app journey

  Scenario: User completes a Doc app journey successfully
    Given the doc app services are running
    When the user visits the doc app relying party
    And the user clicks the continue button
    And the user sends a valid json payload
    Then the user is taken to the user information page
    When the user clicks the My Account link
    Then the user is taken to the sign in page
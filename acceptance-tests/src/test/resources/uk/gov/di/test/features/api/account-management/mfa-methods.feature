@under-development @API
Feature: MFA Method Management API
  Check the MFA Method Management API

  Scenario: Retrieve a logged in Users MFA Methods
    Given a user exists
    When a retrieve request is made to the API
    # Enter steps here

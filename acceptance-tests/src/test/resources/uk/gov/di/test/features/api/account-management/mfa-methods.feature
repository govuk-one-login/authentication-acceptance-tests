@under-development @API
Feature: MFA Method Management API
  Check the MFA Method Management API

  Scenario: Retrieve a logged in Users MFA Methods
    When a retrieve request is made to the API
    # Enter steps here

  Scenario: Authenticated User successfully Add Backup Phone Number as MFA
    Given a User exists
    And the User is Authenticated
    And the user has no backup MFA method
    When the User requests to add a backup MFA Phone Number "07700900112"
#    Then the User's back up MFA phoneNumber is updated to "07700900112"

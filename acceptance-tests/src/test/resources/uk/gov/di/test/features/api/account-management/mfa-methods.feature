@under-development @API
Feature: MFA Method Management API
  Check the MFA Method Management API

  Scenario: Retrieve a logged in Users MFA Methods
    When a retrieve request is made to the API
    # Enter steps here

  Scenario: Authenticated User successfully Add Backup Phone Number as MFA
    Given a Migrated User exists
    And the User is Authenticated
    And the user has no backup MFA method
    When the User requests an OTP to change their Phone Number to "07700900111"
    And the User receives the OTP code
    When the User requests to add a backup MFA Phone Number "07700900111"
    Then the User's back up MFA phoneNumber is updated to "07700900111"

  Scenario: Authenticated User successfully Add Auth App as MFA
    Given a user with SMS MFA exists
    And the User is Authenticated
    And the user has no backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated

  Scenario: Authenticated User successfully Delete Backup Phone Number as MFA
    Given a Migrated User exists
    And the User is Authenticated
    When the User requests an OTP to change their Phone Number to "07700900111"
    And the User receives the OTP code
    When the User requests to add a backup MFA Phone Number "07700900111"
    Then the User's back up MFA phoneNumber is updated to "07700900111"
    When the User requests to delete backup MFA Method
    Then the User's backup MFA Method is deleted

  Scenario: Authenticated User successfully Update Backup MFA method from Auth to SMS Phone Number
    Given a user with SMS MFA exists
    And the User is Authenticated
    And the user has no backup MFA method
    When the User requests to add a backup MFA Auth App
    Then the User's back up MFA Auth App is updated
#    When the User request to update back up MFA as phone number "07700900112"
#    Then the User's back up MFA phoneNumber is updated to "07700900112"


  Scenario: Authenticated User successfully Update Backup MFA method from SMS to Auth App
    Given a Migrated User exists
    And the User is Authenticated
    And the user has no backup MFA method
    When the User requests an OTP to change their Phone Number to "07700900111"
    And the User receives the OTP code
    When the User requests to add a backup MFA Phone Number "07700900112"
    Then the User's back up MFA phoneNumber is updated to "07700900112"
    When the User requests to update a backup MFA Auth App
    Then the User's back up MFA Auth App is updated

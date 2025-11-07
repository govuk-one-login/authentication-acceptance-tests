@AUT-4388 @AUT-4389 @UI @dev @EmailBlocks @under-development
Feature: Create account - Experian email check

  This scenario covers how Experian checks the email address submitted during One Login account creation.
  The check runs in the background while the user enters their email OTP, and based on the result,
  the system either allows a valid email to proceed to password creation or blocks a fraudulent or high-risk email
  from opening an account. These scenarios confirm that the email-blocking logic works as intended,
  while the existing Registration_journey.feature file validates the full end-to-end account creation journey.

  Scenario: User can create an account with a valid email and not blocked by experian
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When the user enters their email address
    Then the user is taken to the "Check your email" page
    And the User waits for 4 seconds
    When the user enters the six digit security code from their email
    And the user email is accepted and taken to "Create your password" page
    Then the user email is not blocked to proceed with account creation


  Scenario: User can create an account with a high-risk email and not blocked by experian when the service is down
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When User enters a high-risk email that will cause an error
    Then the user is taken to the "Check your email" page
    And the User waits for 4 seconds
    When the user enters the six digit security code from their email
    And the user email is accepted and taken to "Create your password" page
    Then the user email is not blocked to proceed with account creation


  Scenario: User get blocked while trying to create an account with high-risk email
    Given a user does not yet exist
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects create an account
    Then the user is taken to the "Enter your email address" page
    When User enters a high-risk email
    Then the user is taken to the "Check your email" page
    And the User waits for 4 seconds
    When the user enters the six digit security code from their email
    Then the user email is blocked and taken to "cannot-use-email-address" page
    And the user attempt to navigates to the previous page
    Then the user remain on the "cannot-use-email-address" page
    And the user clicks on "try another email address" link
    Then the user is taken to the "Enter your email address" page

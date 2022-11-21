Feature: Not logged in
  User launches account management

  Scenario: User launches account management
    Given the not logged in services are running
    When the not logged in user navigates to account root
    Then the not logged in user is taken to the Identity Provider Login Page

  Scenario: User redirects to Sign-in to a service from No GOV.UK Account found page
    Given the registration services are running
    And a new user has different valid credentials
    When the not logged in user navigates to account root
    Then the new user is taken to the Identity Provider Login Page
    When the new user selects sign in
    Then the new user is taken to the sign in to your account page
    When the new user enters their email address
    Then the new user is taken to the account not found page
    When the new user clicks the sign in to a service button
    Then the new user is taken to the sign in to a service page
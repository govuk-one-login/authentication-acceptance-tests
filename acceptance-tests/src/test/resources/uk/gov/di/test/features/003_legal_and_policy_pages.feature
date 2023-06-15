@LegalAndPolicy
Feature: Legal and policy pages
  User clicks on the legal and policy pages

  Scenario: User views legal and policy pages
    Given the services are running
    When the user visits the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
    When the user clicks link "Accessibility statement"
    Then the user is taken to the accessibility statement page
    When the user clicks link "Cookies"
    Then the user is taken to the GOV.UK cookies page
    When the user clicks link "Terms and conditions"
    Then the user is taken to the terms and conditions page
    When the user clicks link "Privacy notice"
    Then the user is taken to the privacy notice page

  Scenario: User accepts updated terms and conditions
    Given the existing user has outdated terms and conditions
    When the user visits the stub relying party
    And the user clicks "govuk-signin-button"
    Then the user is taken to the Identity Provider Login Page
    When the existing user selects sign in
    Then the user is taken to the "Enter your email" page
    When the existing user enters their email address
    Then the user is taken to the "Enter your password" page
    When the existing user enters their password
    Then the user is taken to the "Check your phone" page
    When the existing user enters the six digit security code from their phone
    Then the user is taken to the "terms of use update" page
    When the user does not agree to the updated terms and conditions
    Then the user is taken to the "Agree to the updated terms of use to continue" page
    When the user agrees to the updated terms and conditions
    Then the user is taken to the "Example - GOV.UK - User Info" page
    When the user clicks logout
    Then the user is taken to the "Signed out" page
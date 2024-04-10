@LegalAndPolicy
Feature: Legal and policy pages
  User clicks on the legal and policy pages

  Scenario: User views legal and policy pages
    Given the services are running
    When the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user clicks link "Accessibility statement"
    Then the user is taken to the accessibility statement page
    When the user clicks link "Cookies"
    Then the user is taken to the GOV.UK cookies page
    When the user clicks link "Terms and conditions"
    Then the user is taken to the terms and conditions page
    When the user clicks link "Privacy notice"
    Then the user is taken to the privacy notice page

  Scenario: User accepts updated terms and conditions
    Given the user comes from the stub relying party with options: "default"
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When user enters "TERMS_AND_CONDITIONS_TEST_USER_EMAIL" email address
    Then the user is taken to the "Enter your password" page
    When the user enters their password
    Then the user is taken to the "Check your phone" page
    When the user enters the six digit security code from their phone
    Then the user is taken to the "terms of use update" page
    When the user agrees to the updated terms and conditions
    Then the user is returned to the service
    And the user logs out

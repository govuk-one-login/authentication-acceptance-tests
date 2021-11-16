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

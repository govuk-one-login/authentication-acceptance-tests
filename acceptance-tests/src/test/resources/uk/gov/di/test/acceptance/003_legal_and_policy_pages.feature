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

  Scenario: User accepts terms and conditions
    Given the services are running
    And a new user has valid credentials
    And the new user clears cookies
    When the new user visits the stub relying party
    And the new user clicks "govuk-signin-button"
    Then the new user is taken to the Identity Provider Login Page
    And there are no accessibility violations
    When the new user selects sign in
    And there are no accessibility violations
    And the new user enters their t&c email address
    And there are no accessibility violations
    And the new user enters their t&c password
    And there are no accessibility violations
    When the new user enters the six digit security code from their phone
    Then the new user is taken to the updated terms and conditions page
    And there are no accessibility violations
    When the new user clicks link by href "/updated-terms-and-conditions-disagree"
    Then the new user is taken to the Agree to the updated terms of use to continue page
    When the new user clicks by name "termsAndConditionsResult"
    Then the new user is returned to the service
    When the new user clicks by name "logout"
    And there are no accessibility violations
    Then the new user is taken to the signed out page

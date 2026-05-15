@UI @PasskeysEnabled
Feature: Back Button Behaviour

  Scenario: User can navigate to reset-password-check-email and navigate back through each page
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the forgotten password link
    Then the user is taken to the "Check your email" page
    When the user clicks the Back link
    Then the user is taken to the "Enter your password" page
    When the user clicks the Back link
    Then the user is taken to the "Enter your email" page
    When the user clicks the Back link
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page

  Scenario: User can navigate to enter-password and navigate back through each page
    Given a user with SMS MFA exists
    When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    When the user selects sign in
    Then the user is taken to the "Enter your email" page
    When the user enters their email address
    Then the user is taken to the "Enter your password" page
    When the user clicks the Back link
    Then the user is taken to the "Enter your email" page
    When the user clicks the Back link
    Then the user is taken to the "Create your GOV.UK One Login or sign in" page

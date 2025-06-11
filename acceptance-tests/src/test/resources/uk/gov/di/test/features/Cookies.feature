@UI @Cookies
Feature: Cookies
  The correct cookies are set on the browser, at the correct time

  Scenario: User views the start page
    Given the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
    Then the "di-device-intelligence" cookie has been set

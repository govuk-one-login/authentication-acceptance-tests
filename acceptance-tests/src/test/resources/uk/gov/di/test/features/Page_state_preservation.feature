Feature: Page state is preserved when using back link

  Scenario: User has chosen text message as their auth method and moved on to the next page. When clicking the Back link, the user is returned to the auth method selection page and their previous choice of text message remains selected.
    Given a user has selected text message as their auth method and has moved on to the next page
    When the user clicks the Back link
    Then the user is taken to the "Choose how to get security codes" page
    And their previously chosen text message auth method remains selected

  Scenario: User has chosen auth app their auth method and moved on to the next page. When clicking the Back link, the user is returned to the auth method selection page and their previous choice of auth app remains selected.
    Given a user has selected auth app as their auth method and has moved on to the next page
    When the user clicks the Back link
    Then the user is taken to the "Choose how to get security codes" page
    And their previously chosen auth app auth method remains selected
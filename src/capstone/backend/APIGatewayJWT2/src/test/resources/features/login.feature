Feature: Login functionality

  Scenario: Successful login
    Given the user is on the login page
    When the user enters valid credentials
    And clicks the login button
    Then the user should be redirected to the appropriate dashboard

  Scenario: Unsuccessful login with invalid credentials
    Given the user is on the login page
    When the user enters invalid credentials
    And clicks the login button
    Then an error message should be displayed

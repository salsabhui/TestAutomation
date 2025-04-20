Feature: Test user registration functionality

  Scenario: Successful registration of a user
    Given I am on the registration page for basketballengland.co.uk
    When I enter the correct registration details
    Then I should become a member successfully

  Scenario: Failed registration when last name is missing
    Given I am on the registration page for basketballengland.co.uk
    When I enter the correct registration details without filling in the last name
    Then I should get an error message indicating that the last name is missing

  Scenario: Failed registration when the password does not match
    Given I am on the registration page for basketballengland.co.uk
    When I enter the correct registration details but the password does not match
    Then I should get an error message indicating that the password does not match

  Scenario: Failed registration when the terms and conditions are not accepted
    Given I am on the registration page for basketballengland.co.uk
    When I enter the correct registration details but do not accept the terms and conditions
    Then I should get an error message indicating that the terms and conditions are not accepted
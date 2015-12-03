Feature: Adding groceries to the database.
  This is a key feature of the application. There is a local database on the device that is going to add groceries
  and there is an online database where every synchronized entry is stored. When adding a food the user can
  declare specifications of the food. It is obligatory to give the name and the expire date.

  Scenario: Adding grocery to local database without a template
    Given the user is logged in at appXpired
    When he clicks on Add
    And he writes a valid name into the input field "Name"
    And he clicks on add
    Then he will see a success message
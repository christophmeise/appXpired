Feature: Display and Delete Food
  This feature describes the flow of events which is mandatory to delete food.
  We will describe two different scenarios. One is to delete a complete entry of food, the other one is to only consume a amount of the food entry.

  Scenario: Consumes food
Given the user is logged in at appXpired
When he clicks on Show
And he clicks on consume next to an entry of food from the list
#Then the food will be deleted in the local database

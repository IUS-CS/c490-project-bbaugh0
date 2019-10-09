# Project

## Motivation

My project will be a budgeting type app. There are already a lot of these out here, but this one will be tailored specific to me and what I want it to be. It will be semi similar to the app called Mint, but I always have problems with it because it classifies purchases incorrectly and doesn't have exactly what I want. [This is a link to the app.](https://www.mint.com) The target user base will be myself, or anyone else who is interested in an app that helps them budget specifically for them.

## Meeting minimum requirements

The opening screen will likely include a few different things. The top of the screen will show how much over or under budget you are, with a + or - sign indicating under or over, and will be green or red depending on which you are. Underneath of that will include all of the items in your weekly budget, such as food, gas, entertainment, etc, and these will be user customizable with a plus button on the bottom right, this will be another page. There will also be a page where you can enter how much money you spent in a category on that day, and these will be the numbers that are subtracted from the database. These will be values stored in a SQL database on the device. There will be a table with all categories you are budgeting for and how much each one amounts to, and there will be a table that tracks these values on a weekly basis based on user input. So if the user spends money, they enter how much they spent and that value gets updated with that amount subtracted by how much the user spent. The app will keep its state by using a view model. The app will use Facebook login and I am going to try to allow multiple profiles within the app, but we will see how far I get with that.

### Model descriptions

The data that will be stored will be stored using mySQL and room built into android. The data will be stored on the app itself. There will likely be two tables, one table that has all of the budget categories and each column will be a different category. The second table will be how much the user has remaining in the budget for each category. All numbers will be decimals. 

### Controller Description

The controller will only update data upon user input on the screen that allows the user to update the data. They will be able to enter a number in any field, and all fields will be nullable, in case they didn't spend anything on that category that day. When they click save at the bottom, the app will pull the values from the second table, subtract those values by the user input, and then put the updated values into the database.
<h1 align="center"> The Bloomi Development Journey 🌱 </h1><br>
<p align="center"> A summary of everyday's progress! (Dec 30 - ongoing) </p>

## Dec 30
- Started the Bloomi project with Tabs activity
- Connected the floating action button to the Add a New Plant Activity
- Generally designed the Add a Plant page (activity_add_plant.xml)
- Began to develop the Add a Plant Activity (AddPlant.java)
    - Implemented multiple button OnClick features
    - Made the "watering notifications" content only visible when the user selects the feature
    
## Dec 31
- Added the first implementations of the Date and Time pickers for the watering notifications (AddPlant.java)
- Re-did the constraints and finished Activity design (activity_add_plant.xml)
- Created README.md
- Added the feature to select a plant image from the user's gallery (AddPlant.java)
- Developed snackbars to indicate any actions the user must take when trying to add a plant, if any
    - Added "!" to the required fields that have been left empty, and made them disappear once the user starts typing in the field
    - Indicates if no Time/Date has been chosen for the watering notifications, if applicable

## Jan 1
- Added the feature to take a photo from the phone's camera to select an image for the plant (AddPlant.java)
- Displays the image chosen as the plant's avatar
- Created and finished the second implementations of the Date and Time pickers
- Fixed bugs for the display of the notification times and dates
- Finished developing the Add a Plant activity (AddPlant.java)
- Began developing the Garden tab (fragment) of the Main Activity (MainActivity.java, Garden.java, fragment_garden.xml)
- Implemented Cardviews for each plant the user adds (Garden.java,  RecyclerViewAdapter.java, garden_item.xml, fragment_garden.xml)
- Displays all Cardviews in a Recyclerview in the Garden tab

## Jan 2
- Made each Cardview clickable, and brings up the Plant's information in a new Activity (PlantCardView.java)
- UI design for the above (activity_plant_card_view.xml)
- Gave each plant a unique ID
- Implemented the back button in PlantCardView.java
- Made tweaks on UI design
- Created a custom, rounded corner Alert Dialog to enter in a new plant measurement (PlantCardView.java)
    - Added "OK" and "Cancel" buttons to the Alert Dialog
    - Allows user to choose metric again (will convert into a uniform metric to use for graphs)
    - Notifies user if field is left empty

## Jan 4
- Created the Bloomi database in MySQL
    - added a plant_data table, where all the data about each plant is stored
    - added a users table, storing the login information for each user
- Created the log in activity page with an option to sign up
    - email must exist
    - password must match password in database
- Developed the sign up page
    - the two passwords must match
    - email must not be already used
    
## Jan 5
- Linked the users table in the Bloomi database to LoginActivity.java by developing BackgroundWorker.java
- Edited the UI of activity_login.xml and activity_sign_up.xml
- Added email and password verification with the data in the bloomi database
    - let me pat myself on the back for this one... I had a simple error that went completely unnoticed by both
    the system and myself :') spent 2 hours debugging only to realize I had to move 2 lines of code :')
    - Had to re-get the user's input into the username and password EditTexts once the log in button was clicked!! Lesson well learned...
    
## TODO:
- set up notifications based on the times indicated by the user
- link the login page to the main activity 
    - only display login page the first time the user uses the app 
- for sign up, add the users data to the db
- make edit plant activity that's connected to db
- connect activity_add_plant to db
- make dashboard based on db
- make logo
- make settings menu activity 
    - logout
    - notifications
    - bug reports
    - contact
    - about
    - permissions
- research and apply UI design principles 
- testing app 
    - database synchronization
    - deleting
    - editing
    - adding
    - logout
    - login
    - signup
    - notifications
<h1 align="center"> The Bloomi Development Journey 🌱 </h1><br>
<p align="center"> A summary of everyday's progress! (Dec 30 - ongoing) </p>

### Dec 30
- Started the Bloomi project with Tabs activity
- Connected the floating action button to the Add a New Plant Activity
- Generally designed the Add a Plant page (activity_add_plant.xml)
- Began to develop the Add a Plant Activity (AddPlant.java)
    - Implemented multiple button OnClick features
    - Made the "watering notifications" content only visible when the user selects the feature
    
### Dec 31
- Added the first implementations of the Date and Time pickers for the watering notifications (AddPlant.java)
- Re-did the constraints and finished Activity design (activity_add_plant.xml)
- Created README.md
- Added the feature to select a plant image from the user's gallery (AddPlant.java)
- Developed snackbars to indicate any actions the user must take when trying to add a plant, if any
    - Added "!" to the required fields that have been left empty, and made them disappear once the user starts typing in the field
    - Indicates if no Time/Date has been chosen for the watering notifications, if applicable

### Jan 1
- Added the feature to take a photo from the phone's camera to select an image for the plant (AddPlant.java)
- Displays the image chosen as the plant's avatar
- Created and finished the second implementations of the Date and Time pickers
- Fixed bugs for the display of the notification times and dates
- Finished developing the Add a Plant activity (AddPlant.java)
- Began developing the Garden tab (fragment) of the Main Activity (MainActivity.java, Garden.java, fragment_garden.xml)
- Implemented Cardviews for each plant the user adds (Garden.java,  RecyclerViewAdapter.java, garden_item.xml, fragment_garden.xml)
- Displays all Cardviews in a Recyclerview in the Garden tab

### Jan 2
- Made each Cardview clickable, and brings up the Plant's information in a new Activity (PlantCardView.java)
- UI design for the above (activity_plant_card_view.xml)
- Gave each plant a unique ID
- Implemented the back button in PlantCardView.java
- Made tweaks on UI design
- Created a custom, rounded corner Alert Dialog to enter in a new plant measurement (PlantCardView.java)
    - Added "OK" and "Cancel" buttons to the Alert Dialog
    - Allows user to choose metric again (will convert into a uniform metric to use for graphs)
    - Notifies user if field is left empty

### Jan 4
- Created the Bloomi database in MySQL
    - added a plant_data table, where all the data about each plant is stored
    - added a users table, storing the login information for each user
- Created the log in activity page with an option to sign up
    - email must exist
    - password must match password in database
- Developed the sign up page
    - the two passwords must match
    - email must not be already used
    
### Jan 5
- Linked the users table in the Bloomi database to LoginActivity.java by developing BackgroundWorker.java
- Edited the UI of activity_login.xml and activity_sign_up.xml
- Added email and password verification with the data in the bloomi database
    - let me pat myself on the back for this one... I had a simple error that went completely unnoticed by both
    the system and myself :') spent 2 hours debugging only to realize I had to move 2 lines of code :')
    - Had to re-get the user's input into the username and password EditTexts once the log in button was clicked!! Lesson well learned...
- Switch from MySQL to Firebase! 
- Implemented Firebase Authorizations with the Signup and Login activities
    - Signup:
        - email must not be already used
        - email must be correctly formatted
        - passwords must match
        - password must be >6 characters in length
    - Login:
        - credentials must be correct 
- if sign up authorizations fail, app alerts user where the error was, and the alert disappears
when the field is correctly formatted again 
- Once user adds a plant, all its data is entered into the Firebase storage as a new document
    - plant image is compressed from a Bitmap to a JPG
    - plant image is uploaded to the Firebase Storage 
    - image path in storage is added with the plant data
- Bug after bug after bug today :'')))
    - IT WORKED!!
    - everything actually works!!!!!
- On successful Add Plant, snackbar is displayed for 1.5s before going back to Garden
- User is now kept logged in once they log in once

### Jan 6
- Added a Splash Screen for the app 
    - Activated on launch
    - Determines if the user needs to log in or if the user is already signed in
    - Moves user to appropriate page
- Created settings menu
    - user will be able to change email, password, and log out 
    - user will be able to toggle notifications
    - user will be able to send feedback / bug reports
    - user will be able to learn more about the app
- Inflated the menu option to be ON THE SAME actionbar as the app title!!
- In the settings menu:
    - Displays current user's email under Change Email.
        - Updates when user changes email!
    - developed the change email dialog
        - displays appropriate error messages to the user 
    - Implemented change password feature 
        - tests for correct old password 
        - new passwords must not be weak and must match
    - log out 
        - brings user back to login 
- redesigned the displaying of error messages to user in
    - login activity
    - sign up activity 
- removed app action bars in login and signup 
- user can now effectively log in, change email, change password, and log out.
- added cancel button in sign up

### Jan 7
- changed database structure
    - users -> [actual user] -> [all of the user's plants so it's kept in one collection] -> [plant document for each plant]
    - [actual user] document created on user sign up
    - plants subcollection created on user signup, initialized with an initial plant document (empty)
- added forgot password button in login 
    - sends reset password email 
    - displays error if email cannot be sent 
    - displays verification if email has been sent 
- when changing password, new pass cannot be old pass.
- Garden:
    - for every plant in user's collection in db, add plant document as Map<String, Object> into an ArrayList
    - Inflated every cardview in Garden with an item from above ArrayList
    - I was so scared this wasn't ever going to work lol!
    - after adding a new plant, list of plants refresh
    - loads plant image onto the small cardview from database
- When any plant is clicked in garden, all of the plant's data from database is passed into the PlantCardView Activity 
    - image, nickname, type, other notes 
- Measurements:
    - when new plant is added with measurements, the plant document creates a new map: <[DATE ADDED], [MEASUREMENT] to make generating the graph more accurate
    - When a new measurement is added in AddMeasurementDialog:
        - PlantCardView's "last measurement" text is updated
        - New measurement is added to database
- changed the app's core colors
    
## TODO:
- figure out metric stuff
- get latest measurement from date and time 
- make is so that the nicknames are unique
- set up notifications based on the times indicated by the user
- add plant
    - remove image option
    - change the floating action button to a cardview in garden with a plus sign in the middle?
- redesign Add Plant activity to look like Plant Card View 
- make edit plant activity that's connected to db
    - delete plant 
- make dashboard based on db
- make logo
- research and apply UI design principles 
- testing app 
    - database synchronization (seems all good rn)
    - deleting
    - editing
    - adding
    - logout (seems all good rn)
    - login (seems all good rn)
    - signup (seems all good rn)
    - notifications
- review constraints so that it looks the ~same on all devices
- When app is finished, remove <intent-filter> tag from all activities in Manifest.xml EXCEPT FOR splash activity
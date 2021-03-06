<h1 align="center"> The Bloomi Development Journey 🌱 </h1><br>
<p align="center"> A summary of everyday's progress! (Dec 30 - Jan 8) </p>
<hr>
I knew Bloomi would be my biggest project yet, but it became so much deeper than I ever could've thought!
However, reflecting on the abundance of newfound knowledge, I wouldn't have had it any other way. <br>
I'm sharing with you below my entire journey in developing Bloomi, broken down by the day, and a quick summary of what I've learned in my first time in Android dev. <br>
Let's code!

## :rocket: I learned: 
- Object-Oriented Programming:
    - The Java programming language and applying the principles of OOP.
    - Various code exceptions and errors + how to fix/avoid them.
- Android SDK:
    - Navigating and connecting activities, and passing data throughout the app.
    - Developing a .xml file and tailoring it to different screen sizes.
    - Layout constraints, relative positioning, etc.
- Database:
    - MySQL, PHP, and the way data is stored/retrieved.
    - Cloud Firestore and implementing Firebase API methods.
    - Extracting, sorting and analyzing data.
    - Updating and displaying information in realtime.
- Skills:
    - Analyzing every detail and edge case.
    - UI/UX fundamentals in mobile development.
    - Motivation and planning ahead.
    - When to sacrifice a smaller feature to implement a larger one.
    - Strategic problem resolution, and how to (sneakily) work around "unfixable" bugs.

## :books: Development Log:
    
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

### Jan 8
- Implemented LinkedMap from Apache's Common Collections to store all plant measurements in order.
- Scraped LinkedMap logic since Firebase does not support
- Used Database References to sort all measurements by date!!
    - Most recent comes last 
    - "Latest measurement" on Plant Card View corresponds to this recent measurement 
    - Began to populate graph with growth data. Buggy 
    - No longer buggy!
- Graph:
    - X-Axis is date measurement was added 
    - Y-Axis is height in metric the user chooses
    - Graph updates when activity refreshes
    - newest measurement is the rightmost data 
        - X-Axis is sorted from oldest->most recent 
- Redesigned Add Plant activity to look like Plant Card View 
- Each plant has one type of metric to keep graph more accurate
- Plant specific metric is displayed in Add Measurement dialog 

## Complete Collection of Everything I Learned:
- Java
    - Classes
        - Constructors, getters, setters, contexts
    - Methods
        - Public, private, static, return types
        - Overriding
    - Exceptions
        - try catch, debugging strategies
    - Converting from one type to another
        - Parsing
    - Printing to and reading log
    - Interfaces
        - Implementations
    - Abstract methods
    - Applying API's / libraries
        - Imports
    - Converting from bitmap to files
        - Input and Output streams
    - Intents
    - ArrayLists, Maps
- Android
    - Manifest file, XML code
    - Gradle builds
        - Libraries, third-party API's
    - Third party API's and libraries
    - Constraint, relative, linear layouts
        - Margin, padding, visibility, style, id
        - CalendarViews
        - CardViews
        - RecyclerViews
        - TextViews, EditTexts
        - Buttons
        - Spinners
        - ImageViews, ImageButtons
        - ScrollViews
        - Fragments, TabViews
    - Date and time formatting
    - Toasts, Snackbars
    - Notification builders, channels
    - Date and Time picker activities
    - Dialogs
        - custom dialogs
        - exchanging data between activity and dialog
    - App themes and styles
    - Starting activities through intents
    - Menu (settings)
    - Splashscreens, launcher activities
- Database
    - Uploading and retrieving files to storage
    - User authentication
        - sign up
        - log in
        - password reset
        - email change
        - user id
    - Creating, updating, reading documents
    - Getting data by key
    - Refreshing data

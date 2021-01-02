<br><h1 align="center"> The Bloomi Development Journey 🌱 </h1>
<p align="center"> A summary of everyday's progress! (Dec 30 - ongoing) </p>

## Dec 30
- Started project with Tabs activity
- Created a floating action button to add a new plant
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
    - Updates the "Last Measurement:" TextView accordingly


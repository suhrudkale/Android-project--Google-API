**Overview**

CrowdSense is an innovative Android application developed in Kotlin, designed to aid shoppers in gauging the crowd density at shopping malls in real-time. Utilizing Firebase for data storage and retrieval, users can report or view the current crowd status of specific malls, enhancing their shopping experience by avoiding overcrowded places.

**Features**

Real-Time Crowd Reporting: Users can report the current crowd density at a mall, which is then stored in a Firebase database.
Crowd Density Viewing: Allows users to view the current crowd density of different malls.
Location-Based Services: The app uses Google Maps and location services to identify the nearest malls to the user.

**Installation**

1. Clone the repository:
    git clone [repository URL]
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the application on an emulator or physical device.

**Usage**

Reporting Crowd Density:
1. Open the app and select the mall you are in.
2. Report the crowd density by choosing the appropriate option.
Viewing Mall Crowd Density:
1. Navigate to the screen showing mall lists.
2. Select a mall to view its current crowd density.

**Code Structure**
1. FragmentOne: Handles the map view and location-based services to find the nearest mall.

    Key method: onMapReady, updateLocationUI, getNearestMall.
   
2.FragmentTwo: Manages data retrieval from Firebase Firestore to display crowd density.

    Key method: displayData.
    
3.MainActivity: Manages the bottom navigation and fragment transactions.

4.Places Response and Results: Data models for handling API responses from Google Maps API.

**Dependencies**

1.Firebase Firestore

2.Google Maps API

3.Google Places API

4.OkHttpClient

**Contributing**

Contributions to improve CrowdSense are welcomed. Please follow these steps:

1.Fork the repository.

2.Create a new branch (git checkout -b feature-branch).

3.Make changes and commit (git commit -am 'Add some feature').

4.Push to the branch (git push origin feature-branch).

5.Create a new Pull Request.

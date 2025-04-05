# FoodieRee - Food Discovery App

## Overview
FoodieRee is an Android application that combines social media-style food content browsing with location-based restaurant discovery. Users can watch short video reels of food items and find nearby restaurants on an interactive map.

## Key Features

### Home Screen (Reels)
- Vertical scrolling food video reels
- Like/save/share functionality
- Restaurant information overlay
- Video autoplay with sound control
- "Crave & Order" button to quickly place orders

### Map Screen (Nearby)
- Interactive Google Maps integration
- Current location detection
- Restaurant pins with details
- Saved addresses functionality (Home/Work)
- Restaurant search capability
- Restaurant details in bottom sheet
- One-click ordering from map view

### General Features
- Intuitive bottom navigation
- Modern UI with consistent color scheme
- Crave & Order bottom sheet for quick checkout
- Location-based restaurant discovery within 10km

## Technical Implementation

### Technologies Used
- Java for Android
- Google Maps API for location functionality
- ExoPlayer/YouTube Player API for video playback
- ViewPager2 for smooth reel scrolling
- Material Design components
- ConstraintLayout for responsive UI
- Navigation Component for fragment management

### Architecture
- Fragment-based navigation
- Model-View-Controller pattern
- RecyclerView adapters for efficient list rendering
- Location services integration

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/foodieree5/
│   │   │   ├── activities/
│   │   │   │   └── MainActivity.java
│   │   │   ├── fragments/
│   │   │   │   ├── HomeFragment.java
│   │   │   │   ├── MapFragment.java
│   │   │   │   └── OrderBottomSheetFragment.java
│   │   │   ├── adapters/
│   │   │   │   └── ReelAdapter.java
│   │   │   └── models/
│   │   │       ├── Reel.java
│   │   │       └── Vendor.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   ├── fragment_map.xml
│   │   │   │   ├── item_reel.xml
│   │   │   │   └── bottom_sheet_order.xml
│   │   │   ├── navigation/
│   │   │   │   └── nav_graph.xml
│   │   │   ├── menu/
│   │   │   │   └── bottom_nav_menu.xml
│   │   │   ├── drawable/
│   │   │   │   ├── ic_home.xml
│   │   │   │   ├── ic_map.xml
│   │   │   │   ├── ic_current_location.xml
│   │   │   │   ├── ic_food_marker.xml
│   │   │   │   └── video_overlay_gradient.xml
│   │   │   └── values/
│   │   │       ├── colors.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── README.md
```

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Get a Google Maps API key from the Google Cloud Console
4. Add your API key to the `AndroidManifest.xml` file:
   ```xml
   <meta-data
       android:name="com.google.android.geo.API_KEY"
       android:value="YOUR_API_KEY" />
   ```
5. Sync the project with Gradle files
6. Run the app on an emulator or physical device

## Required Permissions
- `INTERNET`: For loading videos and map data
- `ACCESS_FINE_LOCATION`: For precise location on map
- `ACCESS_COARSE_LOCATION`: For approximate location

## Screenshots
- Home screen with food reels
- Map view with restaurant markers
- Restaurant details view
- Order confirmation screen

## Future Enhancements
- User authentication and profiles
- Order history and tracking
- Restaurant ratings and reviews
- Payment integration
- Food category filtering
- Personalized recommendations

## Dependencies
```gradle
dependencies {
    // AndroidX and UI components
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Navigation components
    implementation 'androidx.navigation:navigation-fragment:2.7.7'
    implementation 'androidx.navigation:navigation-ui:2.7.7'
    
    // Maps and location
    implementation 'com.google.android.gms:play-services-maps:18.2.0'
    implementation 'com.google.android.gms:play-services-location:21.1.0'
    
    // Video playback
    implementation 'com.google.android.exoplayer:exoplayer:2.19.1'
}
```

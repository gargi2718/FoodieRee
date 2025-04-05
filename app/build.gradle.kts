plugins {
    alias(libs.plugins.android.application)
}
android {
    namespace = "com.example.foodieree"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodieree"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.exoplayer:exoplayer:2.19.0") // ExoPlayer for video playback
    implementation("com.google.android.gms:play-services-location:21.0.1") // Location services
    implementation("com.google.maps.android:android-maps-utils:2.3.0") // Optional for Map View
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // If networking is needed
    // ExoPlayer for video playback
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    // Location services
    implementation ("com.google.android.gms:play-services-location:21.1.0")

    // Maps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    // Material Design
    implementation ("com.google.android.material:material:1.11.0")

    // Glide for image loading
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")

    // ExoPlayer
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    // Google Maps & Location
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.1.0")


    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")


        // Media3 dependencies
        implementation ("androidx.media3:media3-exoplayer:1.2.1")
        implementation ("androidx.media3:media3-ui:1.2.1")
        implementation ("androidx.media3:media3-common:1.2.1")
        implementation ("androidx.media3:media3-session:1.2.1")

    // Yt videos
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")



}
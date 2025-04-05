package com.example.foodieree.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodieree.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            // Set up navigation controller
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment != null) {
                navController = navHostFragment.getNavController();
            } else {
                Log.e(TAG, "NavHostFragment is null");
            }

            // Set up bottom navigation
            bottomNavigationView = findViewById(R.id.bottomNavigation);
            if (bottomNavigationView != null && navController != null) {
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
            }

            // Request location permissions for map functionality
            checkLocationPermissions();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Location permission granted");
            } else {
                Log.d(TAG, "Location permission denied");
            }
        }
    }
}
/*
package com.example.foodieree.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.foodieree.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "Setting content view");
            setContentView(R.layout.activity_main);

            Log.d(TAG, "Setting up navigation");
            setupNavigation();

            Log.d(TAG, "Setting up bottom navigation");
            setupBottomNavigation();

            Log.d(TAG, "Checking location permissions");
            checkLocationPermissions();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
        }
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            Log.e(TAG, "NavHostFragment is null");
        }
    }

    private void setupBottomNavigation() {
        try {
            bottomNavigationView = findViewById(R.id.bottomNavigation);
            if (bottomNavigationView != null && navController != null) {
                NavigationUI.setupWithNavController(bottomNavigationView, navController);
            } else {
                Log.e(TAG, "bottomNavigationView or navController is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up bottom navigation: ", e);
        }
    }



    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
*/
package com.example.foodieree5.utils;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.example.foodieree.utils.LocationUpdateListener;

public class LocationUtils {
    private final Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private LocationUpdateListener listener;

    public LocationUtils(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void startLocationUpdates(LocationUpdateListener listener) {
        this.listener = listener;

        LocationRequest locationRequest = new LocationRequest.Builder(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(5000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;
                currentLocation = locationResult.getLastLocation();
                if (listener != null && currentLocation != null) {
                    listener.onLocationUpdated(currentLocation);
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
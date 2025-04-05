package com.example.foodieree.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.foodieree.R;
import com.example.foodieree5.utils.LocationUtils;
import com.example.foodieree.utils.LocationUpdateListener;
import android.location.Location;
import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment implements OnMapReadyCallback, LocationUpdateListener {
    private GoogleMap mMap;
    private LocationUtils locationUtils;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final float DEFAULT_ZOOM = 15f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationUtils = new LocationUtils(requireContext());
        checkLocationPermission();
        return view;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        locationUtils.startLocationUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
        addDummyVendors();
    }

    private void setupMap() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        }
    }

    private void addDummyVendors() {
        // Add dummy vendor markers
        List<DummyVendor> vendors = getDummyVendors();
        for (DummyVendor vendor : vendors) {
            mMap.addMarker(new MarkerOptions()
                    .position(vendor.location)
                    .title(vendor.name)
                    .snippet(vendor.description));
        }
    }

    private List<DummyVendor> getDummyVendors() {
        List<DummyVendor> vendors = new ArrayList<>();
        vendors.add(new DummyVendor("Burger Heaven", "Best burgers in town",
                new LatLng(40.7128, -74.0060)));
        vendors.add(new DummyVendor("Pizza Paradise", "Authentic Italian Pizza",
                new LatLng(40.7150, -74.0080)));
        vendors.add(new DummyVendor("Sushi Station", "Fresh sushi and sashimi",
                new LatLng(40.7140, -74.0040)));
        return vendors;
    }

    @Override
    public void onLocationUpdated(Location location) {
        if (mMap != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
            // You could update nearby vendors based on new location here
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
                setupMap();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationUtils != null) {
            locationUtils.stopLocationUpdates();
        }
    }

    // Helper class for dummy vendors
    private static class DummyVendor {
        String name;
        String description;
        LatLng location;

        DummyVendor(String name, String description, LatLng location) {
            this.name = name;
            this.description = description;
            this.location = location;
        }
    }
}
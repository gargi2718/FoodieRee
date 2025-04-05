package com.example.foodieree.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.foodieree.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    // Location permission request
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    // Views
    private LinearLayout useCurrentLocationBtn;
    private LinearLayout homeAddressLayout;
    private LinearLayout workAddressLayout;
    private Button findNearbyBtn;
    private TextView vendorNameText, vendorAddressText, ratingText, priceText;
    private LinearLayout vendorDetailsContainer;

    // Map and location
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;

    // Saved Addresses
    private final LatLng HOME_ADDRESS = new LatLng(37.422, -122.084); // Near Google HQ
    private final LatLng WORK_ADDRESS = new LatLng(37.426, -122.077); // Nearby location

    // Data
    private List<Vendor> vendors = new ArrayList<>();
    private Vendor selectedVendor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize permission launcher
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) ||
                            (coarseLocationGranted != null && coarseLocationGranted)) {
                        // Location access granted
                        getCurrentLocation();
                    } else {
                        // No location access granted
                        Toast.makeText(requireContext(), "Location permission required",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize mock vendor data near Google HQ
        initializeVendors();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        useCurrentLocationBtn = view.findViewById(R.id.currentLocationLayout);
        homeAddressLayout = view.findViewById(R.id.homeAddressLayout);
        workAddressLayout = view.findViewById(R.id.workAddressLayout);
        findNearbyBtn = view.findViewById(R.id.findNearbyBtn);
        vendorDetailsContainer = view.findViewById(R.id.vendorDetailsContainer);
        vendorNameText = view.findViewById(R.id.vendorNameText);
        vendorAddressText = view.findViewById(R.id.vendorAddressText);
        ratingText = view.findViewById(R.id.ratingText);
        priceText = view.findViewById(R.id.priceText);
        Button orderBtn = view.findViewById(R.id.orderBtn);

        // Hide vendor details initially
        vendorDetailsContainer.setVisibility(View.GONE);

        // Set click listeners
        useCurrentLocationBtn.setOnClickListener(v -> requestLocationPermission());

        homeAddressLayout.setOnClickListener(v -> {
            moveToLocation(HOME_ADDRESS, "Home");
            Toast.makeText(requireContext(), "Going to home address", Toast.LENGTH_SHORT).show();
        });

        workAddressLayout.setOnClickListener(v -> {
            moveToLocation(WORK_ADDRESS, "Work");
            Toast.makeText(requireContext(), "Going to work address", Toast.LENGTH_SHORT).show();
        });

        findNearbyBtn.setOnClickListener(v -> {
            // Default to Google HQ area if no current location
            LatLng targetLocation = (currentLocation != null) ? currentLocation : new LatLng(37.422, -122.084);
            moveToLocation(targetLocation, "Current Location");
            showAllVendors();
            Toast.makeText(requireContext(), vendors.size() + " restaurants found nearby", Toast.LENGTH_SHORT).show();
        });

        orderBtn.setOnClickListener(v -> {
            if (selectedVendor != null) {
                Toast.makeText(requireContext(), "Ordering from: " + selectedVendor.name,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    if (mMap != null) {
                        // Add current location marker
                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("My Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        // Move camera to current location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                        // Add nearby vendor markers
                        showAllVendors();
                    }

                    Toast.makeText(requireContext(), "Location updated", Toast.LENGTH_SHORT).show();
                } else {
                    // If location is null, use default location (Google HQ for demo)
                    currentLocation = new LatLng(37.422, -122.084);

                    if (mMap != null) {
                        // Add current location marker
                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("Demo Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        // Move camera to current location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                        // Add nearby vendor markers
                        showAllVendors();
                    }

                    Toast.makeText(requireContext(), "Using demo location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void moveToLocation(LatLng location, String title) {
        if (mMap != null) {
            // Clear previous markers
            mMap.clear();

            // Add marker for the location
            BitmapDescriptor icon = title.equals("Home") || title.equals("Work")
                    ? BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(title)
                    .icon(icon));

            // Move camera to location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

            // Add vendor markers
            addVendorMarkers();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set map UI settings
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null && marker.getTag() instanceof Vendor) {
                Vendor vendor = (Vendor) marker.getTag();
                showVendorDetails(vendor);
                return true;
            }
            return false;
        });

        // Initial setup - show Google HQ area with vendors
        moveToLocation(new LatLng(37.422, -122.084), "Default Location");

        // Try to get current location if permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        }
    }

    private void addVendorMarkers() {
        for (Vendor vendor : vendors) {
            LatLng position = new LatLng(vendor.latitude, vendor.longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(vendor.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            if (marker != null) {
                marker.setTag(vendor);
            }
        }
    }

    private void showAllVendors() {
        if (mMap != null) {
            // Clear previous vendor markers
            mMap.clear();

            // Add current location marker if available
            if (currentLocation != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .title("My Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }

            // Add all vendor markers
            addVendorMarkers();
        }
    }

    private void showVendorDetails(Vendor vendor) {
        selectedVendor = vendor;

        // Update UI
        vendorNameText.setText(vendor.name);
        vendorAddressText.setText(vendor.address);
        ratingText.setText("★ " + vendor.rating);
        priceText.setText(vendor.priceRange);

        // Show details container
        vendorDetailsContainer.setVisibility(View.VISIBLE);

        // Zoom to vendor location
        LatLng position = new LatLng(vendor.latitude, vendor.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17f));
    }

    private void initializeVendors() {
        // Initialize with mock data near Google HQ area (37.42, -122.08)
        vendors.add(new Vendor("Burger King", 37.423, -122.083, "1401 El Camino Real", "4.2", "$$"));
        vendors.add(new Vendor("Pizza Hut", 37.421, -122.085, "123 Main Street", "4.5", "$$"));
        vendors.add(new Vendor("Taco Bell", 37.425, -122.082, "456 Castro Street", "3.9", "$"));
        vendors.add(new Vendor("Sushi Palace", 37.419, -122.081, "789 Oak Avenue", "4.7", "$$$"));
        vendors.add(new Vendor("Dessert Haven", 37.422, -122.086, "555 Pine Boulevard", "4.8", "$$"));
        vendors.add(new Vendor("Curry House", 37.418, -122.079, "222 Maple Drive", "4.6", "$$"));
        vendors.add(new Vendor("Noodle Express", 37.424, -122.087, "333 Elm Street", "4.3", "$"));
        vendors.add(new Vendor("Steakhouse", 37.420, -122.075, "777 Birch Lane", "4.9", "$$$"));
        vendors.add(new Vendor("Vegetarian Delight", 37.426, -122.080, "888 Cedar Court", "4.4", "$$"));
        vendors.add(new Vendor("Coffee Shop", 37.421, -122.078, "999 Willow Road", "4.1", "$"));
        vendors.add(new Vendor("Ice Cream Parlor", 37.423, -122.076, "111 Cherry Street", "4.5", "$"));
        vendors.add(new Vendor("Sandwich Stop", 37.418, -122.084, "444 Ash Avenue", "4.0", "$"));
    }

    // Simple Vendor class
    private static class Vendor {
        String name;
        double latitude;
        double longitude;
        String address;
        String rating;
        String priceRange;

        Vendor(String name, double latitude, double longitude, String address, String rating, String priceRange) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
            this.rating = rating;
            this.priceRange = priceRange;
        }
    }
}
/*
package com.example.foodieree.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodieree.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    // Location permission request
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    // Views
    private LinearLayout useCurrentLocationBtn;
    private RecyclerView vendorRecyclerView;
    private Button orderBtn;
    private TextView vendorNameText, vendorAddressText, ratingText, priceText;
    private LinearLayout vendorDetailsContainer;

    // Map and location
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;

    // Data
    private List<Vendor> vendors = new ArrayList<>();
    private Vendor selectedVendor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize permission launcher
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if ((fineLocationGranted != null && fineLocationGranted) ||
                            (coarseLocationGranted != null && coarseLocationGranted)) {
                        // Location access granted
                        getCurrentLocation();
                    } else {
                        // No location access granted
                        Toast.makeText(requireContext(), "Location permission required",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize mock vendor data
        initializeVendors();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        useCurrentLocationBtn = view.findViewById(R.id.currentLocationLayout);
        vendorDetailsContainer = view.findViewById(R.id.vendorDetailsContainer);
        vendorNameText = view.findViewById(R.id.vendorNameText);
        vendorAddressText = view.findViewById(R.id.vendorAddressText);
        ratingText = view.findViewById(R.id.ratingText);
        priceText = view.findViewById(R.id.priceText);
        orderBtn = view.findViewById(R.id.orderBtn);

        // Hide vendor details initially
        vendorDetailsContainer.setVisibility(View.GONE);

        // Set click listeners
        useCurrentLocationBtn.setOnClickListener(v -> {
            requestLocationPermission();
        });

        orderBtn.setOnClickListener(v -> {
            if (selectedVendor != null) {
                Toast.makeText(requireContext(), "Ordering from: " + selectedVendor.name,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    if (mMap != null) {
                        // Clear previous markers
                        mMap.clear();

                        // Add current location marker
                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("My Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        // Move camera to current location
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                        // Add nearby vendor markers
                        addVendorMarkers();
                    }

                    Toast.makeText(requireContext(), "Location updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Unable to get current location",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set map UI settings
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getTag() != null && marker.getTag() instanceof Vendor) {
                Vendor vendor = (Vendor) marker.getTag();
                showVendorDetails(vendor);
                return true;
            }
            return false;
        });

        // Add vendor markers to map
        addVendorMarkers();

        // Try to get current location if permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        }
    }

    private void addVendorMarkers() {
        if (mMap == null) return;

        for (Vendor vendor : vendors) {
            LatLng position = new LatLng(vendor.latitude, vendor.longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(vendor.name));

            if (marker != null) {
                marker.setTag(vendor);
            }
        }
    }

    private void showVendorDetails(Vendor vendor) {
        selectedVendor = vendor;

        // Update UI
        vendorNameText.setText(vendor.name);
        vendorAddressText.setText(vendor.address);
        ratingText.setText("★ " + vendor.rating);
        priceText.setText(vendor.priceRange);

        // Show details container
        vendorDetailsContainer.setVisibility(View.VISIBLE);

        // Zoom to vendor location
        LatLng position = new LatLng(vendor.latitude, vendor.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17f));
    }

    private void initializeVendors() {
        // Initialize with mock data
        vendors.add(new Vendor("Burger Heaven", 40.7128, -74.006, "123 Food St", "4.5", "$$"));
        vendors.add(new Vendor("Pizza Palace", 40.7138, -74.008, "456 Main Ave", "4.8", "$$"));
        vendors.add(new Vendor("Taco Town", 40.7148, -74.003, "789 Taco Ln", "4.2", "$"));
        vendors.add(new Vendor("Sushi Spot", 40.7118, -74.009, "321 Fish Blvd", "4.7", "$$$"));
        vendors.add(new Vendor("Dessert Delight", 40.7158, -74.004, "555 Sweet St", "4.9", "$$"));
    }

    // Simple Vendor class
    private static class Vendor {
        String name;
        double latitude;
        double longitude;
        String address;
        String rating;
        String priceRange;

        Vendor(String name, double latitude, double longitude, String address, String rating, String priceRange) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
            this.rating = rating;
            this.priceRange = priceRange;
        }
    }
}

 */
/*
package com.example.foodieree.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodieree.R;
import android.Manifest;

public class MapFragment extends Fragment {

    private ActivityResultLauncher<String[]> locationPermissionRequest;
    private RecyclerView restaurantList;
    private Button findNearbyBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize permission launcher
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted
                        findNearbyRestaurants();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted
                        findNearbyRestaurants();
                    } else {
                        // No location access granted
                        Toast.makeText(requireContext(), "Location permission required to find nearby restaurants",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantList = view.findViewById(R.id.restaurantList);
        restaurantList.setLayoutManager(new LinearLayoutManager(requireContext()));

        findNearbyBtn = view.findViewById(R.id.findNearbyBtn);
        findNearbyBtn.setOnClickListener(v -> requestLocationPermission());

        // Display mock data initially
        displayMockRestaurants();
    }

    private void requestLocationPermission() {
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void findNearbyRestaurants() {
        Toast.makeText(requireContext(), "Finding nearby restaurants...", Toast.LENGTH_SHORT).show();
        // For now just display mock data
        displayMockRestaurants();
    }

    private void displayMockRestaurants() {
        // In a real implementation, you would use a proper adapter with restaurant data
        // For now, just show a message
        Toast.makeText(requireContext(), "5 restaurants found nearby", Toast.LENGTH_SHORT).show();
    }
}
*/
/*
package com.example.foodieree.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.foodieree.R;
import com.example.foodieree.utils.LocationUpdateListener;
import com.example.foodieree5.utils.LocationUtils;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationUpdateListener {
    private GoogleMap mMap;
    private LocationUtils locationUtils;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        if (mMap != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
            // You can also update nearby vendors here based on the new location
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
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
}
*/
package com.example.foodieree.models;

public class Vendor {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private String address;
    private String rating;
    private String priceRange;

    public Vendor(String id, String name, double latitude, double longitude,
                  String description, String address, String rating, String priceRange) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.address = address;
        this.rating = rating;
        this.priceRange = priceRange;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public String getRating() { return rating; }
    public String getPriceRange() { return priceRange; }
}
/*
package com.example.foodieree.models;

import com.google.android.gms.maps.model.LatLng;

public class Vendor {
    private String id;
    private String name;
    private String description;
    private LatLng location;

    public Vendor(String id, String name, String description, LatLng location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LatLng getLocation() { return location; }
}
*/


package com.example.foodieree.models;

public class Reel {
    private String id;
    private String videoUrl;
    private String foodTitle;
    private String restaurantName;
    private String distance;
    private boolean isLiked;
    private boolean isBookmarked;

    public Reel(String id, String videoUrl, String foodTitle, String restaurantName, String distance) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.foodTitle = foodTitle;
        this.restaurantName = restaurantName;
        this.distance = distance;
        this.isLiked = false;
        this.isBookmarked = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getVideoUrl() { return videoUrl; }
    public String getFoodTitle() { return foodTitle; }
    public String getRestaurantName() { return restaurantName; }
    public String getDistance() { return distance; }
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }
    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
}


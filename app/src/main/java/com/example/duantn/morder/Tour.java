package com.example.duantn.morder;

public class Tour {
    private String id;
    private String tour_name;
    private String avatar;
    private int rating;

    public Tour(String id, String tour_name, String avatar, int rating) {
        this.id = id;
        this.tour_name = tour_name;
        this.avatar = avatar;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTour_name() {
        return tour_name;
    }

    public void setTour_name(String tour_name) {
        this.tour_name = tour_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

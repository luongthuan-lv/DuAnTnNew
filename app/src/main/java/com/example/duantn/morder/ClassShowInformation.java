package com.example.duantn.morder;

public class ClassShowInformation {
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private int position;


    public ClassShowInformation(double latitude, double longitude, String title, String content, int position) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.content = content;
        this.position = position;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

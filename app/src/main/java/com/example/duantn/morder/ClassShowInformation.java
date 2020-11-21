package com.example.duantn.morder;

import java.util.List;

public class ClassShowInformation {
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    private List<String> waypoints;
    private List<String> imageList;
    private boolean audio=false;
    private boolean visited = false;


    public List<String> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<String> waypoints) {
        this.waypoints = waypoints;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
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

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

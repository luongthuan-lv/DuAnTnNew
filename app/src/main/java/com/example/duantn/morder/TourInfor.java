
package com.example.duantn.morder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TourInfor {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("place")
    @Expose
    private String place;

    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("information")
    @Expose
    private String information;
    @SerializedName("avatar")
    @Expose
    private List<String> avatar = null;


    private boolean audio=false;
    private boolean visited = false;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public List<String> getAvatar() {
        return avatar;
    }
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
    public void setAvatar(List<String> avatar) {
        this.avatar = avatar;
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

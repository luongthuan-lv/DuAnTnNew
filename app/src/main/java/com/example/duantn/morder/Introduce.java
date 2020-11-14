
package com.example.duantn.morder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Introduce {

    @SerializedName("id_tour")
    @Expose
    private String idTour;
    @SerializedName("tour_name")
    @Expose
    private String tourName;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("introduce")
    @Expose
    private String introduce;

    public String getIdTour() {
        return idTour;
    }

    public void setIdTour(String idTour) {
        this.idTour = idTour;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

}

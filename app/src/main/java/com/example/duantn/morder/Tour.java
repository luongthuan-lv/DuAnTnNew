
package com.example.duantn.morder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tour {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("cate_name")
    @Expose
    private String cateName;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("router")
    @Expose
    private String router;
    @SerializedName("lang_id")
    @Expose
    private String langId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCateName() {
        return cateName;
    }
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

}

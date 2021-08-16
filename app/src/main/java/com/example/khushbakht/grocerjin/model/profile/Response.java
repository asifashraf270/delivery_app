package com.example.khushbakht.grocerjin.model.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 9/26/2017.
 */

public class Response {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("Name")
    private String name = "";

    @SerializedName("username")
    private String username = "";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @SerializedName("locationName")
    private String location = "";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("locationLatitude")
    private String latitude = "";
    @SerializedName("locationLongitude")
    private String longitude = "";

}

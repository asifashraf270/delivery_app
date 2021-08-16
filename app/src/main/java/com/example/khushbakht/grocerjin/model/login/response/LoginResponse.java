package com.example.khushbakht.grocerjin.model.login.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 10/12/2017.
 */

public class LoginResponse {

    @SerializedName("e_id")
    @Expose
    private String eId;

    @SerializedName("location_id")
    @Expose
    private String location_id;

    public String getPush_name() {
        return push_name;
    }

    public void setPush_name(String push_name) {
        this.push_name = push_name;
    }

    @SerializedName("location_name")

    @Expose
    private String location_name;

    @SerializedName("push_name")
    @Expose
    private String push_name;


    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getEId() {
        return eId;
    }

    public void setEId(String eId) {
        this.eId = eId;
    }

}
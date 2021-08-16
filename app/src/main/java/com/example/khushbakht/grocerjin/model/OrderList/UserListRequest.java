package com.example.khushbakht.grocerjin.model.OrderList;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 8/11/2017.
 */

public class UserListRequest {

    @SerializedName("deliveryMan_id")
    String deliveryMan_id;


    @SerializedName("location_id")
    String location_id;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getAdmin_id() {
        return deliveryMan_id;
    }

    public void setAdmin_id(String deliveryMan_id) {
        this.deliveryMan_id = deliveryMan_id;
    }
}

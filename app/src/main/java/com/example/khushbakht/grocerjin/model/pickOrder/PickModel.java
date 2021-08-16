package com.example.khushbakht.grocerjin.model.pickOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 11/22/2017.
 */

public class PickModel {
    @SerializedName("status_id")
    public int StatusId;
    @SerializedName("driver_id")
    public int driverId;

  /*  public String getDeliveryman_id() {
        return deliveryman_id;
    }

    public void setDeliveryman_id(String deliveryman_id) {
        this.deliveryman_id = deliveryman_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

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

    @SerializedName("deliveryMan_id")
    @Expose
    private String deliveryman_id;
    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("order_status")
    @Expose
    private String order_status;


    @SerializedName("location_id")
    @Expose
    private String location_id;

    @SerializedName("location_name")
    @Expose
    private String location_name;*/

}

package com.example.khushbakht.grocerjin.model.Postdelivery;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {
    @SerializedName("status_id")
    public int statusId;
    @SerializedName("reason")
    public String reason;
   /* public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SerializedName("order_id")
    private String orderId = "";

    public String getCashRecieved() {
        return cashRecieved;
    }

    public void setCashRecieved(String cashRecieved) {
        this.cashRecieved = cashRecieved;
    }

    @SerializedName("cash_recieved")
    private String cashRecieved = "";
    @SerializedName("order_status")
    private String orderStatus = "";
    @SerializedName("delivery_boy_notes")
    private String deliveryBoyNotes = "";


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryBoyNotes() {
        return deliveryBoyNotes;
    }

    public void setDeliveryBoyNotes(String deliveryBoyNotes) {
        this.deliveryBoyNotes = deliveryBoyNotes;
    }*/

}
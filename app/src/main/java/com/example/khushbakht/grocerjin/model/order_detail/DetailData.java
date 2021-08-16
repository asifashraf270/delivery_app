package com.example.khushbakht.grocerjin.model.order_detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 9/6/2017.
 */

public class DetailData {

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("order_id")
    private String orderno = "";
    @SerializedName("phone_number")
    private String dateTime = "";

    @SerializedName("total_amount")
    private String amount = "";
    @SerializedName("address")
    private String address = "";
}

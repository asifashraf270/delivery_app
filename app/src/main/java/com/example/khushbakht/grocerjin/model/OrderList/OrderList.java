package com.example.khushbakht.grocerjin.model.OrderList;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by khush on 8/11/2017.
 */

public class OrderList implements Serializable {

    @SerializedName("order_id")
    private String orderno = "";

    @SerializedName("order_tax")
    public String orderTax;
    @SerializedName("addedOn")
    private String dateTime;
    @SerializedName("latitude")
    private String latitude = "";
    @SerializedName("longitude")
    private String longitude = "";
    @SerializedName("order_status")
    private Integer status;
    @SerializedName("customer_id")
    public Integer customerId;
    @SerializedName("driver_id")
    public Integer driverId;
    @SerializedName("customer_name")
    public String customerName;
    @SerializedName("customer_phone")
    public String customerPhoneNo;

    public String get_amount() {
        return _amount;
    }

    public void set_amount(String _amount) {
        this._amount = _amount;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    @SerializedName("delivery_address")
    private String address = "";
    @SerializedName("phone_number")
    private String phone_number = "";
    @SerializedName("order_amount")
    private String amount = "";
    @SerializedName("amount")
    private String _amount = "";
    @SerializedName("delivery_charges")
    private String delivery_charges = "";
    @SerializedName("Total_items")
    private String totalItems = "";
    @SerializedName("order_status_name")
    public String orderStatusName;
    @SerializedName("order_note")
    public String orderNote;
    @SerializedName("payment_method_id")
    public Integer paymentMethod;


    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

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

    public String getOrderno() {
        return orderno;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}

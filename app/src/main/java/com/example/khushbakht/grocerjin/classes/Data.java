package com.example.khushbakht.grocerjin.classes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Khushbakht on 7/14/2017.
 */

public class Data {
    private String orderno;
    private String dateTime;
    private String status;
    private String timeOfDelivery;
    private String address;

    public Data(String orderno, String status, String dateTime, String timeOfDelivery, String address)
    {
        this.orderno = orderno;
        this.status = status;
        this.dateTime = dateTime;
        this.timeOfDelivery = timeOfDelivery;
        this.address= address;
    }


    public void setOrderno(String orderno)
    {
        this.orderno = orderno;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }
    public String getOrderno()
    {
        return orderno;
    }
    public String getStatus()
    {
        return status;
    }
    public String getDateTime()
    {
        return dateTime;
    }
    public void setTimeOfDelivery(String timeOfDelivery)
    {
        this.timeOfDelivery = timeOfDelivery;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getTimeOFDelivery()
    {
        return timeOfDelivery;
    }
    public String getAddress()
    {
        return address;
    }



}

package com.example.khushbakht.grocerjin.responseentity;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponseModel {
    @SerializedName("message")
    public String message;
    @SerializedName("statusCode")
    public int statusCode;
    @SerializedName("response")
    public OrderDetailResponse response;
}

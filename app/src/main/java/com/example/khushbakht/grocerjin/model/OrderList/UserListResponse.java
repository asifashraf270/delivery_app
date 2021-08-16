package com.example.khushbakht.grocerjin.model.OrderList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 8/11/2017.
 */

public class UserListResponse {
    @SerializedName("message")

    public String message;
    @SerializedName("statusCode")
    public int statusCode;
    @SerializedName("response")
    private OrderResponse response = null;


    public OrderResponse getResponse() {
        return response;
    }

    public void setResponse(OrderResponse response) {
        this.response = response;
    }
}

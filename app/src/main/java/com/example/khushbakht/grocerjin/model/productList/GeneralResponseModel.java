package com.example.khushbakht.grocerjin.model.productList;

import com.google.gson.annotations.SerializedName;

public class GeneralResponseModel {
    @SerializedName("message")
    public String message;
    @SerializedName("statusCode")
    public int statusCode;
}

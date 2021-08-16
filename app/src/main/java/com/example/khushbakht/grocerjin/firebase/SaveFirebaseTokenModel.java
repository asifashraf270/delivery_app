package com.example.khushbakht.grocerjin.firebase;

import com.google.gson.annotations.SerializedName;

public class SaveFirebaseTokenModel {
    @SerializedName("driver_id")
    public int driver_id;
    @SerializedName("device_token")
    public String deviceToken;
    @SerializedName("device_type")
    public String deviceType="android";
}

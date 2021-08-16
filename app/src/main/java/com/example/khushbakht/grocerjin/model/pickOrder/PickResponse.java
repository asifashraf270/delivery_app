package com.example.khushbakht.grocerjin.model.pickOrder;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 11/22/2017.
 */

public class PickResponse {
    @SerializedName("message")
    public String message;
    @SerializedName("statusCode")
    public int statusCode;
    /*@SerializedName("status")
    PickStatus status;

    public PickStatus getStatus() {
        return status;
    }

    public void setStatus(PickStatus status) {
        this.status = status;
    }*/

}

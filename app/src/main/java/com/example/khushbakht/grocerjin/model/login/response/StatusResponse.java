package com.example.khushbakht.grocerjin.model.login.response;

import com.example.khushbakht.grocerjin.model.login.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public class StatusResponse {

    @SerializedName("status")
    @Expose
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

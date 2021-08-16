package com.example.khushbakht.grocerjin.model.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 9/26/2017.
 */

public class ProfileResponse {
    @SerializedName("status")
    Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @SerializedName("response")
    Response response;

}

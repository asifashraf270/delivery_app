package com.example.khushbakht.grocerjin.model.StatusUpdate;

import com.example.khushbakht.grocerjin.model.OrderList.Status;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 9/11/2017.
 */

public class Change_Order_Status {

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

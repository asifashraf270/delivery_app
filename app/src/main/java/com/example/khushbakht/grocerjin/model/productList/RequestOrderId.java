package com.example.khushbakht.grocerjin.model.productList;

/**
 * Created by khush on 8/23/2017.
 */


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hafiz Haseeem on 8/23/2017.
 */

public class RequestOrderId implements Serializable {

    @SerializedName("order_id")
    public String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getorder_status() {
        return order_status;
    }

    public void setorder_status(String order_status) {
        this.order_status = order_status;
    }

    public String order_status;


}
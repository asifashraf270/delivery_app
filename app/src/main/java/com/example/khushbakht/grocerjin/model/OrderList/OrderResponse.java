package com.example.khushbakht.grocerjin.model.OrderList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by khush on 8/16/2017.
 */

public class OrderResponse {
    @SerializedName("order_detail")
    List<OrderList> orders = null;

    public List<OrderList> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderList> orders) {
        this.orders = orders;
    }
}

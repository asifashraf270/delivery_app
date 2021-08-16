package com.example.khushbakht.grocerjin.classes;

import com.example.khushbakht.grocerjin.model.OrderList.OrderList;

/**
 * Created by khush on 9/19/2017.
 */

public class DataPass {
    public OrderList getOrderObject() {
        return orderObject;
    }

    public void setOrderObject(OrderList orderList) {
        this.orderObject = orderList;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    OrderList orderObject;
    String lng;
}

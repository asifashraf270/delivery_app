package com.example.khushbakht.grocerjin;

import com.example.khushbakht.grocerjin.model.OrderList.OrderList;

/**
 * Created by khush on 1/23/2018.
 */

public class DataController {
    public OrderList getOrderList() {
        return orderList;
    }

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }

    OrderList orderList;
}

package com.example.khushbakht.grocerjin;

import com.example.khushbakht.grocerjin.model.OrderList.OrderList;

/**
 * Created by Haseee on 11/29/2017.
 */

public class Controller {

    private String inboxMessage = null;
    private String inboxTime = null;
    OrderList orderList = null;

    public OrderList getOrderList() {
        return orderList;
    }

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }

    public String getInboxTime() {
        return inboxTime;
    }

    public void setInboxTime(String inboxTime) {
        this.inboxTime = inboxTime;
    }

    public String getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(String inboxMessage) {
        this.inboxMessage = inboxMessage;
    }
}

package com.example.khushbakht.grocerjin.model.push;

/**
 * Created by Sajid on 26/12/2017.
 */

public class PushResponse {
    String message;
    String Orderid;
    String order_status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderid() {
        return Orderid;
    }

    public void setOrderid(String orderid) {
        Orderid = orderid;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}

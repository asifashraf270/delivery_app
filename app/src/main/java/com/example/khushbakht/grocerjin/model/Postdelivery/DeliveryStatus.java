package com.example.khushbakht.grocerjin.model.Postdelivery;

import java.io.Serializable;

/**
 * Created by khush on 8/30/2017.
 */

public class DeliveryStatus implements Serializable {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    int code;
    String message;
}

package com.example.khushbakht.grocerjin.model.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by khush on 9/26/2017.
 */

public class ProfileRequest {

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    @SerializedName("e_id")
    String e_id = "";
}

package com.example.khushbakht.grocerjin.model.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Khushbakht on 8/10/2017.
 */

public class PostSignUp {

    @SerializedName("username")
    private String email = "";

    @SerializedName("password")
    private String password = "";



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

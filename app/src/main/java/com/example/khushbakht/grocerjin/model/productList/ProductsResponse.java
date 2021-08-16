package com.example.khushbakht.grocerjin.model.productList;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsResponse {

    @SerializedName("message")
    public String message;
    @SerializedName("statusCode")
    public Integer statusCode;
    @SerializedName("response")
    @Expose
    private ProductList response;


    public ProductList getResponse() {
        return response;
    }

    public void setResponse(ProductList response) {
        this.response = response;
    }

}

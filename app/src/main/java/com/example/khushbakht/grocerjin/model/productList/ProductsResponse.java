package com.example.khushbakht.grocerjin.model.productList;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsResponse {

    @SerializedName("status")
    @Expose
    private ProductStatus status;
    @SerializedName("response")
    @Expose
    private ProductList response;

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public ProductList getResponse() {
        return response;
    }

    public void setResponse(ProductList response) {
        this.response = response;
    }

}

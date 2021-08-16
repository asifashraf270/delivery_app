package com.example.khushbakht.grocerjin.model.productList;

/**
 * Created by khush on 8/23/2017.
 */



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductList {

    @SerializedName("Products")
    @Expose
    private List<Product> products = null;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}

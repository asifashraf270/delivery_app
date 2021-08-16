package com.example.khushbakht.grocerjin.model.productList;

/**
 * Created by khush on 8/23/2017.
 */

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Product {


    @SerializedName("p_id")

    @Expose

    private String pId;

    @SerializedName("p_title")

    @Expose

    private String pTitle;

    @SerializedName("p_quantity")

    @Expose

    private String pQuantity;

    @SerializedName("p_status")

    @Expose

    private String pStatus;

    @SerializedName("p_notes")

    @Expose

    private String pNotes;

    @SerializedName("p_image")

    @Expose

    private String pImage;

    @SerializedName("p_actualPrice")

    @Expose

    private String p_actualPrice;
    @SerializedName("p_actualWeight")

    @Expose

    private String p_actualWeight;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getP_actualWeight() {
        return p_actualWeight;
    }

    public void setP_actualWeight(String p_actualWeight) {
        this.p_actualWeight = p_actualWeight;
    }

    public String getP_estimatedWeight() {
        return p_estimatedWeight;
    }

    public void setP_estimatedWeight(String p_estimatedWeight) {
        this.p_estimatedWeight = p_estimatedWeight;
    }

    @SerializedName("p_estimatedWeight")

    @Expose

    private String p_estimatedWeight;


    public String getP_actualPrice() {
        return p_actualPrice;
    }

    public void setP_actualPrice(String p_actualPrice) {
        this.p_actualPrice = p_actualPrice;
    }

    public String getPId() {

        return pId;
    }


    public void setPId(String pId) {

        this.pId = pId;
    }


    public String getPTitle() {

        return pTitle;
    }


    public void setPTitle(String pTitle) {

        this.pTitle = pTitle;
    }


    public String getPQuantity() {

        return pQuantity;
    }


    public void setPQuantity(String pQuantity) {

        this.pQuantity = pQuantity;
    }


    public String getPStatus() {

        return pStatus;
    }


    public void setPStatus(String pStatus) {

        this.pStatus = pStatus;
    }


    public String getPNotes() {

        return pNotes;
    }


    public void setPNotes(String pNotes) {

        this.pNotes = pNotes;
    }


    public String getPImage() {

        return pImage;
    }


    public void setPImage(String pImage) {

        this.pImage = pImage;
    }


}

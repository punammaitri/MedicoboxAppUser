package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderTrackingModel {
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("company_name")
    private String company_name;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("price")
    private String price;


    public OrderTrackingModel(String id, String company_name, String image, String price) {
        this.id = id;
        this.company_name = company_name;
        this.image = image;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

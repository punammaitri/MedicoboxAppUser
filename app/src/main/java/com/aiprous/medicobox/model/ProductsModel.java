package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsModel {


    public ProductsModel(String id, String sku, String title, String price, String image_url) {
        this.id = id;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.image_url = image_url;
    }

    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("sku")
    private String sku;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("price")
    private String price;
    @Expose
    @SerializedName("image_url")
    private String image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

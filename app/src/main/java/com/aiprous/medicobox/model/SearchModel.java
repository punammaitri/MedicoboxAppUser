package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchModel {

    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("sku")
    public String sku;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("price")
    public String price;

    @Expose
    @SerializedName("discount")
    public String discount;

    @Expose
    @SerializedName("short_description")
    public String short_description;
    @Expose
    @SerializedName("image")
    public String image;

    public SearchModel(String id, String sku, String title, String price, String discount, String short_description, String image) {
        this.id = id;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.short_description = short_description;
        this.image = image;
    }

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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return title;
    }
}

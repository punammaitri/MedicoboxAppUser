package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductsModel {

    @Expose
    @SerializedName("item_id")
    private String item_id;
    @Expose
    @SerializedName("order_id")
    private String order_id;
    @Expose
    @SerializedName("sku")
    private String sku;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("price")
    private String price;

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    @SerializedName("mrp_total")
    private String mrp_total;

    public ProductsModel(String item_id, String order_id, String sku, String name, String price, String image, String mrp_total) {
        this.item_id = item_id;
        this.order_id = order_id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.image = image;
        this.mrp_total = mrp_total;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMrp_total() {
        return mrp_total;
    }

    public void setMrp_total(String mrp_total) {
        this.mrp_total = mrp_total;
    }
}

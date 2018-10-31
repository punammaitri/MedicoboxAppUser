package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListModel {

    String id;
    String sku;
    String title;
    String price;
    String sale_price;
    String discount;
    String short_description;
    String prescription_required;
    String image;

    public ListModel(String id, String sku, String title, String price, String sale_price, String discount, String short_description, String prescription_required, String image) {
        this.id = id;
        this.sku = sku;
        this.title = title;
        this.price = price;
        this.sale_price = sale_price;
        this.discount = discount;
        this.short_description = short_description;
        this.prescription_required = prescription_required;
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

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
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

    public String getPrescription_required() {
        return prescription_required;
    }

    public void setPrescription_required(String prescription_required) {
        this.prescription_required = prescription_required;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

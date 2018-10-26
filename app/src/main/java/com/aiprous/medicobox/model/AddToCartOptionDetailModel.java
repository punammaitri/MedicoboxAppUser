package com.aiprous.medicobox.model;

public class AddToCartOptionDetailModel {

    String image;
    String medicineName;
    String value;
    String mrp;
    String discount;
    String price;
    String Qty;
    String sku;


    public AddToCartOptionDetailModel(String image, String medicineName, String value, String mrp, String discount, String price, String qty,String sku) {
        this.image = image;
        this.medicineName = medicineName;
        this.value = value;
        this.mrp = mrp;
        this.discount = discount;
        this.price = price;
        Qty = qty;
        this.sku=sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}

package com.aiprous.medicobox.model;

public class AddToCartOptionDetailModel {


    int image;
    String medicineName;
    String value;
    String mrp;
    String discount;
    String price;
    String Qty;


    public AddToCartOptionDetailModel(int image, String medicineName, String value, String mrp, String discount, String price, String qty) {
        this.image = image;
        this.medicineName = medicineName;
        this.value = value;
        this.mrp = mrp;
        this.discount = discount;
        this.price = price;
        Qty = qty;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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
}

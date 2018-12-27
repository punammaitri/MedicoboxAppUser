package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinalPaymentModel {

    @Expose
    @SerializedName("product_id")
    private String product_id;
    @Expose
    @SerializedName("qty")
    private int qty;
    @Expose
    @SerializedName("price")
    private String price;

    public FinalPaymentModel(String product_id, int qty, String price) {
        this.product_id = product_id;
        this.qty = qty;
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

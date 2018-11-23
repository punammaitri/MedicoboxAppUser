package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {


    @Expose
    @SerializedName("response")
    private ArrayList<Response> response;


    public ArrayList<Response> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Response> response) {
        this.response = response;
    }

    public static class Response {
        @Expose
        @SerializedName("discount")
        private int discount;
        @Expose
        @SerializedName("prescription")
        private String prescription;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("short_description")
        private String short_description;
        @Expose
        @SerializedName("sale_price")
        private String sale_price;
        @Expose
        @SerializedName("quote_id")
        private String quote_id;
        @Expose
        @SerializedName("product_type")
        private String product_type;
        @Expose
        @SerializedName("price")
        private String price;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("qty")
        private int qty;
        @Expose
        @SerializedName("sku")
        private String sku;
        @Expose
        @SerializedName("item_id")
        private int item_id;

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getPrescription() {
            return prescription;
        }

        public void setPrescription(String prescription) {
            this.prescription = prescription;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getShort_description() {
            return short_description;
        }

        public void setShort_description(String short_description) {
            this.short_description = short_description;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(String quote_id) {
            this.quote_id = quote_id;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public Response(int discount, String prescription, String image, String short_description, String sale_price, String quote_id, String product_type, String price, String name, String id, int qty, String sku, int item_id) {
            this.discount = discount;
            this.prescription = prescription;
            this.image = image;
            this.short_description = short_description;
            this.sale_price = sale_price;
            this.quote_id = quote_id;
            this.product_type = product_type;
            this.price = price;
            this.name = name;
            this.id = id;
            this.qty = qty;
            this.sku = sku;
            this.item_id = item_id;
        }
    }



}

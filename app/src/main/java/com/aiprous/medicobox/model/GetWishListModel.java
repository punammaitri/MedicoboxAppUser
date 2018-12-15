package com.aiprous.medicobox.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWishListModel {

    @Expose
    @SerializedName("wishlist_name_id")
    public String wishlist_name_id;
    @Expose
    @SerializedName("wishlist_name")
    public String wishlist_name;
    @Expose
    @SerializedName("items")
    public JsonArray items;


    public GetWishListModel(String wishlist_name_id, String wishlist_name, JsonArray items) {
        this.wishlist_name_id = wishlist_name_id;
        this.wishlist_name = wishlist_name;
        this.items = items;
    }

    public String getWishlist_name_id() {
        return wishlist_name_id;
    }

    public void setWishlist_name_id(String wishlist_name_id) {
        this.wishlist_name_id = wishlist_name_id;
    }

    public String getWishlist_name() {
        return wishlist_name;
    }

    public void setWishlist_name(String wishlist_name) {
        this.wishlist_name = wishlist_name;
    }

    public JsonArray getItems() {
        return items;
    }

    public void setItems(JsonArray items) {
        this.items = items;
    }
}

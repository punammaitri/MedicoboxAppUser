package com.aiprous.medicobox.fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerModel {


    public BannerModel(String image_url) {
        this.image_url = image_url;
    }

    @Expose
    @SerializedName("image_url")
    private String image_url;


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

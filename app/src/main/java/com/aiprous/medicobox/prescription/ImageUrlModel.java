package com.aiprous.medicobox.prescription;

public class ImageUrlModel {

    String imageUrl;

    public ImageUrlModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return imageUrl;
    }
}

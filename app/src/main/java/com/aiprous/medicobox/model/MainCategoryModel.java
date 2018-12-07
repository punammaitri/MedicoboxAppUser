package com.aiprous.medicobox.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCategoryModel {

    @Expose
    @SerializedName("categoryName")
    public String categoryName;

    @Expose
    @SerializedName("CategoryId")
    public String CategoryId;

    @Expose
    @SerializedName("image_url")
    public String image_url;

    @Expose
    @SerializedName("SubCategory")
    public JsonArray SubCategory;


    public MainCategoryModel(String categoryName, String categoryId, String image_url, JsonArray subCategory) {
        this.categoryName = categoryName;
        CategoryId = categoryId;
        this.image_url = image_url;
        SubCategory = subCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public JsonArray getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(JsonArray subCategory) {
        SubCategory = subCategory;
    }
}

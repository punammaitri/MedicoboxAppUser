package com.aiprous.medicobox.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

public class MainCategoryModel  implements Serializable {

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
    ArrayList<SubCat> subCats;
   // public JSONArray SubCategory;


    public MainCategoryModel(String categoryName, String categoryId, String image_url, ArrayList<SubCat> subCats) {
        this.categoryName = categoryName;
        CategoryId = categoryId;
        this.image_url = image_url;
        this.subCats = subCats;
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

    public ArrayList<SubCat> getSubCats() {
        return subCats;
    }

    public void setSubCats(ArrayList<SubCat> subCats) {
        this.subCats = subCats;
    }

    public static class SubCat{
        @Expose
        @SerializedName("categoryName")
        public String categoryName;
        @Expose
        @SerializedName("CategoryId")
        public String CategoryId;
        @Expose
        @SerializedName("image_url")
        public String image_url;

        public SubCat(String categoryName, String categoryId, String image_url) {
            this.categoryName = categoryName;
            CategoryId = categoryId;
            this.image_url = image_url;
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
    }



}

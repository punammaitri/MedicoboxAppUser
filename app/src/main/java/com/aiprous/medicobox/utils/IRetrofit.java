package com.aiprous.medicobox.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Jitesh on 12/10/2018.
 */

public interface IRetrofit {

    //for user login
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("login.php")
    Call<JsonObject> userLogin(@Body JsonObject jsonObject);

    //for user registration
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("customers")
    Call<JsonObject> userRegistration(@Body JsonObject jsonObject);

    //for user email availability
    @POST("isEmailAvailable")
    Call<JsonPrimitive> emailAvailable(@Body JsonObject jsonObject);

    //for user key confirmation
    @POST("activate")
    Call<JsonObject> keyConfirmation(@Body JsonObject jsonObject);

    //for getting product
    @GET("featured-products.php")
    Call<JsonArray> getProductList();

    //for getting Categories
    @GET("categories.php")
    Call<JsonObject> getCategories();

    //for user registration
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("products.php")
    Call<JsonObject> getProducts(@Body JsonObject jsonObject);

    //for getting product
    @GET("home-banners.php")
    Call<JsonObject> getBannerList();

    //for getting login token
    @GET("me")
    Call<JsonObject> getAuthorizeToken( @Header("Authorization") String authHeader);

}
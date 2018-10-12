package com.aiprous.medicobox.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Jitesh on 12/10/2018.
 */

public interface IRetrofit {

    //for user registration
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("customers")
    Call<JsonObject> userRegistration(@Body JsonObject jsonObject);

    //for user login
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("token")
    Call<JsonPrimitive> userLogin(@Body JsonObject jsonObject);

    //for getting product
    @GET("featured-products.php")
    Call<JsonArray> getProductList();
}
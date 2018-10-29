package com.aiprous.medicobox.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by **
 *  * Created by Jitesh on 29/10/2018.
 *  */

public interface IRetrofit {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("login.php")
    Call<JsonObject> postRawJSON(@Body JsonObject jsonObject);


}

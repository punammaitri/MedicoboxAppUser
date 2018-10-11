package com.aiprous.medicobox.register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("customers")
    @FormUrlEncoded
    Call<RegisterModel> signUpData(@Field("json") String customer);
}

package com.aiprous.medicobox.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @POST("token")
    @FormUrlEncoded
    Call<LoginModel> loginData(@Field("username") String username,
                               @Field("password") String password);
}

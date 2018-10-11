package com.aiprous.medicobox.fragment;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FeatureService {
    @POST("customers")
    @FormUrlEncoded
    Call<FeatureModel> signUpData(@Field("json") String customer);
}

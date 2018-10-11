package com.aiprous.medicobox.fragment;

import android.content.Context;

import com.aiprous.medicobox.utils.APICallback;
import com.aiprous.medicobox.utils.APIServiceFactory;
import com.aiprous.medicobox.utils.BaseServiceResponseModel;
import com.aiprous.medicobox.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeatureServiceProvider {
    private final FeatureService featureService;

    public FeatureServiceProvider(Context context) {
        featureService = APIServiceFactory.createService(FeatureService.class, context);
    }

    public void signUpData(String customer, final APICallback apiCallback) {
        Call<FeatureModel> call = null;
        call = featureService.signUpData(customer);
        String url = call.request().url().toString();

        call.enqueue(new Callback<FeatureModel>() {
            @Override
            public void onResponse(Call<FeatureModel> call, Response<FeatureModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 500) {
                    apiCallback.onSuccess(response.body());
                } else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<FeatureModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
package com.aiprous.medicobox.register;

import android.content.Context;

import com.aiprous.medicobox.utils.APICallback;
import com.aiprous.medicobox.utils.APIServiceFactory;
import com.aiprous.medicobox.utils.BaseServiceResponseModel;
import com.aiprous.medicobox.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterServiceProvider {
    private final RegisterService registerService;

    public RegisterServiceProvider(Context context) {
        registerService = APIServiceFactory.createService(RegisterService.class, context);
    }

    public void signUpData(String customer, final APICallback apiCallback) {
        Call<RegisterModel> call = null;
        call = registerService.signUpData(customer);
        String url = call.request().url().toString();

        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
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
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
package com.aiprous.medicobox.login;

import android.content.Context;

import com.aiprous.medicobox.utils.APICallback;
import com.aiprous.medicobox.utils.APIServiceFactory;
import com.aiprous.medicobox.utils.BaseServiceResponseModel;
import com.aiprous.medicobox.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginServiceProvider {
    private final LoginService loginService;

    public LoginServiceProvider(Context context) {
        loginService = APIServiceFactory.createService(LoginService.class, context);
    }

    public void loginData(String username, String password, final APICallback apiCallback) {
        Call<LoginModel> call = null;
        call = loginService.loginData(username, password);
        String url = call.request().url().toString();

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
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
            public void onFailure(Call<LoginModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
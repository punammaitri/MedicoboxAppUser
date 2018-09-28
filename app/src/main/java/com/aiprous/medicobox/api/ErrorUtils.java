package com.aiprous.medicobox.api;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by vishal.jadhav on 6/9/2017.
 */
@SuppressWarnings("ALL")
public class ErrorUtils {
    public static BaseServiceResponseModel parseError(@NonNull Response<?> response) {
        BaseServiceResponseModel error;
        try {
            Converter<ResponseBody, BaseServiceResponseModel> converter = APIServiceFactory.retrofit.responseBodyConverter(BaseServiceResponseModel.class, new Annotation[0]);
            if (response.errorBody() != null)
                error = converter.convert(response.errorBody());
            else {
                error = (BaseServiceResponseModel) response.body();

            }
        } catch (IOException e) {
            return new BaseServiceResponseModel();
        }

        return error;
    }
}


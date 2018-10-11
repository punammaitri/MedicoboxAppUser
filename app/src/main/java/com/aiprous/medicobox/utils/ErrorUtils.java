package com.aiprous.medicobox.utils;


import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by v.jadhav on 6/9/2017.
 */
@SuppressWarnings("ALL")
public class ErrorUtils {
    public static BaseServiceResponseModel parseError(Response<?> response) {
        BaseServiceResponseModel error;
        try {
            Converter<ResponseBody, BaseServiceResponseModel> converter = APIServiceFactory.retrofit.responseBodyConverter(BaseServiceResponseModel.class, new Annotation[0]);
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new BaseServiceResponseModel();
        }

        return error;
    }
}
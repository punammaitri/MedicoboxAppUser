package com.aiprous.deliveryboy.api;

import android.support.annotation.NonNull;

import com.aiprous.deliveryboy.utils.Constant;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ALL")
public class BaseServiceResponseModel {
    @NonNull
    @SerializedName("Message")
    private String Message = Constant.SOME_THING_WENT_WRONG;
    @SerializedName("StatusCode")
    private int StatusCode;

    @NonNull
    public String getMessage() {
        return Message;
    }

    public int getStatusCode() {
        return StatusCode;
    }
}

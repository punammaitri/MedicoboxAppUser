package com.aiprous.medicobox.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER1 on 7/4/2017.
 */

public class BaseServiceResponseModel {
    @SerializedName("status")
    public int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    public String message;

}



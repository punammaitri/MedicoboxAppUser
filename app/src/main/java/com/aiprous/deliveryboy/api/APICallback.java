package com.aiprous.deliveryboy.api;

/**
 * Created by androidu1 on 20/11/16.
 */
@SuppressWarnings("ALL")
public interface APICallback {
    //testing
    <T> void onSuccess(T serviceResponse);

    <T> void onFailure(T apiErrorModel, T extras);
}

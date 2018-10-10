package com.aiprous.deliveryboy.api;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.aiprous.deliveryboy.api.APIConstant.SERVER_URL;


public class APIServiceFactory {

    @NonNull
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()

            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(4, TimeUnit.MINUTES);
    @NonNull
    private static Retrofit.Builder builder = new Retrofit
            .Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());
    @NonNull
    static Retrofit retrofit = builder.build();

    public static <S> S createService(@NonNull Class<S> serviceClass) {
        OkHttpClient client = httpClient.build();

        Retrofit retrofit = builder.client(client).addConverterFactory(
                GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .create())).build();
        return retrofit.create(serviceClass);
    }
}
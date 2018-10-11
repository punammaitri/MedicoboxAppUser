package com.aiprous.medicobox.utils;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.aiprous.medicobox.utils.Constant.SERVER_URL;

/**
 * Created by Gaurang on 6/22/2016.
 */
public class APIServiceFactory {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS);

    private static Retrofit.Builder builder = new Retrofit
            .Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());
    static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass, final Context context) {
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).addConverterFactory(
                GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .create())).build();
        return retrofit.create(serviceClass);
    }
}
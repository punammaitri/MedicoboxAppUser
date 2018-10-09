package com.aiprous.deliveryboy.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class DeliveryBoyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

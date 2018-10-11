package com.aiprous.medicobox.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aiprous.medicobox.R;

public class MedicoboxApp extends MultiDexApplication {

    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        //ACRA.init(this);
        mContext = getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
    }


    public static Context getContext() {
        return mContext;
    }

    public static void onSetStarForFifthScreen(float q21, float q22) {
        SharedPreferences.Editor lEditor = mSharedPreferences.edit();
        lEditor.putFloat("Q21", q21);
        lEditor.putFloat("Q22", q22);

        lEditor.commit();
    }


    public static Float onGetQ21Star() {
        return mSharedPreferences.getFloat("Q21", 0);
    }

    public static Float onGetQ22Star() {
        return mSharedPreferences.getFloat("Q22", 0);
    }
}
package com.aiprous.medicobox.application;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static void onSaveLoginDetail(String id, String firstname, String lastname, String mobile_number, String email) {

        SharedPreferences.Editor edt = mSharedPreferences.edit();
        edt.putString("ID", id);
        edt.putString("FIRSTNAME", firstname);
        edt.putString("LASTNAME", lastname);
        edt.putString("MOBILENO", mobile_number);
        edt.putString("EMAIL", email);
        edt.commit();
    }

    public static String onGetId() {
        return mSharedPreferences.getString("ID", "");
    }

    public static String onGetFirstName() {
        return mSharedPreferences.getString("FIRSTNAME", "");
    }

    public static String onGetLastName() {
        return mSharedPreferences.getString("LASTNAME", "");
    }

    public static String onGetMobileNo() {
        return mSharedPreferences.getString("MOBILENO", "");
    }

    public static String onGetEmail() {
        return mSharedPreferences.getString("EMAIL", "");
    }
}
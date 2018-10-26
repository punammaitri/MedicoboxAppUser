package com.aiprous.medicobox.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
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

    public static void onSaveLoginDetail(String id,String
             authToken,String firstname, String lastname, String mobile_number, String email) {

        SharedPreferences.Editor edt = mSharedPreferences.edit();
        edt.putString("ID", id);
        edt.putString("AUTHTOKEN", authToken);
        edt.putString("FIRSTNAME", firstname);
        edt.putString("LASTNAME", lastname);
        edt.putString("MOBILENO", mobile_number);
        edt.putString("EMAIL", email);
        edt.commit();
    }


    public static String onGetId() {
        return mSharedPreferences.getString("ID", "");
    }

    public static String onGetAuthToken() {
        return mSharedPreferences.getString("AUTHTOKEN", "");
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


    public static void onSaveCity(String city) {

        SharedPreferences.Editor edt = mSharedPreferences.edit();
        edt.putString("CITY", city);
        edt.commit();
    }

    public static String onGetCity() {
        return mSharedPreferences.getString("CITY", "");
    }

    public static void onSaveLatiLong(String latlong) {
        SharedPreferences.Editor lEditor = mSharedPreferences.edit();
        lEditor.putString("LATILONG", latlong);
        lEditor.commit();
    }

    @Nullable
    public static String getLatiLong() {
        return mSharedPreferences.getString("LATILONG", "");
    }


    public static void onSaveCartId(String cartId) {

        SharedPreferences.Editor edt = mSharedPreferences.edit();
        edt.putString("CARDID", cartId);
        edt.commit();
    }

    public static String onGetCartID() {
        return mSharedPreferences.getString("CARDID", "");
    }

}
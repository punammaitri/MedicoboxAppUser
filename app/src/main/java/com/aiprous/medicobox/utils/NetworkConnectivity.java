package com.aiprous.medicobox.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aiprous.medicobox.application.MedicoboxApp;

public class NetworkConnectivity {

    private static Context mContext = MedicoboxApp.getContext();
    private static ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

    public static boolean isOnline() {

        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
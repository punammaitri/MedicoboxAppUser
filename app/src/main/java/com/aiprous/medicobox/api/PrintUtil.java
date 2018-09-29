package com.aiprous.medicobox.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.Constant;
import com.aiprous.medicobox.utils.CustomProgressDialog;


public class PrintUtil {

    public static void showToast(Context context, String msg) {
        BaseActivity.showToast(context, msg);
    }

    public static String showNetworkAvailableToast(@NonNull Context context) {
        try {
            if (BaseActivity.isNetworkAvailable(context)) {
                String msg = "";
                if (!BaseActivity.isNetworkAccess(context)) {
                    CustomProgressDialog.getInstance().showDialog(context, Constant.Network_Error, Constant.ERROR_TYPE);
                } else {
                    msg = context.getResources().getString(R.string.error_server);
                    BaseActivity.showToast(context, msg);
                }
                return msg;
            } else {
                CustomProgressDialog.getInstance().showDialog(context, Constant.Network_Error, Constant.ERROR_TYPE);
                return "";//context.getResources().getString(R.string.error_internet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return context.getResources().getString(R.string.error_server);
        }
    }

    //---Function to check network connection---//
    public static boolean isNetworkAvailable(@NonNull Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
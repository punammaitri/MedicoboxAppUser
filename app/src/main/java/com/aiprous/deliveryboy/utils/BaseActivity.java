package com.aiprous.deliveryboy.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.deliveryboy.R;
import com.aiprous.deliveryboy.api.APIConstant;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class BaseActivity extends AppCompatActivity {

    /* show toast message to user */
    public static void showToast(Context context, String message) {
        View inflatedView = View.inflate(context, R.layout.toast_layout, null);
        Toast myToast = new Toast(context);
       // Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/" + context.getResources().getString(R.string.font_family_regular));
        TextView textView = inflatedView.findViewById(R.id.textView);
      //  textView.setTypeface(face);
        textView.setText(message);
        myToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, APIConstant.SUCCESS_CODE);
        myToast.setDuration(Toast.LENGTH_SHORT);
        myToast.setView(inflatedView);
        myToast.show();
    }

    public static boolean isNetworkAccess(Context context) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1000);
                    urlc.connect();
                    return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
                } catch (java.net.SocketTimeoutException e) {
                    return false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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


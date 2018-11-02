package com.aiprous.medicobox.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final boolean DEBUG_ENABLE = true;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.app_name));
    }

    public static void printLog(String tag, String message) {
        if (DEBUG_ENABLE) {
            Log.d(tag, message);
        }
    }

    public void printError(String tag, String message, Exception e) {
        if (DEBUG_ENABLE) {
            Log.e(tag, message, e);
        }
    }

    public static void hideKeyboard(@NonNull Context context, @Nullable View view) {
        // Check if no view has focus:
        //View view = context.get
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /* check email id is valid or not */
    public static boolean isValidEmailId(EditText editText) {
        String text = editText.getText().toString().trim();
        if (!Pattern.matches(EMAIL_REGEX, text)) {
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    /* show toast message to user */
    public static void showToast(Context context, String message) {
        View inflatedView = View.inflate(context, R.layout.toast_layout, null);
        Toast myToast = new Toast(context);
        TextView textView = inflatedView.findViewById(R.id.textView);
        textView.setText(message);
        myToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, APIConstant.SUCCESS_CODE);
        myToast.setDuration(Toast.LENGTH_SHORT);
        myToast.setView(inflatedView);
        myToast.show();
    }

    /*password validator*/
    public static boolean passwordValidation(Context mContext, String password, EditText edtPassword) {
        boolean valid = true;
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[,~,!,@,#,$,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";

        if (password.length() <= 7) {
            edtPassword.setError("Password should be more than 7 characters.");
            valid = false;
        } else if (!password.matches(upperCaseChars)) {
            edtPassword.setError("Password should contain atleast one upper case letter");
            valid = false;
        } else if (!password.matches(lowerCaseChars)) {
            edtPassword.setError("Password should contain atleast one lower case letter");
            valid = false;
        } else if (!password.matches(numbers)) {
            edtPassword.setError("Password should contain atleast one number.");
            valid = false;
        } else if (!password.matches(specialChars)) {
            edtPassword.setError("Password should contain atleast one special character");
            valid = false;
        }
        return valid;
    }

    /*Change Status bar color*/
    public static void changeStatusBarColor(Activity mActivity) {
        //set status bar color
        Window window = mActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        }
    }

    /*Change Status bar color*/
    public static void firebaseAnalytics(String mActivityName) {
        FirebaseCrash.logcat(Log.ERROR,mActivityName,"Firebase Crash Analytics");
        Throwable exception = null;
        FirebaseCrash.report(exception);
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

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}


package com.aiprous.medicobox.api;
/*
 * Created by v.jadhav on 05-01-2018.
 */

import android.support.annotation.NonNull;

public class APIConstant {

    @NonNull
    public static final String SERVER_URL = "http://staging.medicobox.com";

    //TODO Please replace this url for image show while app up on production

    @NonNull
    //These URL for production
    public static final String GetAllergies = "M_PatientEMR/GetAllergies";
    public static final String Network_Error = "Network is not available. Establish network connection.";
    public static final String SOME_THING_WENT_WRONG = "Oops! Something went wrong!";


    //Response Status Code
    public static final int SUCCESS_CODE = 200;

}

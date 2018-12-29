package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.FORGOT_PASSWORD_VERIFY_OTP;
import static com.aiprous.medicobox.utils.APIConstant.GETUSERINFO;
import static com.aiprous.medicobox.utils.APIConstant.SIGN_IN_WITH_OTP_VERIFY_OTP;



public class OTPActivity extends AppCompatActivity {

    @BindView(R.id.btn_otp_proceed)
    Button btn_otp_proceed;
    @BindView(R.id.edt_one)
    EditText edt_one;
    @BindView(R.id.edt_two)
    EditText edt_two;
    @BindView(R.id.edt_there)
    EditText edt_there;
    @BindView(R.id.edt_four)
    EditText edt_four;
    private Context mContext=this;
    private String mMobileNumber;
    private String mFlag;
    private String mToken;
    private String getMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        if (getIntent().getStringExtra("mobilenumber") != null) {
            mMobileNumber = getIntent().getStringExtra("mobilenumber");
        }


            edt_one.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {

                    if (s.length() == 1) {
                        edt_two.requestFocus();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });

            edt_two.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        edt_there.requestFocus();
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
            edt_there.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        edt_four.requestFocus();
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });

    }

    @OnClick(R.id.btn_otp_proceed)
    public void onClickOtpProceesd() {
        String lOne = edt_one.getText().toString();
        String ltwo = edt_two.getText().toString();
        String lThree = edt_there.getText().toString();
        String lfour = edt_four.getText().toString();
        String lOTP=lOne+ltwo+lThree+lfour;
        if (lOne.length() == 0 && ltwo.length() == 0 && lThree.length() == 0 && lfour.length() == 0) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
        } else {

            JSONObject jsonObjectOTP = new JSONObject();
            try {
                jsonObjectOTP.put("mobile", mMobileNumber);
                jsonObjectOTP.put("otp",lOTP);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(getIntent().getStringExtra("flag")!=null)
            {
                mFlag=getIntent().getStringExtra("flag");
                if(mFlag.equalsIgnoreCase("SignWithOTP"))
                {
                    //call API to sign In With Otp verify otp
                    SignInWithOTPVerifyOTP(jsonObjectOTP);
                }else if(mFlag.equalsIgnoreCase("forgotpassword")){
                    //call verify forgot password otp
                    forgotPasswordVerifyOTP(jsonObjectOTP);
                }
            }


        }
    }


    private void SignInWithOTPVerifyOTP(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(SIGN_IN_WITH_OTP_VERIFY_OTP)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            JSONObject responseJSONObject=jsonResponse.getJSONObject("response");
                            String status=responseJSONObject.get("status").toString();
                            String message=responseJSONObject.get("message").toString();

                            if(status.equalsIgnoreCase("success"))
                            {
                                 mToken=responseJSONObject.get("response_token").toString();
                                 //call get profile API
                                getUserInfo(mToken);

                               /* Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, MainActivity.class));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();*/
                            }else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                           // CustomProgressDialog.getInstance().dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                            /*startActivity(new Intent(MobileNumberActivity.this, ForgotPasswordActivity.class));
                            finish();*/
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    private void forgotPasswordVerifyOTP(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(FORGOT_PASSWORD_VERIFY_OTP)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            JSONObject responseJSONObject=jsonResponse.getJSONObject("response");
                            String status=responseJSONObject.get("status").toString();
                            String message=responseJSONObject.get("message").toString();

                            if(status.equalsIgnoreCase("success"))
                            {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, ForgotPasswordActivity.class).putExtra("MobileNumber",mMobileNumber));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                            /*startActivity(new Intent(MobileNumberActivity.this, ForgotPasswordActivity.class));
                            finish();*/
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    private void getUserInfo(final String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(GETUSERINFO)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {

                            /*JSONObject jsonObject = new JSONObject(response.getString("response"));
                            String getId = jsonObject.get("id").toString();
                            String getGroupId = jsonObject.get("group_id").toString();
                            String getEmail = jsonObject.get("email").toString();
                            String getFirstname = jsonObject.get("firstname").toString();
                            String getLastname = jsonObject.get("lastname").toString();
                            String getStoreId = jsonObject.get("store_id").toString();
                            String getWebsiteId = jsonObject.get("website_id").toString();
                            String getMobile = jsonObject.get("mobile").toString();

                            MedicoboxApp.onSaveLoginDetail(getId, bearerToken, getFirstname, getLastname, getMobile, getEmail, getStoreId);
                            Toast.makeText(mContext, "Login successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OTPActivity.this, MainActivity.class)
                                    .putExtra("email", "" + getEmail));
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();*/


                             JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                             JsonObject responseObject = getAllResponse.get("response").getAsJsonObject();
                             String status = responseObject.get("status").getAsString();
                             if (status.equals("success")) {
                             JsonObject responseObjects = responseObject.get("data").getAsJsonObject();
                             String getId = responseObjects.get("id").getAsString();
                             String getGroupId = responseObjects.get("group_id").getAsString();
                             String getEmail = responseObjects.get("email").getAsString();
                             String getFirstname = responseObjects.get("firstname").getAsString();
                             String getLastname = responseObjects.get("lastname").getAsString();
                             String getStoreId = responseObjects.get("store_id").getAsString();
                             String getWebsiteId = responseObjects.get("website_id").getAsString();
                             JsonArray custom_attributes_array = responseObjects.get("custom_attributes").getAsJsonArray();

                                 if (custom_attributes_array != null) {
                                     for (int j = 0; j < custom_attributes_array.size(); j++) {
                                         JsonObject customObject = custom_attributes_array.get(j).getAsJsonObject();
                                         getMobileNumber = customObject.get("value").getAsString();
                                       }
                                 }
                                 MedicoboxApp.onSaveLoginDetail(getId, bearerToken, getFirstname, getLastname, getMobileNumber, getEmail, getStoreId);
                                 Toast.makeText(mContext, "Login successfully", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(OTPActivity.this, MainActivity.class)
                                         .putExtra("email", "" + getEmail));
                                         overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                         finish();
                             }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(OTPActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error",
                                "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


}

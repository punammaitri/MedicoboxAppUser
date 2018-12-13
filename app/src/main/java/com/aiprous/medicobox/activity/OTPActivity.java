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
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.FORGOT_PASSWORD_VERIFY_OTP;
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
                                //need to send token to next screen
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(mContext, MainActivity.class));
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


}

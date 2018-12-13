package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static com.aiprous.medicobox.utils.APIConstant.FORGOT_PASSWORD_VERIFY_MOBILE_NUMBER;
import static com.aiprous.medicobox.utils.APIConstant.ISEMAILAVAILABLE;
import static com.aiprous.medicobox.utils.APIConstant.SIGN_IN_WITH_OTP_VERIFY_MOBILE_NUMBER;



public class MobileNumberActivity extends AppCompatActivity {

    @BindView(R.id.btn_proceed)
    Button btn_proceed;
    @BindView(R.id.edt_email_or_mobile)
    EditText edt_email_or_mobile;
    private String lMobile_email;
    CustomProgressDialog mAlert;
    private Context mContext = this;
    private String mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        mAlert = CustomProgressDialog.getInstance();

    }


    @OnClick(R.id.btn_proceed)
    public void onClickProceed() {

        lMobile_email = edt_email_or_mobile.getText().toString().trim();

        JSONObject jsonObjectMobileNumber = new JSONObject();
        try {
            jsonObjectMobileNumber.put("mobile", lMobile_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(getIntent().getStringExtra("flag")!=null)
        {
            mFlag=getIntent().getStringExtra("flag");
            if(mFlag.equalsIgnoreCase("SignWithOTP"))
            {
                //call API to  sign In With Otp verify mobile number
                signInWithOtpVerifyMobileNumber(jsonObjectMobileNumber);
            }else if(mFlag.equalsIgnoreCase("forgotpassword")){
                //Call API for FORGOT_PASSWORD_VERIFY_MOBILE_NUMBER
                forgotPasswordVerifyMobileNo(jsonObjectMobileNumber);
            }
        }




        //call verify email
        /*lEmail = edt_email_or_mobile.getText().toString().trim();

        if (lMobile_email.length() > 0) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("customerEmail", lMobile_email);
                jsonObject.put("websiteId", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!isNetworkAvailable(this)) {
                CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
            } else {
                verifyEmail(jsonObject);
            }

        } else if (lMobile_email.length() == 0) {
            showToast(this, getResources().getString(R.string.error_email));
        }*/

    }



    private void verifyEmail(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(ISEMAILAVAILABLE)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {

                            JSONObject jsonResponse = new JSONObject(response.toString());
                            boolean success = jsonResponse.getBoolean("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();
                        startActivity(new Intent(MobileNumberActivity.this, ForgotPasswordActivity.class));
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
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


    private void signInWithOtpVerifyMobileNumber(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(SIGN_IN_WITH_OTP_VERIFY_MOBILE_NUMBER)
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
                                startActivity(new Intent(mContext,OTPActivity.class).putExtra("mobilenumber",edt_email_or_mobile.getText().toString()).putExtra("flag","SignWithOTP"));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();

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


    private void forgotPasswordVerifyMobileNo(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(FORGOT_PASSWORD_VERIFY_MOBILE_NUMBER)
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
                                startActivity(new Intent(mContext,OTPActivity.class).putExtra("mobilenumber",edt_email_or_mobile.getText().toString()).putExtra("flag","forgotpassword"));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();

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

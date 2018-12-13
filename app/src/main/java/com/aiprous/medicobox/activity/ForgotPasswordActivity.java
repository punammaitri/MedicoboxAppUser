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

import static com.aiprous.medicobox.utils.APIConstant.CONFIRMKEY;
import static com.aiprous.medicobox.utils.APIConstant.FORGOT_PASSWORD_VERIFY_OTP;
import static com.aiprous.medicobox.utils.APIConstant.SET_NEW_PASSWORD;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_set_password)
    Button btn_set_password;
    CustomProgressDialog mAlert;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    private Context mContext = this;
    private String mMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        mAlert = CustomProgressDialog.getInstance();

        if (getIntent().getStringExtra("MobileNumber") != null) {
            mMobileNumber = getIntent().getStringExtra("MobileNumber");
        }

        //Add parameters to json for confirmation Key
        JSONObject jsonObjectconfirmationKey = new JSONObject();
        try {
            jsonObjectconfirmationKey.put("confirmationKey", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

   /* private void AttemptToConfirmKey(JSONObject jsonObject) {

        AndroidNetworking.post(CONFIRMKEY)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(ForgotPasswordActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }
*/

    @OnClick(R.id.btn_set_password)
    public void onClickSetPassword() {

        String lPassword = edt_password.getText().toString();
        String lConfirm_Password = edt_confirm_password.getText().toString();

        if (lPassword.length() == 0) {
            edt_password.setError("Please New Password");
        } else if (lConfirm_Password.length() == 0) {
            edt_confirm_password.setError("Please confirm password");
        }else if(!lPassword.equals(lConfirm_Password))
        {
            Toast.makeText(mContext, "Password mismatch", Toast.LENGTH_SHORT).show();
        }
        else {
            //Add parameters to json for set new password
            JSONObject jsonSetNewPassword = new JSONObject();
            try {
                jsonSetNewPassword.put("mobile", mMobileNumber);
                jsonSetNewPassword.put("password",lPassword );
                jsonSetNewPassword.put("confirm_password",lConfirm_Password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!isNetworkAvailable(this)) {
                CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
            }else {
                //AttemptToConfirmKey(jsonObject);
                setNewPassword(jsonSetNewPassword);
            }


        }
    }


    private void setNewPassword(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        AndroidNetworking.post(SET_NEW_PASSWORD)
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
                                startActivity(new Intent(mContext, LoginActivity.class));
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

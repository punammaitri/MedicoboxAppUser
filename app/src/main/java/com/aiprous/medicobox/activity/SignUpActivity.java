package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.REGISTER;
import static com.aiprous.medicobox.utils.APIConstant.SEND_SMS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.isValidEmailId;
import static com.aiprous.medicobox.utils.BaseActivity.passwordValidation;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.tv_sign_in_here)
    TextView tv_sign_in_here;
    CustomProgressDialog mAlert;
    @BindView(R.id.edt_firstname)
    EditText edt_first_name;
    @BindView(R.id.edt_last_name)
    EditText edt_last_name;
    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    @BindView(R.id.edt_mobile)
    EditText edtMobile;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    Context mContext = this;
    private String lFirstname, lLastname, lEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        mAlert = CustomProgressDialog.getInstance();

        if (getIntent().getStringExtra("firstname") != null) {
            lFirstname = getIntent().getStringExtra("firstname");
            lLastname = getIntent().getStringExtra("lastname");
            lEmail = getIntent().getStringExtra("email");

            edt_first_name.setText(lFirstname);
            edt_last_name.setText(lLastname);
            edtEmail.setText(lEmail);
        }
    }

    @Override
    protected void onResume() {
        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        }
        super.onResume();
    }

    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp() {

        String lFirstname = edt_first_name.getText().toString().trim();
        String lLastname = edt_last_name.getText().toString().trim();
        String lMobile = edtMobile.getText().toString().trim();
        String lEmail = edtEmail.getText().toString().trim();
        String lPass = edtPassword.getText().toString().trim();
        String lConfirm_password = edt_confirm_password.getText().toString().trim();

        if (lFirstname.length() <= 2) {
            edt_first_name.setError("Firstname must be greater than 2 character");
        } else if (lLastname.length() <= 2) {
            edt_last_name.setError("Lastname must be greater than 2 character");
        } else if (lMobile.length() <= 9) {
            edtMobile.setError("Mobile number must be  10 digit");
        } else if (!isValidEmailId(edtEmail)) {
            edtEmail.setError("Invalid email address");
        } else if (!passwordValidation(mContext, lPass, edtPassword)) {
            Toast.makeText(mContext, "Password should contains one uppercase,lowercase,special character,number & should be greater than 7 character .", Toast.LENGTH_LONG).show();
        } else if (!lPass.equals(lConfirm_password)) {
            Toast.makeText(mContext, "Password mismatch", Toast.LENGTH_SHORT).show();
        } else {
            try {

                //Add Json Object
                JSONObject jsonObjectAttribute = new JSONObject();
                jsonObjectAttribute.put("attributeCode", "mobile_number");
                jsonObjectAttribute.put("value", lMobile);

                JSONArray jsonArrayAttribute = new JSONArray();
                jsonArrayAttribute.put(jsonObjectAttribute);

                JSONObject objCustomer = new JSONObject();
                objCustomer.put("email", lEmail);
                objCustomer.put("firstname", lFirstname);
                objCustomer.put("lastname", lLastname);
                objCustomer.put("storeId", 1);
                objCustomer.put("customAttributes", jsonArrayAttribute);

                JSONObject jsonObjectReg = new JSONObject();
                jsonObjectReg.put("customer", objCustomer);
                jsonObjectReg.put("password", lPass);

                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
                    AttemptToRegister(jsonObjectReg, lMobile, lEmail, lPass);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void AttemptToRegister(final JSONObject jsonObject, final String lMobile, final String lEmail, final String lPass) {
        AndroidNetworking.post(REGISTER)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //for registration response
                        CustomProgressDialog.getInstance().dismissDialog();
                        try {
                            JSONObject jsonResponse = new JSONObject(response.getString("response"));

                            if (!jsonResponse.isNull("message")) {
                                String errorMessage = jsonResponse.getString("message");
                                Toast.makeText(mContext, "" + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                CallSmsAPI(lMobile, lEmail, lPass);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void CallSmsAPI(String lMobile, String lEmail, String lPass) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        //jsonArray = new JSONArray(mStreetArray);
        JSONObject jsonObject = new JSONObject();
        try {
            if (lMobile.contains("+") || lMobile.contains("91")) {
                jsonObject.put("mobile_number", lMobile);
                jsonObject.put("message", "Thanks for registration." + "\n" + "Team Medicobox");
            } else {
                jsonObject.put("mobile_number", "91" + lMobile);
                jsonObject.put("message", "Thanks for registration with MedicoBox your email is  " + lEmail + " and password is " + lPass + "\n" + "Team medicoBox .");
            }
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SEND_SMS)
                .addJSONObjectBody(jsonObject) // posting json
                // .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mContext, "Registration Successful and SMS sent to your register mobile number ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        //Toast.makeText(MyOrdersActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    @OnClick(R.id.tv_sign_in_here)
    public void onClickSignInHere() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }
}
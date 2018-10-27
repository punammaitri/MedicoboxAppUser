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
import com.aiprous.medicobox.register.RegisterModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.REGISTER;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.passwordValidation;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.tv_sign_in_here)
    TextView tv_sign_in_here;
    CustomProgressDialog mAlert;
    @BindView(R.id.edt_first_name)
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
    public ArrayList<String> mTempArray = new ArrayList<String>();
    Context mContext = this;
    private RegisterModel detailModelArrayList;

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
    }

    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp() {

        String lName = edt_first_name.getText().toString().trim();
        String lMobile = edtMobile.getText().toString().trim();
        String lEmail = edtEmail.getText().toString().trim();
        String lPass = edtPassword.getText().toString().trim();
        String lConfirm_password = edt_confirm_password.getText().toString().trim();

        String emailPattern = "[A-Za-z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (edt_first_name.getText().length() <= 2) {
            edt_first_name.setError("Name must be greater than 2 character");
        } else if (edtMobile.getText().length() <= 9) {
            edtMobile.setError("Mobile number must be greater 10 digit");
        } else if (!lEmail.matches(emailPattern)) {
            edtEmail.setError("Invalid email address");
        } else if (!lPass.equals(lConfirm_password)) {
            Toast.makeText(mContext, "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
        } else if (passwordValidation(mContext, lPass, edtPassword)) {

            try {
                JSONObject object = new JSONObject();
                object.put("id", 0);
                object.put("groupId", 0);
                object.put("defaultBilling", "string");
                object.put("defaultShipping", "string");
                object.put("confirmation", "string");
                object.put("createdAt", "string");
                object.put("updatedAt", "string");
                object.put("createdIn", "string");
                object.put("dob", "string");
                object.put("email", lEmail);
                object.put("firstname", lName);
                object.put("lastname", lName);
                object.put("middlename", "string");
                object.put("prefix", "string");
                object.put("suffix", "string");
                object.put("gender", 0);
                object.put("storeId", 0);
                object.put("taxvat", "string");
                object.put("websiteId", 1);
                object.put("addresses", "");
                object.put("disableAutoGroupChange", 0);
                object.put("extensionAttributes", "");
                object.put("customAttributes", "");

                //Add Json Object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("customer", object);
                jsonObject.put("password", lPass);
                jsonObject.put("redirectUrl", "string");
                jsonObject.put("mobile", lMobile);

                AttemptToRegister(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void AttemptToRegister(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            AndroidNetworking.post(REGISTER)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                String getId = String.valueOf(response.get("id"));
                                String getFirstname = String.valueOf(response.get("firstname"));
                                String getLastname = String.valueOf(response.get("lastname"));
                                String getEmail = String.valueOf(response.get("email"));
                                String getStoreId = String.valueOf(response.get("store_id"));

                                BaseActivity.printLog("response-success : ", response.toString());
                                CustomProgressDialog.getInstance().dismissDialog();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class)
                                        .putExtra("id", "" + getId)
                                        .putExtra("firstname", "" + getFirstname)
                                        .putExtra("lastname", "" + getLastname)
                                        .putExtra("email", "" + getEmail));

                                MedicoboxApp.onSaveLoginDetail(getId, "", getFirstname, getLastname, "", getEmail,getStoreId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    @OnClick(R.id.tv_sign_in_here)
    public void onClickSignInHere() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

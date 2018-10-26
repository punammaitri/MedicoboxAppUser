package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
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

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETBEARERTOKEN;
import static com.aiprous.medicobox.utils.APIConstant.UPDATEUSERINFO;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R.id.edt_last_name)
    EditText edtLastName;
    @BindView(R.id.edt_mobile_no)
    EditText edtMobileNo;
    @BindView(R.id.edt_email_id)
    EditText edtEmailId;
    @BindView(R.id.edt_gender)
    EditText edtGender;
    @BindView(R.id.edt_dob)
    EditText edtDob;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    CustomProgressDialog mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAlert = CustomProgressDialog.getInstance();
        GetProfileInfoUsingAuthKey(MedicoboxApp.onGetAuthToken());

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave() {
        String lfirst_name = edtFirstName.getText().toString();
        String lLast_Name = edtLastName.getText().toString();
        String lMobile_no = edtMobileNo.getText().toString();
        String lEmail_id = edtEmailId.getText().toString();
        String lDob = edtDob.getText().toString();
        String lPassword = edtPassword.getText().toString();
        String lConfirm_password = edtConfirmPassword.getText().toString();

        if (lfirst_name.length() == 0) {
            edtFirstName.setError("Please enter first name");
        } else if (lLast_Name.length() == 0) {
            edtLastName.setError("Please enter last name");
        } else if (lMobile_no.length() == 0) {
            edtMobileNo.setError("Please enter mobile number");
        } else if (lEmail_id.length() == 0) {
            edtEmailId.setError("Please enter email id");
        } else if (lPassword.length() == 0) {
            edtPassword.setError("Please enter password");
        } else if (lConfirm_password.length() == 0) {
            edtConfirmPassword.setError("Please enter confirm password");
        } else if (!lPassword.equals(lConfirm_password)) {
            Toast.makeText(this, "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
        } else {
            //API call

            try {
                JSONObject object = new JSONObject();
                object.put("confirmation", "");
                object.put("dob", "");
                object.put("email", lEmail_id);
                object.put("firstname", lfirst_name);
                object.put("lastname", lLast_Name);
                object.put("middlename", "");
                object.put("prefix", "");
                object.put("suffix", "");
                object.put("gender", 1);
                object.put("storeId", MedicoboxApp.onGetStoreId());
                object.put("taxvat", "");
                object.put("websiteId", "");

                //Add Json Object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("customer", object);

                UpdateProfileInfo(jsonObject,MedicoboxApp.onGetAuthToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void GetProfileInfoUsingAuthKey(final String bearerToken) {

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            mAlert.onShowProgressDialog(EditProfileActivity.this, true);
            AndroidNetworking.get(GETBEARERTOKEN)
                    .addHeaders(Authorization, BEARER + bearerToken)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                String getId = response.getString("id");
                                String getGroupId = response.getString("group_id");
                                String getEmail = response.getString("email");
                                String getFirstname = response.getString("firstname");
                                String getLastname = response.getString("lastname");
                                String getStoreId = response.getString("store_id");
                                String getWebsiteId = response.getString("website_id");

                                edtFirstName.setText(getFirstname);
                                edtLastName.setText(getLastname);
                                edtEmailId.setText(getEmail);

                                mAlert.onShowProgressDialog(EditProfileActivity.this, false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            mAlert.onShowProgressDialog(EditProfileActivity.this, false);
                            Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    private void UpdateProfileInfo(final JSONObject jsonObject, String bearerToken) {

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            AndroidNetworking.put(UPDATEUSERINFO)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders(Authorization, BEARER + bearerToken)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            GetProfileInfoUsingAuthKey(MedicoboxApp.onGetAuthToken());
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }
}

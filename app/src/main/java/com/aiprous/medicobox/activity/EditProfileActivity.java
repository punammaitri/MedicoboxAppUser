package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETBEARERTOKEN;
import static com.aiprous.medicobox.utils.APIConstant.UPDATEUSERINFO;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.isValidEmailId;
import static com.aiprous.medicobox.utils.BaseActivity.passwordValidation;

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
    private Context mContext = this;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleDateFormat;

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
        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            GetProfileInfoUsingAuthKey(MedicoboxApp.onGetAuthToken());
        }

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave() {
        String lfirst_name = edtFirstName.getText().toString().trim();
        String lLast_Name = edtLastName.getText().toString().trim();
        String lMobile_no = edtMobileNo.getText().toString().trim();
        String lEmail_id = edtEmailId.getText().toString().trim();
        String lDob = edtDob.getText().toString().trim();
        String lPassword = edtPassword.getText().toString().trim();
        String lConfirm_password = edtConfirmPassword.getText().toString().trim();

        if (lfirst_name.length() <= 2) {
            edtFirstName.setError("Firstname must be greater than 2 character");
        } else if (lLast_Name.length() <= 2) {
            edtLastName.setError("Lastname must be greater than 2 character");
        } else if (lMobile_no.length() <= 9) {
            edtMobileNo.setError("Mobile number must be greater 10 digit");
        } else if (!isValidEmailId(edtEmailId)) {
            edtEmailId.setError("Invalid email address");
        } else if (!passwordValidation(mContext, lPassword, edtPassword)) {
            edtPassword.setError("Please enter proper password");
        } else if (!passwordValidation(mContext, lConfirm_password, edtConfirmPassword)) {
            edtConfirmPassword.setError("Please enter confirm password");
        } else if (!lPassword.equals(lConfirm_password)) {
            Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show();
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

                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    UpdateProfileInfo(jsonObject, MedicoboxApp.onGetAuthToken());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void GetProfileInfoUsingAuthKey(final String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
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

                            CustomProgressDialog.getInstance().dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void UpdateProfileInfo(final JSONObject jsonObject, String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.put(UPDATEUSERINFO)
                .addJSONObjectBody(jsonObject)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        CustomProgressDialog.getInstance().dismissDialog();
                        startActivity(getIntent());
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(EditProfileActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    @OnClick(R.id.edt_dob)
    public void onViewClicked() {
        openDateAndTimePicker();
    }

    public void openDateAndTimePicker() {
        //////////////for pick up date and time /////////////////////////////////
        simpleDateFormat = new SimpleDateFormat("HH:mm EEE dd MM yyyy", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        calendar.setTimeInMillis(System.currentTimeMillis() - 1000);

        final Date minDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .minutesStep(1)
                .backgroundColor(Color.WHITE)
                .minDateRange(minDate)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                    }
                })
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        edtDob.setText(simpleDateFormat.format(date));
                    }
                });
        singleBuilder.display();
    }
}
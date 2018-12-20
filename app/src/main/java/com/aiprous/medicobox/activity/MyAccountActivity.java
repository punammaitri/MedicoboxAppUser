package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.prescription.PrescriptionChooseDeliveryAddressActivity;
import com.aiprous.medicobox.prescription.PrescriptionEditAddressActivity;
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

import static com.aiprous.medicobox.utils.APIConstant.ADD_ADDRESS;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class MyAccountActivity extends AppCompatActivity {


    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtMobileNumber)
    TextView txtMobileNumber;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.txtEditAddAddress)
    TextView txtEditAddAddress;
    @BindView(R.id.tv_change_delivery_address)
    TextView tvChangeDeliveryAddress;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        txtMobileNumber.setText(MedicoboxApp.onGetMobileNo());
        txtEmail.setText(MedicoboxApp.onGetEmail());
        txt_user_name.setText(MedicoboxApp.onGetFirstName() + " " + MedicoboxApp.onGetLastName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show cart size
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MedicoboxApp.onGetId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            callGetAddress(jsonObject);
        }
    }

    private void callGetAddress(JSONObject jsonObject) {
        AndroidNetworking.post(GET_ALL_ADDRESS)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonArray responseArray = getAllResponse.get("address").getAsJsonArray();


                        CustomProgressDialog.getInstance().dismissDialog();
                        // do anything with response
                      /*  try {
                            JSONObject jsonObject = new JSONObject(response.getString("response"));
                            String getId = jsonObject.get("id").toString();
                            String getGroupId = jsonObject.get("group_id").toString();
                            String getEmail = jsonObject.get("email").toString();
                            String getFirstname = jsonObject.get("firstname").toString();
                            String getLastname = jsonObject.get("lastname").toString();
                            String getStoreId = jsonObject.get("store_id").toString();
                            String getWebsiteId = jsonObject.get("website_id").toString();
                            String getMobile = jsonObject.get("mobile").toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
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

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {

        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.tv_edit_profile)
    public void onClickEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.tv_change_delivery_address)
    public void ChangeDeliveryAddress() {
        startActivity(new Intent(this, PrescriptionChooseDeliveryAddressActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick({R.id.txtEditAddAddress, R.id.tv_change_delivery_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtEditAddAddress:
                startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                        .putExtra("billingFlag", "true"));
                break;
            case R.id.tv_change_delivery_address:
                startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                        .putExtra("billingFlag", "false"));
                break;
        }
    }
}

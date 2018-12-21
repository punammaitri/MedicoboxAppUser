package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
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
    @BindView(R.id.txtEditBillingAddress)
    TextView txtEditBillingAddress;
    @BindView(R.id.txt_shipping_address)
    TextView txt_shipping_address;
    @BindView(R.id.txtUsername)
    TextView txtUsername;
    @BindView(R.id.txtFullAddress)
    TextView txtFullAddress;
    @BindView(R.id.txtTelephone)
    TextView txtTelephone;
    @BindView(R.id.linearBillingAddress)
    LinearLayout linearBillingAddress;
    @BindView(R.id.linearShippingAddress)
    LinearLayout linearShippingAddress;
    @BindView(R.id.txtShippingName)
    TextView txtShippingName;
    @BindView(R.id.txtShippingAddress)
    TextView txtShippingAddress;
    @BindView(R.id.txtShippingMobile)
    TextView txtShippingMobile;

    private Context mContext = this;
    private String id, street, lastname, postcode, region_id;
    private String country_id, city, firstname, telephone;

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
            callGetAddress(jsonObject);
            CustomProgressDialog.getInstance().dismissDialog();
        }
    }

    private void callGetAddress(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GET_ALL_ADDRESS)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                        String status = responseArray.get("status").getAsString();
                        if (status.equals("success")) {
                            JsonArray jsonAddressArray = responseArray.get("address").getAsJsonArray();
                            if (!(jsonAddressArray.size() == 0)) {
                                for (int i = 0; i < jsonAddressArray.size(); i++) {
                                    JsonObject asJsonObject = jsonAddressArray.get(i).getAsJsonObject();
                                    if (asJsonObject.get("default_billing").getAsString().equalsIgnoreCase("true")) {
                                        linearBillingAddress.setVisibility(View.VISIBLE);
                                        id = asJsonObject.get("id").getAsString();
                                        firstname = asJsonObject.get("firstname").getAsString();
                                        lastname = asJsonObject.get("lastname").getAsString();
                                        city = asJsonObject.get("city").getAsString();
                                        country_id = asJsonObject.get("country_id").getAsString();
                                        region_id = asJsonObject.get("region_id").getAsString();
                                        postcode = asJsonObject.get("postcode").getAsString();
                                        telephone = asJsonObject.get("telephone").getAsString();
                                        JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                        JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                        street = streetInnerArray.get(0).getAsString();

                                        String fullAddress = street + "," + city + "," + country_id + "\n" + postcode;
                                        txtFullAddress.setText(fullAddress);
                                        txtUsername.setText(firstname + " " + lastname);
                                        txtTelephone.setText(telephone);
                                        txtEditBillingAddress.setText("Edit");
                                    } else {
                                        linearShippingAddress.setVisibility(View.VISIBLE);
                                        id = asJsonObject.get("id").getAsString();
                                        firstname = asJsonObject.get("firstname").getAsString();
                                        lastname = asJsonObject.get("lastname").getAsString();
                                        city = asJsonObject.get("city").getAsString();
                                        country_id = asJsonObject.get("country_id").getAsString();
                                        region_id = asJsonObject.get("region_id").getAsString();
                                        postcode = asJsonObject.get("postcode").getAsString();
                                        telephone = asJsonObject.get("telephone").getAsString();
                                        JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                        JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                        street = streetInnerArray.get(0).getAsString();

                                        String fullAddress = street + "," + city + "," + country_id + "\n" + postcode;
                                        txtShippingAddress.setText(fullAddress);
                                        txtShippingName.setText(firstname + " " + lastname);
                                        txtShippingMobile.setText(telephone);
                                        txt_shipping_address.setText("Edit");
                                    }
                                }
                            }
                        }
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

    @OnClick({R.id.txtEditBillingAddress, R.id.txt_shipping_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtEditBillingAddress:
                if (txtEditBillingAddress.equals("Add")) {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("billingFlag", "true")
                            .putExtra("id", id)
                            .putExtra("firstname", firstname)
                            .putExtra("lastname", lastname)
                            .putExtra("city", city)
                            .putExtra("country_id", country_id)
                            .putExtra("region_id", region_id)
                            .putExtra("postcode", postcode)
                            .putExtra("telephone", telephone)
                            .putExtra("street", street));
                    finish();
                }

                break;
            case R.id.txt_shipping_address:
                if (txt_shipping_address.equals("Add")) {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "false"));
                    finish();
                } else {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "false"));
                    finish();
                }
                break;
        }
    }
}

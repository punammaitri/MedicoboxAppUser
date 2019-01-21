package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class MyAccountActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
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
    private String landmark;
    private String flat;
    private String billing_id,billing_firstname,billing_lastname,billing_city;
    private String billing_country_id,billing_region_id,billing_postcode,billing_telephone,billing_flat,billing_street,billing_landmark;
    private String shipping_firstname;
    private String shipping_lastname;
    private String shipping_telephone;
    private String shipping_flat;
    private String shipping_street;
    private String shipping_landmark;
    private String shipping_city;
    private String shipping_region_id;
    private String shipping_postcode;
    private String shipping_country_id;
    private String shipping_id;

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
            rlayout_cart.setVisibility(View.VISIBLE);
        } else {
            rlayout_cart.setVisibility(View.VISIBLE);
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MedicoboxApp.onGetId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().dismissDialog();
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
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
                        try {
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
                                            billing_id= asJsonObject.get("id").getAsString();
                                            billing_firstname = asJsonObject.get("firstname").getAsString();
                                            billing_lastname = asJsonObject.get("lastname").getAsString();
                                            billing_city = asJsonObject.get("city").getAsString();
                                            billing_country_id = asJsonObject.get("country_id").getAsString();
                                            billing_region_id = asJsonObject.get("region_id").getAsString();
                                            billing_postcode = asJsonObject.get("postcode").getAsString();
                                            billing_telephone = asJsonObject.get("telephone").getAsString();

                                            JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                            JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                            billing_flat = streetInnerArray.get(0).getAsString();
                                            billing_street = streetInnerArray.get(1).getAsString();
                                            billing_landmark = streetInnerArray.get(2).getAsString();

                                            String fullBillingAddress = billing_flat + "," + billing_street + "," + billing_landmark + "," + "\n" +
                                                    billing_city + "," + billing_country_id +  ","+billing_postcode;
                                            txtFullAddress.setText(fullBillingAddress);
                                            txtUsername.setText(billing_firstname + " " + billing_lastname);
                                            txtTelephone.setText(billing_telephone);
                                            txtEditBillingAddress.setText("EDIT");
                                        } else {
                                            linearShippingAddress.setVisibility(View.VISIBLE);
                                            shipping_id = asJsonObject.get("id").getAsString();
                                            shipping_firstname = asJsonObject.get("firstname").getAsString();
                                            shipping_lastname = asJsonObject.get("lastname").getAsString();
                                            shipping_city = asJsonObject.get("city").getAsString();
                                            shipping_country_id = asJsonObject.get("country_id").getAsString();
                                            shipping_region_id = asJsonObject.get("region_id").getAsString();
                                            shipping_postcode = asJsonObject.get("postcode").getAsString();
                                            shipping_telephone = asJsonObject.get("telephone").getAsString();

                                            JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                            JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                            shipping_flat = streetInnerArray.get(0).getAsString();
                                            shipping_street = streetInnerArray.get(1).getAsString();
                                            shipping_landmark = streetInnerArray.get(2).getAsString();

                                            String fullShippingAddress = shipping_flat + "," + shipping_street + "," + shipping_landmark + "," +
                                                    "\n" + shipping_city + "," + shipping_country_id + ","+ shipping_postcode;
                                            txtShippingAddress.setText(fullShippingAddress);
                                            txtShippingName.setText(shipping_firstname + " " + shipping_lastname);
                                            txtShippingMobile.setText(shipping_telephone);
                                            txt_shipping_address.setText("EDIT");
                                        }
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
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
                if (txtEditBillingAddress.getText().equals("ADD")) {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("billingFlag", "true"));
                    finish();
                } else {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("billingFlag", "true")
                            .putExtra("id", billing_id)
                            .putExtra("firstname", billing_firstname)
                            .putExtra("lastname", billing_lastname)
                            .putExtra("city", billing_city)
                            .putExtra("country_id", billing_country_id)
                            .putExtra("region_id", billing_region_id)
                            .putExtra("postcode", billing_postcode)
                            .putExtra("telephone", billing_telephone)
                            .putExtra("flat", billing_flat)
                            .putExtra("street", billing_street)
                            .putExtra("landmark", billing_landmark));
                    finish();
                }

                break;
            case R.id.txt_shipping_address:
                if (txt_shipping_address.getText().equals("ADD")) {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "true")
                    );
                    finish();
                } else {
                    startActivity(new Intent(MyAccountActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "true")
                            .putExtra("id", shipping_id)
                            .putExtra("firstname", shipping_firstname)
                            .putExtra("lastname", shipping_lastname)
                            .putExtra("city", shipping_city)
                            .putExtra("country_id", shipping_country_id)
                            .putExtra("region_id", shipping_region_id)
                            .putExtra("postcode", shipping_postcode)
                            .putExtra("telephone", shipping_telephone)
                            .putExtra("flat", shipping_flat)
                            .putExtra("street", shipping_street)
                            .putExtra("landmark", shipping_landmark));
                    finish();
                }
                break;
        }
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}

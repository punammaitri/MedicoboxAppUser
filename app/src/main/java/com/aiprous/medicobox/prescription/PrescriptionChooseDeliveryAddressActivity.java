package com.aiprous.medicobox.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.OrderSummaryActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AllCustomerAddress;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.DELETE_ADDRESS;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class PrescriptionChooseDeliveryAddressActivity extends AppCompatActivity implements PrescriptionChooseDeliveryAddressAdapter.DeleteInterface {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.rc_medicine_list)
    RecyclerView rc_medicine_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    ArrayList<AllCustomerAddress> mAllCustomerArrayList = new ArrayList<AllCustomerAddress>();
    @BindView(R.id.btn_insta_list)
    Button btnInstaList;

    private String id, street, lastname, postcode, region_id;
    private String country_id, city, firstname, telephone;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private String chooseDeliveryAddess;
    private boolean isChecked = false;
    private String flat, landmark;
    private String mAddressId = "";
    private String PrescriptionImageModel;
    private String getAdditionalComment;
    private String getPatientName;
    private String mFullname;
    private String mMobile;
    private String mAdress;
    private String getDose;
    private String getFlag;
    private String choose_billing_address;
    private String address_id;
    private String quote_id;
    private String imageBinaryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery_address);
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
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (getIntent().getStringExtra("choose_delivery_address") != null) {
            chooseDeliveryAddess = getIntent().getStringExtra("choose_delivery_address");
            getPatientName = getIntent().getStringExtra("getPatientName");
            getAdditionalComment = getIntent().getStringExtra("getAdditionalComment");
            getDose = getIntent().getStringExtra("getDose");
            getFlag = getIntent().getStringExtra("getFlag");
            btnInstaList.setVisibility(View.VISIBLE);
        }

        CallGetAddressAPI();
        CustomProgressDialog.getInstance().dismissDialog();
    }

    private void CallGetAddressAPI() {
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
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.tv_add_new)
    public void addNew() {
        startActivity(new Intent(this, PrescriptionEditAddressActivity.class)
                .putExtra("billingFlag", "true"));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick(R.id.btn_insta_list)
    public void ButtonInstaList() {
        if (isChecked) {
            startActivity(new Intent(this, PrescriptionOrderSummaryActivity.class)
                    .putExtra("mAddressId", "" + mAddressId)
                    .putExtra("choose_delivery_address", "" + chooseDeliveryAddess)
                    .putExtra("getPatientName", "" + getPatientName)
                    .putExtra("getAddress", "" + mAdress)
                    .putExtra("getFullname", "" + mFullname)
                    .putExtra("getMobile", "" + mMobile)
                    .putExtra("getDose", "" + getDose)
                    .putExtra("getFlag", "" + getFlag)
                    .putExtra("getAdditionalComment", "" + getAdditionalComment));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            Toast.makeText(mContext, "Please select delivery address", Toast.LENGTH_SHORT).show();
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
                        mAllCustomerArrayList.clear();
                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                        String status = responseArray.get("status").getAsString();
                        if (status.equals("success")) {
                            JsonArray jsonAddressArray = responseArray.get("address").getAsJsonArray();
                            if (jsonAddressArray != null) {
                                for (int i = 0; i < jsonAddressArray.size(); i++) {
                                    JsonObject asJsonObject = jsonAddressArray.get(i).getAsJsonObject();
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
                                    flat = streetInnerArray.get(0).getAsString();
                                    street = streetInnerArray.get(1).getAsString();
                                    landmark = streetInnerArray.get(2).getAsString();

                                    AllCustomerAddress allCustomerAddress = new AllCustomerAddress(id, telephone, postcode, region_id, country_id,
                                            city, street, lastname, firstname, flat, landmark);
                                    allCustomerAddress.setId(id);
                                    allCustomerAddress.setTelephone(telephone);
                                    allCustomerAddress.setPostcode(postcode);
                                    allCustomerAddress.setRegion_id(region_id);
                                    allCustomerAddress.setCountry_id(country_id);
                                    allCustomerAddress.setCity(city);
                                    allCustomerAddress.setStreet(street);
                                    allCustomerAddress.setLastname(lastname);
                                    allCustomerAddress.setFirstname(firstname);
                                    allCustomerAddress.setFlat(flat);
                                    allCustomerAddress.setLandmark(landmark);
                                    mAllCustomerArrayList.add(allCustomerAddress);
                                }

                                layoutManager = new LinearLayoutManager(mContext);
                                rc_medicine_list.setLayoutManager(new LinearLayoutManager(PrescriptionChooseDeliveryAddressActivity.this, LinearLayoutManager.VERTICAL, false));
                                rc_medicine_list.setHasFixedSize(true);
                                rc_medicine_list.setAdapter(new PrescriptionChooseDeliveryAddressAdapter(PrescriptionChooseDeliveryAddressActivity.this, mAllCustomerArrayList));
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

    @Override
    public void Delete(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MedicoboxApp.onGetId());
            jsonObject.put("address_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CallDeleteAPI(jsonObject);

        }
    }

    @Override
    public void RadioButtonCheck(boolean value, String id, String address, String fullname, String mobile) {
        isChecked = value;
        mAddressId = id;
        mAdress = address;
        mFullname = fullname;
        mMobile = mobile;

    }

    private void CallDeleteAPI(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(DELETE_ADDRESS)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonObject jsonObject = getAllResponse.get("response").getAsJsonObject();
                        String responseMsg = jsonObject.get("status").getAsString();

                        if (responseMsg.equals("success")) {
                            String msg = jsonObject.get("msg").getAsString();
                            CustomProgressDialog.getInstance().dismissDialog();
                            Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            CustomProgressDialog.getInstance().dismissDialog();
                            CallGetAddressAPI();
                        } else {
                            CustomProgressDialog.getInstance().dismissDialog();
                            Toast.makeText(mContext, "Address not deleted", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}

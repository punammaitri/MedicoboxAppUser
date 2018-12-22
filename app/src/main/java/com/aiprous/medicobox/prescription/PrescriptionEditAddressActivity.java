package com.aiprous.medicobox.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ADD_ADDRESS;
import static com.aiprous.medicobox.utils.APIConstant.UPDATE_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class PrescriptionEditAddressActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.edt_firstname)
    EditText edt_firstname;
    @BindView(R.id.edt_lastname)
    EditText edt_lastname;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_flat_no)
    EditText edtFlatNo;
    @BindView(R.id.edt_street)
    EditText edtStreet;
    @BindView(R.id.edt_landmark)
    EditText edtLandmark;
    @BindView(R.id.edt_pincde)
    EditText edtPincde;
    @BindView(R.id.edt_state)
    EditText edtState;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.radioButton)
    RadioButton radioButton;
    @BindView(R.id.btn_save)
    Button btnSave;
    private String billingFlag = "", shippingFlag = "";
    private Context mContext = this;
    private String id, street, postcode, telephone, country_id;
    private String city, lastname, firstname;
    private String region_id;
    private String chooseDeliveryAddess = "";
    private String setBillingAddress, setShippingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
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

        if (getIntent().getStringExtra("billingFlag") != null) {
            billingFlag = getIntent().getStringExtra("billingFlag");
            if (billingFlag.equals("true")) {
                radioButton.setVisibility(View.VISIBLE);
            } else {
                radioButton.setVisibility(View.GONE);
            }
        }

        if (getIntent().getStringExtra("shippingFlag") != null) {
            shippingFlag = getIntent().getStringExtra("shippingFlag");
            if (shippingFlag.equals("true")) {
                radioButton.setVisibility(View.GONE);
            } else {
                radioButton.setVisibility(View.VISIBLE);
            }
        }

        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            firstname = getIntent().getStringExtra("firstname");
            lastname = getIntent().getStringExtra("lastname");
            telephone = getIntent().getStringExtra("telephone");
            street = getIntent().getStringExtra("street");
            postcode = getIntent().getStringExtra("postcode");
            city = getIntent().getStringExtra("city");
            country_id = getIntent().getStringExtra("country_id");
            region_id = getIntent().getStringExtra("region_id");

            //set value
            edt_firstname.setText(firstname);
            edt_lastname.setText(lastname);
            edtPhone.setText(telephone);
            edtStreet.setText(street);
            edtPincde.setText(postcode);
            edtCity.setText(city);

            btnSave.setText("Update");
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.btn_save)
    public void ButtonSave() {
        String lFirstname = edt_firstname.getText().toString();
        String lLastname = edt_lastname.getText().toString();
        String lPhone = edtPhone.getText().toString();
        String lFlatNo = edtFlatNo.getText().toString();
        String lStreet = edtStreet.getText().toString();
        String lLandmark = edtLandmark.getText().toString();
        String lPincode = edtPincde.getText().toString();
        String lState = edtState.getText().toString();
        String lCity = edtCity.getText().toString();

        if (billingFlag.equals("true")) {
            setBillingAddress = "1";
            setShippingAddress = "0";
        } else {
            setBillingAddress = "0";
            setShippingAddress = "1";
        }


        if (lFirstname.length() == 0 && lPhone.length() == 0 && lFlatNo.length() == 0 && lStreet.length() == 0 && lLandmark.length() == 0 && lPincode.length() == 0 && lState.length() == 0 && lCity.length() == 0) {
            Toast.makeText(this, "All fields are required ", Toast.LENGTH_SHORT).show();
        } else if (lFirstname.length() == 0) {
            edt_firstname.setError("Please enter firstname");
        } else if (lLastname.length() == 0) {
            edt_lastname.setError("Please enter lastname");
        } else if (lPhone.length() == 0) {
            edtPhone.setError("Please enter phone number");
        } else if (lFlatNo.length() == 0) {
            edtFlatNo.setError("Please enter flat number or building name");
        } else if (lStreet.length() == 0) {
            edtStreet.setError("Please enter Street / Road Name");
        } else if (lLandmark.length() == 0) {
            edtLandmark.setError("Please enter landmark");
        } else if (lPincode.length() == 0) {
            edtPincde.setError("Please enter pincode");
        } else if (lState.length() == 0) {
            edtState.setError("Please enter state");
        } else if (lCity.length() == 0) {
            edtCity.setError("Please enter city");
        } else if (lPhone.length() <= 9) {
            edtPhone.setError("Phone number should be 10 digit");
        } else {

            if (btnSave.getText().equals("Save")) {
                //for Add API
                JSONObject jsonObjectReg = null;
                try {
                    JSONObject objCustomer = new JSONObject();
                    objCustomer.put("first_name", lFirstname);
                    objCustomer.put("last_name", lLastname);
                    objCustomer.put("city", lCity);
                    objCustomer.put("state_code", 1);
                    objCustomer.put("country_id", "IN");
                    objCustomer.put("postcode", lPincode);
                    objCustomer.put("telephone", lPhone);
                    objCustomer.put("company", "Company");
                    objCustomer.put("fax", "fax");
                    objCustomer.put("street", lStreet);
                    objCustomer.put("default_shipping", setShippingAddress);
                    objCustomer.put("default_billing", setBillingAddress);

                    jsonObjectReg = new JSONObject();
                    jsonObjectReg.put("user_id", MedicoboxApp.onGetId());
                    jsonObjectReg.put("address", objCustomer);
                    Log.e("url", jsonObjectReg.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
                    CallAddAddressAPI(jsonObjectReg);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            } else {

                //for update API
                JSONObject jsonObjectReg = null;
                try {
                    JSONObject objCustomer = new JSONObject();
                    objCustomer.put("first_name", lFirstname);
                    objCustomer.put("last_name", lLastname);
                    objCustomer.put("city", lCity);
                    objCustomer.put("state_code", 1);
                    objCustomer.put("country_id", "IN");
                    objCustomer.put("postcode", lPincode);
                    objCustomer.put("telephone", lPhone);
                    objCustomer.put("company", "Company");
                    objCustomer.put("fax", "fax");
                    objCustomer.put("street", lStreet);
                    objCustomer.put("default_shipping", setShippingAddress);
                    objCustomer.put("default_billing", setBillingAddress);

                    jsonObjectReg = new JSONObject();
                    jsonObjectReg.put("user_id", MedicoboxApp.onGetId());
                    jsonObjectReg.put("address_id", id);
                    jsonObjectReg.put("address", objCustomer);
                    Log.e("url", jsonObjectReg.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
                    CallUpdateAPI(jsonObjectReg);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }

    private void CallUpdateAPI(JSONObject jsonObjectReg) {
        AndroidNetworking.post(UPDATE_ADDRESS)
                .addJSONObjectBody(jsonObjectReg) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                        String responseMsg = responseArray.get("msg").getAsString();
                        Toast.makeText(mContext, "Address update successfully.", Toast.LENGTH_SHORT).show();

                        if (responseMsg.equals("address added successfully.")) {
                            JsonObject data = responseArray.get("data").getAsJsonObject();
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

    private void CallAddAddressAPI(JSONObject jsonObject) {
        AndroidNetworking.post(ADD_ADDRESS)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                        String responseMsg = responseArray.get("msg").getAsString();
                        Toast.makeText(mContext, "" + responseMsg, Toast.LENGTH_SHORT).show();

                        if (responseMsg.equals("address added successfully.")) {
                            JsonObject data = responseArray.get("data").getAsJsonObject();
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

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        if (chooseDeliveryAddess.isEmpty()) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (chooseDeliveryAddess.isEmpty()) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else {
            finish();
        }
    }
}

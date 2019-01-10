package com.aiprous.medicobox.prescription;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.GooglePlacesActivity;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ADD_ADDRESS;
import static com.aiprous.medicobox.utils.APIConstant.UPDATE_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class PrescriptionEditAddressActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.search_address)
    EditText search_address;
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
    private String setBillingAddress, setShippingAddress;
    private String chooseDelivery = "";
    private JSONArray jsonArray;
    ArrayList<String> mStreetArray = new ArrayList<String>();
    private String flat;
    private String landmark;
    private String order_summary = "";
    private String mEdit = "";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Double lat = 0.0;
    Double log = 0.0;
    private String mAddress = "";
    private String mAreaUsingPlaces = "";
    private String mCityUsingPlaces = "";
    private String mStateUsingPlaces = "";
    String mArea;
    private String subArea1 = "";
    private String subArea2 = "";
    private String mPostalCodeUsingPlaces;
    private String mFeatureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        search_address.setFocusable(false);

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

        //get billing flag
        if (getIntent().getStringExtra("billingFlag") != null) {
            billingFlag = getIntent().getStringExtra("billingFlag");
            if (billingFlag.equals("true")) {
                radioButton.setVisibility(View.VISIBLE);
                setBillingAddress = "1";
                setShippingAddress = "0";
            } else {
                radioButton.setVisibility(View.GONE);
            }
        }

        if (getIntent().getStringExtra("fromchooseDelivery") != null) {
            chooseDelivery = getIntent().getStringExtra("fromchooseDelivery");
        }

        //for back page issue
        if (getIntent().getStringExtra("edit_popup") != null) {
            mEdit = getIntent().getStringExtra("edit_popup");
        }


        if (getIntent().getStringExtra("order_summary") != null) {
            order_summary = getIntent().getStringExtra("order_summary");
        }

        //get shipping flag
        if (getIntent().getStringExtra("shippingFlag") != null) {
            shippingFlag = getIntent().getStringExtra("shippingFlag");
            if (shippingFlag.equals("true")) {
                radioButton.setVisibility(View.GONE);
                setBillingAddress = "0";
                setShippingAddress = "1";
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
            flat = getIntent().getStringExtra("flat");
            landmark = getIntent().getStringExtra("landmark");
            postcode = getIntent().getStringExtra("postcode");
            city = getIntent().getStringExtra("city");
            country_id = getIntent().getStringExtra("country_id");
            region_id = getIntent().getStringExtra("region_id");

            //set value
            edt_firstname.setText(firstname);
            edt_lastname.setText(lastname);
            edtPhone.setText(telephone);
            edtStreet.setText(street);
            edtFlatNo.setText(flat);
            edtLandmark.setText(landmark);
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
                mStreetArray.clear();

                String getFlatNo = edtFlatNo.getText().toString().trim();
                String getStreet = edtStreet.getText().toString().trim();
                String getLandmark = edtLandmark.getText().toString().trim();

                mStreetArray.add(getFlatNo);
                mStreetArray.add(getStreet);
                mStreetArray.add(getLandmark);
                jsonArray = new JSONArray(mStreetArray);

                try {
                    JSONObject objCustomer = new JSONObject();
                    objCustomer.put("first_name", lFirstname);
                    objCustomer.put("last_name", lLastname);
                    objCustomer.put("city", lCity);
                    objCustomer.put("state_code", "553");
                    objCustomer.put("country_id", "IN");
                    objCustomer.put("postcode", lPincode);
                    objCustomer.put("telephone", lPhone);
                    objCustomer.put("company", "Company");
                    objCustomer.put("fax", "fax");
                    objCustomer.put("street", jsonArray);
                    objCustomer.put("default_shipping", setShippingAddress);
                    objCustomer.put("default_billing", setBillingAddress);

                    jsonObjectReg = new JSONObject();
                    jsonObjectReg.put("user_id", MedicoboxApp.onGetId());
                    jsonObjectReg.put("address", objCustomer);
                    Log.e("data", jsonObjectReg.toString());
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
                mStreetArray.clear();

                String getFlatNo = edtFlatNo.getText().toString().trim();
                String getStreet = edtStreet.getText().toString().trim();
                String getLandmark = edtLandmark.getText().toString().trim();

                mStreetArray.add(getFlatNo);
                mStreetArray.add(getStreet);
                mStreetArray.add(getLandmark);
                jsonArray = new JSONArray(mStreetArray);

                try {
                    JSONObject objCustomer = new JSONObject();
                    objCustomer.put("first_name", lFirstname);
                    objCustomer.put("last_name", lLastname);
                    objCustomer.put("city", lCity);
                    objCustomer.put("state_code", "553");
                    objCustomer.put("country_id", "IN");
                    objCustomer.put("postcode", lPincode);
                    objCustomer.put("telephone", lPhone);
                    objCustomer.put("company", "Company");
                    objCustomer.put("fax", "fax");
                    objCustomer.put("street", jsonArray);
                    objCustomer.put("default_shipping", setShippingAddress);
                    objCustomer.put("default_billing", setBillingAddress);

                    jsonObjectReg = new JSONObject();
                    jsonObjectReg.put("user_id", MedicoboxApp.onGetId());
                    jsonObjectReg.put("address_id", id);
                    jsonObjectReg.put("address", objCustomer);
                    Log.e("data", jsonObjectReg.toString());
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

        if (!order_summary.isEmpty()) {
            finish();
        } else if (mEdit.equals("true")) {
            finish();
        } else if (billingFlag.equals("true") || shippingFlag.equals("true")) {
            startActivity(new Intent(PrescriptionEditAddressActivity.this, MyAccountActivity.class));
            finish();
        } else if (chooseDelivery.isEmpty()) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else if (!billingFlag.equals("") || !shippingFlag.equals("")) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!order_summary.isEmpty()) {
            finish();
        } else if (mEdit.equals("true")) {
            finish();
        } else if (billingFlag.equals("true") || shippingFlag.equals("true")) {
            startActivity(new Intent(PrescriptionEditAddressActivity.this, MyAccountActivity.class));
            finish();
        } else if (chooseDelivery.isEmpty()) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else if (!billingFlag.equals("") || !shippingFlag.equals("")) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            finish();
        } else {
            finish();
        }
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

    @OnClick(R.id.search_address)
    public void searchAddress() {
        Search();
    }

    private void Search() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("Places", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i("Places", "An error occurred: " + status);
            }
        });

        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("IND")
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);

            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mAddress = (String) place.getAddress();

                Log.e("address", "" + (CharSequence) place.getAddress());
                //edt_gps_current_location_partnerwithus.setText();
                LatLng latlog = place.getLatLng();

                lat = latlog.latitude;
                log = latlog.longitude;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, log, 1);
                    if (addresses != null) {
                        mAreaUsingPlaces = " " + addresses.get(0).getSubLocality();
                        mCityUsingPlaces = addresses.get(0).getLocality();
                        mStateUsingPlaces = addresses.get(0).getAdminArea();
                        mPostalCodeUsingPlaces = addresses.get(0).getPostalCode();
                        mFeatureName = addresses.get(0).getFeatureName();

                        edtStreet.setText(mFeatureName);
                        edtLandmark.setText(mAreaUsingPlaces);
                        edtPincde.setText(mPostalCodeUsingPlaces);
                        edtState.setText(mStateUsingPlaces);
                        edtCity.setText(mCityUsingPlaces);
                        getMyLocation(lat, log);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("Result Ok", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("Result Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private String getMyLocation(double latitude1, double longitude1) {
        String local_address = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude1, longitude1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses.size() > 0) {
                Log.e("Address", addresses.get(0).getLocality());
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                mArea = " " + addresses.get(0).getSubLocality();

                String[] arr = address.split(",");
                try {
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].equals(mArea)) {
                            subArea1 = arr[i - 1];
                            subArea2 = arr[i - 2];
                            if (subArea2.isEmpty()) {
                                subArea2 = subArea1;
                            }
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (addresses.get(0).getThoroughfare().equalsIgnoreCase("null")) {
                        //edtEmailLogin.setText(addresses.get(0).getLocality());
                    } else {
                        //edtEmailLogin.setText(addresses.get(0).getLocality());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // AlertDialogs.getInstance().onShowToastNotification(this, getString(R.string.error_location));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return local_address;
    }
}
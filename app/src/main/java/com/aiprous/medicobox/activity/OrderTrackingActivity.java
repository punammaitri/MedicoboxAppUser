package com.aiprous.medicobox.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.OrderTrackingAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.OrderTrackingModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ORDER_TRACKING;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class OrderTrackingActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    @BindView(R.id.txtOrderId)
    TextView txtOrderId;
    @BindView(R.id.txtDate)
    TextView txtDate;
    @BindView(R.id.txtOrderAmount)
    TextView txtOrderAmount;
    @BindView(R.id.delivery_username)
    TextView deliveryUsername;
    @BindView(R.id.del_address)
    TextView delAddress;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //private String mDropAddress = "Khamgaon";
    private LatLng mDropLatLng;
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_medicine_list)
    RecyclerView rc_medicine_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private OrderTrackingActivity mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<LatLng> MarkerPoints;
    private String mWarehouseAddress="", mShippingAddress="", mDeliveryAddress="";
    private LatLng mWarehouseLatLng;
    private String order_id;
    ArrayList<OrderTrackingModel> mTrackingModels = new ArrayList<>();
    private String mWareHouseLat = "", mWareHouseLong="", mDeliveryBoyLat="";
    private String mShippingLat, mShippingLong, mDeliveryBoyLong;
    private String orderId, order_date, order_amount;
    private LatLng mShippingLatLng, mDeliveryLatLng;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String street;
    private String city, postcode, firstname, lastname;
    private Double Latitude = 0.00;
    private Double Longitude = 0.00;

    ArrayList<HashMap<String, String>> location = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this);
        init();
    }

    private void callTrackOrder() {
        //track order api call
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        if (getIntent().getStringExtra("order_id") != null) {
            order_id = getIntent().getStringExtra("order_id");

            if (!isNetworkAvailable(this)) {
                CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
            } else {
                CallTrackOrderAPI("77");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    private void CallTrackOrderAPI(String order_id) {
        AndroidNetworking.get(ORDER_TRACKING + order_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                        String status = getAllResponse.get("status").getAsString();

                        if (status.equals("success")) {
                            //for order date
                            JsonObject responseArray = getAllResponse.get("data").getAsJsonObject();
                            orderId = responseArray.get("order_id").getAsString();
                            txtOrderId.setText("Order ID:" + orderId);

                            //for order amount
                            order_amount = responseArray.get("order_amount").getAsString();
                            txtOrderAmount.setText("\u20B9" + order_amount);

                            //for order date
                            order_date = responseArray.get("order_date").getAsString();
                            StringTokenizer date = new StringTokenizer(order_date, " ");
                            String newYear = date.nextToken();
                            String newTime = date.nextToken();
                            txtDate.setText(newYear);

                            //Warehouse lat long
                            JsonObject warehouse = responseArray.get("warehouse").getAsJsonObject();
                            if (warehouse != null) {
                                mWareHouseLat = warehouse.get("lat").getAsString();
                                mWareHouseLong = warehouse.get("long").getAsString();
                                Double warehouseLat = Double.valueOf(mWareHouseLat);
                                Double warehouseLong = Double.valueOf(mWareHouseLong);
                                //for warehouse
                                mWarehouseLatLng = new LatLng(warehouseLat, warehouseLong);
                                mWarehouseAddress = getAddressFromLatLng(OrderTrackingActivity.this, warehouseLat, warehouseLong);
                            }

                            //Shipping  lat long
                            JsonObject shipping = responseArray.get("shipping").getAsJsonObject();
                            if (shipping != null) {
                                mShippingLat = shipping.get("lat").getAsString();
                                mShippingLong = shipping.get("long").getAsString();
                                Double shippingLat = Double.valueOf(mShippingLat);
                                Double shippingLong = Double.valueOf(mShippingLong);
                                //for shipping
                                mShippingLatLng = new LatLng(shippingLat, shippingLong);
                                mShippingAddress = getAddressFromLatLng(OrderTrackingActivity.this, shippingLat, shippingLong);
                            }

                            //Delivery boy lat long
                            JsonObject delivery_boy = responseArray.get("delivery_boy").getAsJsonObject();
                            if (shipping != null) {
                                mDeliveryBoyLat = delivery_boy.get("lat").getAsString();
                                mDeliveryBoyLong = delivery_boy.get("long").getAsString();
                                Double deliveryLat = Double.valueOf(mDeliveryBoyLat);
                                Double deliveryLong = Double.valueOf(mDeliveryBoyLong);
                                //for delivery boy
                                mDeliveryLatLng = new LatLng(deliveryLat, deliveryLong);
                                mDeliveryAddress = getAddressFromLatLng(OrderTrackingActivity.this, deliveryLat, deliveryLong);
                            }

                            //add location to arrayList for warehouse
                            map = new HashMap<String, String>();
                            map.put("Latitude", mWareHouseLat);
                            map.put("Longitude", mWareHouseLong);
                            map.put("LocationName", mWarehouseAddress);
                            location.add(map);

                            //add location to arrayList for shipping
                            map = new HashMap<String, String>();
                            map.put("Latitude", mShippingLat);
                            map.put("Longitude", mShippingLong);
                            map.put("LocationName", mShippingAddress);
                            location.add(map);

                            //add location to arrayList for delivery boy
                            map = new HashMap<String, String>();
                            map.put("Latitude", mDeliveryBoyLat);
                            map.put("Longitude", mDeliveryBoyLong);
                            map.put("LocationName", mDeliveryAddress);
                            location.add(map);

                            //for adding marker
                            for (int i = 0; i < location.size(); i++) {
                                Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
                                Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
                                String name = location.get(i).get("LocationName").toString();
                                MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
                                //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                if (i == 0) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    mMap.addMarker(marker);
                                } else if (i ==1) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                    mMap.addMarker(marker);
                                } else if (i == 2) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                    mMap.addMarker(marker);
                                }
                            }

                            JsonObject delivery_address = responseArray.get("delivery_address").getAsJsonObject();
                            //for full address
                            firstname = delivery_address.get("firstname").getAsString();
                            lastname = delivery_address.get("lastname").getAsString();
                            street = delivery_address.get("street").getAsString();
                            city = delivery_address.get("city").getAsString();
                            postcode = delivery_address.get("postcode").getAsString();

                            String fulldeliveryAddress = street + "," + city + "," + postcode;
                            delAddress.setText(fulldeliveryAddress);
                            deliveryUsername.setText(firstname + " " + lastname);

                            JsonArray items = responseArray.get("items").getAsJsonArray();
                            if (items != null) {
                                mTrackingModels.clear();
                                for (int j = 0; j < items.size(); j++) {
                                    JsonObject itemObject = items.get(j).getAsJsonObject();
                                    String id = itemObject.get("id").getAsString();
                                    String company_name = itemObject.get("company_name").getAsString();
                                    String image = itemObject.get("image").getAsString();
                                    String price = itemObject.get("price").getAsString();

                                    OrderTrackingModel orderTrackingModel = new OrderTrackingModel(id, company_name, image, price);
                                    orderTrackingModel.setId(id);
                                    orderTrackingModel.setCompany_name(company_name);
                                    orderTrackingModel.setImage(image);
                                    orderTrackingModel.setPrice(price);
                                    mTrackingModels.add(orderTrackingModel);
                                }
                            }

                            layoutManager = new LinearLayoutManager(mContext);
                            rc_medicine_list.setLayoutManager(new LinearLayoutManager(OrderTrackingActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_medicine_list.setHasFixedSize(true);
                            rc_medicine_list.setAdapter(new OrderTrackingAdapter(OrderTrackingActivity.this, mTrackingModels));

                            CustomProgressDialog.getInstance().dismissDialog();
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

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void init() {
        MarkerPoints = new ArrayList<>();
        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        if (checkPlayServices()) {
            if (!BaseActivity.isLocationEnabled(this)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(R.string.location_service);
                dialog.setMessage(R.string.location_service_message);
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
                dialog.show();
            }
        } else {
            Toast.makeText(this, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }
        callTrackOrder();
    }

    /*********************************Checking GooglePlayServices**************************/
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @OnClick(R.id.btn_Cancel_Order)
    public void cancelOrder() {
        startActivity(new Intent(this, CancelOrderActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private String getAddressFromLatLng(Context context, double latitude, double longitude) {
        String strAdd = null;
        try {
            Geocoder geocoder;
            List<Address> addresses;
            strAdd = "";
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                strAdd = address + " " + city;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strAdd;
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}
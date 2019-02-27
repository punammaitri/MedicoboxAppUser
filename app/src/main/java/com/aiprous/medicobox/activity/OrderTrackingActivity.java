package com.aiprous.medicobox.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.rc_medicine_list)
    RecyclerView rc_medicine_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private OrderTrackingActivity mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<LatLng> MarkerPoints;
    private String mWarehouseAddress = "", mShippingAddress = "", mDeliveryAddress = "";
    private LatLng mWarehouseLatLng;
    private String order_id;
    ArrayList<OrderTrackingModel> mTrackingModels = new ArrayList<>();
    private String mWareHouseLat = "", mWareHouseLong = "", mDeliveryBoyLat = "";
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
    private Double warehouseLat;
    private Double warehouseLong;
    private Double shippingLong;
    private Double shippingLat;
    private Double deliveryLat;
    private Double deliveryLong;
    private String delivery_address_flat;
    private String delivery_address_street;
    private String delivery_address_landmark;

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
                CallTrackOrderAPI(order_id);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.VISIBLE);
        } else {
            rlayout_cart.setVisibility(View.VISIBLE);
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
                            txtOrderId.setText("Order ID: " + orderId);

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

                                //for warehouse latlong
                                if (!mWareHouseLat.equals("") && !mWareHouseLong.equals("")) {
                                    warehouseLat = Double.valueOf(mWareHouseLat);
                                    warehouseLong = Double.valueOf(mWareHouseLong);
                                    //for warehouse
                                    mWarehouseLatLng = new LatLng(warehouseLat, warehouseLong);
                                    mWarehouseAddress = getAddressFromLatLng(OrderTrackingActivity.this, warehouseLat, warehouseLong);

                                    //add location to arrayList for warehouse
                                    map = new HashMap<String, String>();
                                    map.put("Latitude", mWareHouseLat);
                                    map.put("Longitude", mWareHouseLong);
                                    map.put("LocationName", mWarehouseAddress);
                                    location.add(map);
                                }
                            }

                            //Shipping  lat long
                            JsonObject shipping = responseArray.get("shipping").getAsJsonObject();
                            if (shipping != null) {
                                mShippingLat = shipping.get("lat").getAsString();
                                mShippingLong = shipping.get("long").getAsString();

                                if (!mShippingLat.equals("") && !mShippingLong.equals("")) {
                                    shippingLat = Double.valueOf(mShippingLat);
                                    shippingLong = Double.valueOf(mShippingLong);
                                    //for shipping
                                    mShippingLatLng = new LatLng(shippingLat, shippingLong);
                                    mShippingAddress = getAddressFromLatLng(OrderTrackingActivity.this, shippingLat, shippingLong);

                                    //add location to arrayList for shipping
                                    map = new HashMap<String, String>();
                                    map.put("Latitude", mShippingLat);
                                    map.put("Longitude", mShippingLong);
                                    map.put("LocationName", mShippingAddress);
                                    location.add(map);
                                }
                            }

                            //Delivery boy lat long
                            JsonObject delivery_boy = responseArray.get("delivery_boy").getAsJsonObject();
                            if (shipping != null) {
                                mDeliveryBoyLat = delivery_boy.get("lat").getAsString();
                                mDeliveryBoyLong = delivery_boy.get("long").getAsString();

                                if (!mDeliveryBoyLat.equals("") && !mDeliveryBoyLong.equals("")) {
                                    deliveryLat = Double.valueOf(mDeliveryBoyLat);
                                    deliveryLong = Double.valueOf(mDeliveryBoyLong);
                                    //for delivery boy
                                    mDeliveryLatLng = new LatLng(deliveryLat, deliveryLong);
                                    mDeliveryAddress = getAddressFromLatLng(OrderTrackingActivity.this, deliveryLat, deliveryLong);

                                    //add location to arrayList for delivery boy
                                    map = new HashMap<String, String>();
                                    map.put("Latitude", mDeliveryBoyLat);
                                    map.put("Longitude", mDeliveryBoyLong);
                                    map.put("LocationName", mDeliveryAddress);
                                    location.add(map);
                                }
                            }


                          /*  //for adding marker on map
                            for (int i = 0; i < location.size(); i++) {
                                Latitude = Double.parseDouble(location.get(i).get("Latitude").toString());
                                Longitude = Double.parseDouble(location.get(i).get("Longitude").toString());
                                String name = location.get(i).get("LocationName").toString();
                                MarkerOptions marker = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name);
                                //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                                if (i == 0) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    mMap.addMarker(marker);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitude), 18));

                                } else if (i == 1) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                    mMap.addMarker(marker);
                                } else if (i == 2) {
                                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                    mMap.addMarker(marker);
                                }
                            }*/

                            JsonObject delivery_address = responseArray.get("delivery_address").getAsJsonObject();
                            //for full address
                            firstname = delivery_address.get("firstname").getAsString();
                            lastname = delivery_address.get("lastname").getAsString();

                            JsonArray streetArray = delivery_address.get("street").getAsJsonArray();
                            JsonArray streetInnerArray = streetArray.getAsJsonArray();
                            delivery_address_flat = streetInnerArray.get(0).getAsString();
                            delivery_address_street = streetInnerArray.get(1).getAsString();
                            delivery_address_landmark = streetInnerArray.get(2).getAsString();

                            city = delivery_address.get("city").getAsString();
                            postcode = delivery_address.get("postcode").getAsString();

                            String fulldeliveryAddress = delivery_address_flat + "," + delivery_address_street + "," + delivery_address_landmark + "," +
                                    "\n" + city + "," + postcode;

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
        startActivity(new Intent(OrderTrackingActivity.this, CancelOrderActivity.class)
                .putExtra("order_id", "" + order_id));
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


        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /*    *
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.*/

                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    OrderTrackingActivity.FetchUrl FetchUrl = new OrderTrackingActivity.FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                }
            }
        });
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


        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


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

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}
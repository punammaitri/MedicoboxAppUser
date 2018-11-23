package com.aiprous.medicobox;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.LoginActivity;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.activity.NotificationActivity;
import com.aiprous.medicobox.activity.SettingActivity;
import com.aiprous.medicobox.adapter.CartAdapter;
import com.aiprous.medicobox.adapter.NavAdaptor;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.model.NavItemClicked;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.aiprous.medicobox.utils.TrackGPS;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETCARTITEMS;


public class MainActivity extends AppCompatActivity
        implements NavItemClicked {

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.sideView)
    LinearLayout sideView;
    @BindView(R.id.layout_container)
    FrameLayout layoutContainer;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    // @BindView(R.id.tvMainToolbarTitle)
    //TextView tvMainToolbarTitle;
    private final String TAG = MainActivity.class.getSimpleName();
    private Unbinder unbinder;
    public static DrawerLayout drawerLayout;
    private HomeFragment homeFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    public static Toolbar toolbarMain;
    private RecyclerView rvForNavigation;
    private NavAdaptor navAdaptor;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    TrackGPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        toolbarMain = findViewById(R.id.toolbarMain);
        toolbarMain.setContentInsetStartWithNavigation(0);
        drawerLayout = findViewById(R.id.drawerLayout);
        init();
    }

    private void init() {

        //set current location city
        gps = new TrackGPS(mContext);
        MedicoboxApp.onSaveLatiLong("" + gps.getLatitude() + "," + gps.getLongitude());
        getLocationFromLatLong();

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        homeFragment = new HomeFragment(this);
        setDrawerToggle();
        addFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //navigation drawer for user
        navigationItem(true);

        //navigation drawer for pharmacist
        //navigationItemPharmacist(true);

        getCartItems(MedicoboxApp.onGetAuthToken());

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            rlayout_cart.setVisibility(View.VISIBLE);
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }


    }

    public void addFragment() {
        //optionMenu.setVisibility(View.VISIBLE);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.layout_container, homeFragment, "TagName");
        mFragmentTransaction.commit();
    }

    private void setDrawerToggle() {
        rvForNavigation = (RecyclerView) navView.findViewById(R.id.rvForNavigation);
        View header = navView.getHeaderView(0);

        txtUserName.setText(MedicoboxApp.onGetFirstName());
        txtEmail.setText(MedicoboxApp.onGetEmail());

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvForNavigation.setLayoutManager(layoutManager);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarMain,
                R.string.navigation_drawer_close, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard(mContext, drawerView);
            }


            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    coordinatorLayout.setTranslationX(slideOffset * drawerView.getWidth());

                }
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        mDrawerToggle.syncState();
    }

    public static void hideKeyboard(@NonNull Context context, @Nullable View view) {
        // Check if no view has focus:
        //View view = context.get
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void navItemClicked(String name, int position) {

        if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_home))) {
            drawerLayout.closeDrawer(GravityCompat.START);
            int fragCount = getSupportFragmentManager().getBackStackEntryCount();
            if (fragCount > 0) {
                for (int i = 0; i < fragCount; i++) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
            addFragment();
            return;
        } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_account))) {
            startActivity(new Intent(mContext, MyAccountActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_cart))) {
            startActivity(new Intent(this, CartActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_settings))) {
            startActivity(new Intent(mContext, SettingActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_notification))) {
            startActivity(new Intent(mContext, NotificationActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_logout))) {
            logout();
            drawerLayout.closeDrawer(GravityCompat.START);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }
    }

    // set data into navigation view
    private void navigationItem(boolean isBasic) {
        String title[];
        int icon[];

        title = new String[]{
                mContext.getResources().getString(R.string.txt_home),
                mContext.getResources().getString(R.string.txt_orders),
                mContext.getResources().getString(R.string.txt_account),
                mContext.getResources().getString(R.string.txt_cart),
                mContext.getResources().getString(R.string.txt_notification),
                mContext.getResources().getString(R.string.txt_settings),
                mContext.getResources().getString(R.string.txt_logout)};

        icon = new int[]{
                R.drawable.home,
                R.drawable.box,
                R.drawable.user,
                R.drawable.cart,
                R.drawable.bell,
                R.drawable.settings,
                R.drawable.logout,};

        navAdaptor = new NavAdaptor(mContext, this, title, icon);
        rvForNavigation.setAdapter(navAdaptor);
    }


    //---Function to check network connection---//
    public static boolean isNetworkAvailable(@NonNull Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void backExit() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getResources().getString(R.string.are_you_sure))
                .setContentText(mContext.getResources().getString(R.string.are_you_exit))
                .setConfirmText(mContext.getResources().getString(R.string.yes))
                .setCancelText(mContext.getResources().getString(R.string.no))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void logout() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getResources().getString(R.string.are_you_sure))
                .setContentText(mContext.getResources().getString(R.string.are_you_sure_logout))
                .setConfirmText(mContext.getResources().getString(R.string.yes))
                .setCancelText(mContext.getResources().getString(R.string.no))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        startActivity(intent);
                        MedicoboxApp.onSaveLoginDetail("", "", "", "", "", "", "");
                        MedicoboxApp.onSaveCartId("");
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        boolean isPop = pop();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!isPop) {
            backExit();
        }
    }

    private boolean pop() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            // tvMainToolbarTitle.setText(mContext.getResources().getString(R.string.dashboard));
            getSupportFragmentManager().popBackStackImmediate();
            return true;
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            return true;
        }
        return false;
    }

    @OnClick(R.id.rlayout_cart)
    public void onClickCart() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        startActivity(new Intent(this, CartActivity.class));
    }

    private void getLocationFromLatLong() {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String[] lLatLong = MedicoboxApp.getLatiLong().split(",");
            List<Address> addresses = geocoder.getFromLocation(Float.parseFloat(lLatLong[0]), Float.parseFloat(lLatLong[1]), 1);

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String area = addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            String fullAddress = area + "," + city + "," + state + "," + country + "," + postalCode;
            tv_location.setText(city);
            MedicoboxApp.onSaveCity(city);
            Log.e("Location fetch:", "" + fullAddress);
            //Toast.makeText(mContext, fullAddress, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Location is not fetch", "");
        }
    }

    private void getCartItems(final String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GETCARTITEMS)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        // Toast.makeText(mcontext, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                            JSONArray getAllProductList = getAllObject.getJSONArray("res");//get the array with the key "response"


                            try {
                                if (getAllProductList != null) {
                                    //clear singleton array
                                    SingletonAddToCart.getGsonInstance().option.clear();
                                    SingletonAddToCart singletonOptionData = SingletonAddToCart.getGsonInstance();
                                    for (int i = 0; i < getAllProductList.length(); i++) {
                                        int id = Integer.parseInt(getAllProductList.getJSONObject(i).get("item_id").toString());
                                        String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                        int qty = Integer.parseInt(getAllProductList.getJSONObject(i).get("qty").toString());
                                        String name = getAllProductList.getJSONObject(i).get("name").toString();
                                        String product_type = getAllProductList.getJSONObject(i).get("product_type").toString();
                                        String lquoteId = getAllProductList.getJSONObject(i).get("quote_id").toString();
                                        String image = getAllProductList.getJSONObject(i).get("image").toString();
                                        String short_description = getAllProductList.getJSONObject(i).get("short_description").toString();
                                        String mrp = getAllProductList.getJSONObject(i).get("price").toString();
                                        int discount = Integer.parseInt(getAllProductList.getJSONObject(i).get("discount").toString());
                                        String price = getAllProductList.getJSONObject(i).get("sale_price").toString();
                                        if(price.isEmpty())
                                        {
                                            price = getAllProductList.getJSONObject(i).get("price").toString();
                                        }else {
                                             price = getAllProductList.getJSONObject(i).get("sale_price").toString();
                                        }
                                        float lcalculatePrice=qty*Float.parseFloat(price);


                                        AddToCartOptionDetailModel listModel = new AddToCartOptionDetailModel(image, name, short_description, mrp, ""+discount, price, "" + qty, sku, "" + id,lcalculatePrice);
                                        listModel.setImage(image);
                                        listModel.setMedicineName(name);
                                        listModel.setValue(short_description);
                                        listModel.setMrp(mrp);
                                        listModel.setDiscount(""+discount);
                                        listModel.setPrice("" + price);
                                        listModel.setQty("" + qty);
                                        listModel.setSku(sku);
                                        listModel.setItem_id("" + id);
                                        listModel.setCalculatePrice(lcalculatePrice);
                                        singletonOptionData.option.add(listModel);

                                    }
                                }

                                if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
                                    rlayout_cart.setVisibility(View.GONE);
                                } else {
                                    rlayout_cart.setVisibility(View.VISIBLE);
                                    tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

}
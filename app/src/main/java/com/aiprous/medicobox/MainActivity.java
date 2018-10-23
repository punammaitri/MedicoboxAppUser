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

import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.LoginActivity;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.activity.NotificationActivity;
import com.aiprous.medicobox.activity.SettingActivity;
import com.aiprous.medicobox.adapter.NavAdaptor;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.model.NavItemClicked;

import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.TrackGPS;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


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

        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            rlayout_cart.setVisibility(View.GONE);
        }
        else {
            rlayout_cart.setVisibility(View.VISIBLE);
            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
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

       /* txtUserName.setText(MedicoboxApp.onGetFirstName());
        txtEmail.setText(MedicoboxApp.onGetEmail());*/

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

       /* if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, Constant.Network_Error, Constant.ERROR_TYPE);
            return;*/

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
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_cart))) {
                startActivity(new Intent(this, CartActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_settings))) {
                startActivity(new Intent(mContext, SettingActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_notification))) {
                startActivity(new Intent(mContext, NotificationActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_logout))) {
                logout();
                drawerLayout.closeDrawer(GravityCompat.START);
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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        startActivity(new Intent(mContext, LoginActivity.class));
                        MedicoboxApp.onSaveLoginDetail("", "", "", "", "");
                        finish();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
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
        startActivity(new Intent(this,CartActivity.class));
    }

    private void getLocationFromLatLong()
    {
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
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location is not fetch", "");
        }
    }



}
package com.aiprous.medicobox;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.LoginActivity;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.activity.NotificationActivity;
import com.aiprous.medicobox.adapter.NavAdaptor;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.deliveryboy.AddDeliveryBoyActivity;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.model.NavItemClicked;
import com.aiprous.medicobox.pharmacist.dashboard.DashboardFragment;
import com.aiprous.medicobox.pharmacist.pharmacist_sidemenu.PharmacistSideMenuAdapter;
import com.aiprous.medicobox.pharmacist.productlist.PharmacistProductListActivity;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderActivity;
import com.aiprous.medicobox.pharmacist.sellertransaction.SellerTransactionActivity;
import com.aiprous.medicobox.utils.BaseActivity;

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
    // @BindView(R.id.tvMainToolbarTitle)
    //TextView tvMainToolbarTitle;
    private final String TAG = MainActivity.class.getSimpleName();
    private Unbinder unbinder;
    public static DrawerLayout drawerLayout;
    private HomeFragment homeFragment;
    private DashboardFragment dashboardFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    public static Toolbar toolbarMain;
    private RecyclerView rvForNavigation;
    private NavAdaptor navAdaptor;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    PharmacistSideMenuAdapter mPharmacistSideMenuAdaptor;
    private int flag = 1;

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

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        homeFragment = new HomeFragment(this);
        //dashboardFragment = new DashboardFragment(this);
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

       /* if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, Constant.Network_Error, Constant.ERROR_TYPE);
            return;*/

        if (flag == 1) {
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
                //startActivity(new Intent(mContext, MyOrdersActivity.class));
                //drawerLayout.closeDrawer(GravityCompat.START);
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

        } else {
            if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_dashboard))) {
                drawerLayout.closeDrawer(GravityCompat.START);
                int fragCount = getSupportFragmentManager().getBackStackEntryCount();
                if (fragCount > 0) {
                    for (int i = 0; i < fragCount; i++) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                }
                addFragment();
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_orders))) {
                startActivity(new Intent(mContext, SellerOrderActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_profile))) {
                //startActivity(new Intent(mContext, SellerOrderActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_products))) {
                startActivity(new Intent(this, PharmacistProductListActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_notification))) {
                startActivity(new Intent(this, NotificationActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_add_delivery_boy))) {
                startActivity(new Intent(mContext, AddDeliveryBoyActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.menu_transactions))) {
                startActivity(new Intent(mContext, SellerTransactionActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            } else if (name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_logout))) {
                logout();
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            }
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

    private void navigationItemPharmacist(boolean isBasic) {
        String title[];
        int icon[];

        title = new String[]{
                mContext.getResources().getString(R.string.menu_dashboard),
                mContext.getResources().getString(R.string.menu_orders),
                mContext.getResources().getString(R.string.menu_profile),
                mContext.getResources().getString(R.string.menu_products),
                mContext.getResources().getString(R.string.menu_notification),
                mContext.getResources().getString(R.string.menu_add_delivery_boy),
                mContext.getResources().getString(R.string.menu_transactions),
                mContext.getResources().getString(R.string.txt_logout)};

        icon = new int[]{
                R.drawable.home,
                R.drawable.box,
                R.drawable.user,
                R.drawable.cart,
                R.drawable.bell,
                R.drawable.settings,
                R.drawable.transaction,
                R.drawable.logout,};

        mPharmacistSideMenuAdaptor = new PharmacistSideMenuAdapter(mContext, this, title, icon);
        rvForNavigation.setAdapter(mPharmacistSideMenuAdaptor);
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
        // startActivity(new Intent(this,CartActivity.class));
        // startActivity(new Intent(this,OrderDetailsActivity.class));
        // startActivity(new Intent(this,OrderPlacedActivity.class));
    }

}
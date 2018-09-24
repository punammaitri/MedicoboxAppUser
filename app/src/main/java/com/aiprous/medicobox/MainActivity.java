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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.activity.ForgotPasswordActivity;
import com.aiprous.medicobox.activity.ProductDescriptionActivity;
import com.aiprous.medicobox.activity.ProductDetailActivity;
import com.aiprous.medicobox.activity.SetPasswordActivity;
import com.aiprous.medicobox.adapter.NavAdaptor;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.model.NavItemClicked;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import medicobox.aiprous.com.medicobox.R;

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
    @BindView(R.id.tvMainToolbarTitle)
    TextView tvMainToolbarTitle;
    private final String TAG = MainActivity.class.getSimpleName();
    private Unbinder unbinder;
    public static DrawerLayout drawerLayout;
    private HomeFragment homeFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mContext = this;
    public static Toolbar toolbarMain;
    private RecyclerView rvForNavigation;
    private NavAdaptor navAdaptor;


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

          homeFragment = new HomeFragment(this);
          setDrawerToggle();
          addFragment();
        }

        @Override
        protected void onResume() {
          super.onResume();
            navigationItem(true);
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
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorwhite));
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
            tvMainToolbarTitle.setText(mContext.getResources().getString(R.string.txt_home));
            addFragment();
           // BaseActivity.FirebaseAnalytics(mContext, "Home", "Navigation Menu to Home");
            return;
        }
        else if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_orders))){
             startActivity(new Intent(mContext, ProductDescriptionActivity.class));
             return;
         }
         else if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_account)))
        {
            startActivity(new Intent(mContext, ProductDetailActivity.class));
            return;
        } else if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_cart)))
         {
             return;
         }else if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.txt_notification)))
         {

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
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,
                    R.drawable.ic_menu_camera,};

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

}
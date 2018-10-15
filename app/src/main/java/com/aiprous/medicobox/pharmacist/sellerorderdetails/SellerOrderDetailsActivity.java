package com.aiprous.medicobox.pharmacist.sellerorderdetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice.SellerInvoiceFragment;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder.SellerItemOrderFragment;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.shipment.SellerShipmentFragment;
import com.aiprous.medicobox.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SellerOrderDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @BindView(R.id.txtTitle)
    TextView mTitle;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);
        ButterKnife.bind(this);

        mTitle.setText("Order Details");
        rlayout_cart.setVisibility(View.VISIBLE);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    SellerItemOrderFragment sellerItemOrderFragment = new SellerItemOrderFragment();
                    return sellerItemOrderFragment;

                case 1:
                    SellerInvoiceFragment sellerInvoiceFragment = new SellerInvoiceFragment();
                    return sellerInvoiceFragment;

                case 2:
                    SellerShipmentFragment sellerShipmentFragment = new SellerShipmentFragment();
                    return sellerShipmentFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ITEMS ORDERED";
                case 1:
                    return "INVOICE";
                case 2:
                    return "SHIPMENTS";
            }
            return null;
        }
    }


    @OnClick(R.id.rlayout_back_button)
    public void BackButton() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
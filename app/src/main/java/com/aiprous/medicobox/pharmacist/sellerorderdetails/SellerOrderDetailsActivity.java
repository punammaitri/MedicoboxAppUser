package com.aiprous.medicobox.pharmacist.sellerorderdetails;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice.SellerInvoiceFragment;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder.SellerItemOrderFragment;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.shipment.SellerShipmentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SellerOrderDetailsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @BindView(R.id.txtTitle)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);
        ButterKnife.bind(this);

        mTitle.setText("Order Details");

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

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
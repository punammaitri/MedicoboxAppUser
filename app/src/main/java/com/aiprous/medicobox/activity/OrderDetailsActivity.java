package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.adapter.OrderDetailsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_product)
    RecyclerView rc_product;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_amount_paid)
    TextView tv_amount_paid;
    @BindView(R.id.tv_total_saved)
    TextView tv_total_saved;
    @BindView(R.id.tv_total_product_price)
    TextView tv_total_product_price;
    private Context mContext=this;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<ProductModel> mproductArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        mproductArrayList.add(new ProductModel("Horicks Lite Badam Jar 450 gm","235","box of 450 gm powder","200"));
        mproductArrayList.add(new ProductModel("Horicks Lite Badam Jar 450 gm","235","box of 450 gm powder","200"));
        mproductArrayList.add(new ProductModel("Horicks Lite Badam Jar 450 gm","235","box of 450 gm powder","200"));


        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        //set text default

        tv_total_product_price.setText(mContext.getResources().getString(R.string.Rs)+"350.0");
        tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs)+"350.0");
        tv_price_discount.setText("-"+mContext.getResources().getString(R.string.Rs)+"30");
        tv_amount_paid.setText(mContext.getResources().getString(R.string.Rs)+"350.0");
        tv_total_saved.setText(mContext.getResources().getString(R.string.Rs)+"30.0");




        layoutManager = new LinearLayoutManager(mContext);
        rc_product.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_product.setHasFixedSize(true);
        rc_product.setAdapter(new OrderDetailsAdapter(mContext, mproductArrayList));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }
    @OnClick(R.id.btn_track_order)
    public void trackOrder()
    {
        startActivity(new Intent(this,OrderPlacedActivity.class));
    }

    public static class ProductModel{
        String medicine_name;
        String mrp_price;
        String item_contains;
        String medicine_price;

        public ProductModel(String medicine_name, String mrp_price, String item_contains, String medicine_price) {
            this.medicine_name = medicine_name;
            this.mrp_price = mrp_price;
            this.item_contains = item_contains;
            this.medicine_price = medicine_price;
        }

        public String getMedicine_name() {
            return medicine_name;
        }

        public void setMedicine_name(String medicine_name) {
            this.medicine_name = medicine_name;
        }

        public String getMrp_price() {
            return mrp_price;
        }

        public void setMrp_price(String mrp_price) {
            this.mrp_price = mrp_price;
        }

        public String getItem_contains() {
            return item_contains;
        }

        public void setItem_contains(String item_contains) {
            this.item_contains = item_contains;
        }

        public String getMedicine_price() {
            return medicine_price;
        }

        public void setMedicine_price(String medicine_price) {
            this.medicine_price = medicine_price;
        }
    }
}

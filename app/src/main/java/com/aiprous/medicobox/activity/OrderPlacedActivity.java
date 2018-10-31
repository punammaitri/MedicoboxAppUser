package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.OrderDetailsAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderPlacedActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_order_placed)
    RecyclerView rc_order_placed;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_amount_paid)
    TextView tv_amount_paid;
    @BindView(R.id.tv_total_saved)
    TextView tv_total_saved;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<OrderDetailsActivity.ProductModel> mproductArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        ButterKnife.bind(this);
        init();
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

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        //set text default
        tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_price_discount.setText("-" + mContext.getResources().getString(R.string.Rs) + "30");
        tv_amount_paid.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_total_saved.setText(mContext.getResources().getString(R.string.Rs) + "30.0");


        mproductArrayList.add(new OrderDetailsActivity.ProductModel("Horicks Lite Badam Jar 450 gm", "235", "box of 450 gm powder", "200"));
        mproductArrayList.add(new OrderDetailsActivity.ProductModel("Horicks Lite Badam Jar 450 gm", "235", "box of 450 gm powder", "200"));
        mproductArrayList.add(new OrderDetailsActivity.ProductModel("Horicks Lite Badam Jar 450 gm", "235", "box of 450 gm powder", "200"));


        layoutManager = new LinearLayoutManager(mContext);
        rc_order_placed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_order_placed.setHasFixedSize(true);
        rc_order_placed.setAdapter(new OrderDetailsAdapter(mContext, mproductArrayList));

    }

    @OnClick(R.id.btn_my_orders)
    public void myOrders() {
        startActivity(new Intent(this, MyOrdersActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}

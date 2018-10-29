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
import com.aiprous.medicobox.adapter.OrderSummaryAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.rc_order_summary)
    RecyclerView rc_order_summary;
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_to_be_paid)
    TextView tv_to_be_paid;
    @BindView(R.id.tv_total_savings)
    TextView tv_total_savings;
    @BindView(R.id.tv_free_shipping_note)
    TextView tv_free_shipping_note;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;


    ArrayList<OrderSummaryModel> orderSummaryArrayList = new ArrayList<>();
    private Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        //set Text
        tv_mrp_total.setText(mcontext.getResources().getString(R.string.Rs) + " 350.0");
        tv_price_discount.setText("-" + mcontext.getResources().getString(R.string.Rs) + " 30.0");
        tv_to_be_paid.setText(mcontext.getResources().getString(R.string.Rs) + " 350.0");
        tv_total_savings.setText(mcontext.getResources().getString(R.string.Rs) + " 30.0");
        tv_free_shipping_note.setText("Free shipping for orders above " + mcontext.getResources().getString(R.string.Rs) + "500");


        orderSummaryArrayList.add(new OrderSummaryModel("Horicks Lite BadamJar 450 gm", "box of 450 gm powder", 235, 200));
        orderSummaryArrayList.add(new OrderSummaryModel("Horicks Lite BadamJar 450 gm", "box of 450 gm powder", 235, 200));
        orderSummaryArrayList.add(new OrderSummaryModel("Horicks Lite BadamJar 450 gm", "box of 450 gm powder", 235, 200));
        orderSummaryArrayList.add(new OrderSummaryModel("Horicks Lite BadamJar 450 gm", "box of 450 gm powder", 235, 200));

        rc_order_summary.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_order_summary.setHasFixedSize(true);
        rc_order_summary.setAdapter(new OrderSummaryAdapter(mcontext, orderSummaryArrayList));
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
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_confirm_order)
    public void confirmOrder() {
        startActivity(new Intent(this, PaymentDetailsActivity.class));
    }

    public class OrderSummaryModel {
        String productName;
        String productcontains;
        int product_mrp;
        int product_price;

        public OrderSummaryModel(String productName, String productcontains, int product_mrp, int product_price) {
            this.productName = productName;
            this.productcontains = productcontains;
            this.product_mrp = product_mrp;
            this.product_price = product_price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductcontains() {
            return productcontains;
        }

        public void setProductcontains(String productcontains) {
            this.productcontains = productcontains;
        }

        public int getProduct_mrp() {
            return product_mrp;
        }

        public void setProduct_mrp(int product_mrp) {
            this.product_mrp = product_mrp;
        }

        public int getProduct_price() {
            return product_price;
        }

        public void setProduct_price(int product_price) {
            this.product_price = product_price;
        }
    }
}

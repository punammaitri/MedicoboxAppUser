package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.CartAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_cart)
    RecyclerView rc_cart;
    public static TextView tv_mrp_total;
    public static TextView tv_price_discount;
    public static TextView tv_to_be_paid;
    public static TextView tv_total_saving;
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;
    @BindView(R.id.tv_shipping_note)
    TextView tv_shipping_note;
    public static TextView tv_cart_empty;
    public static NestedScrollView nestedscroll_cart;
    ArrayList<CartActivity.CartModel> cartModelArrayList = new ArrayList<>();
    private Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        tv_mrp_total=(TextView)findViewById(R.id.tv_mrp_total);
        tv_price_discount=(TextView)findViewById(R.id.tv_price_discount);
        tv_to_be_paid=(TextView)findViewById(R.id.tv_to_be_paid);
        tv_total_saving=(TextView)findViewById(R.id.tv_total_saving);
        tv_cart_empty=(TextView)findViewById(R.id.tv_cart_empty);
        nestedscroll_cart=(NestedScrollView)findViewById(R.id.nestedscroll_cart);
        rlayout_cart=(RelativeLayout)findViewById(R.id.rlayout_cart);
        tv_cart_size=(TextView)findViewById(R.id.tv_cart_size);

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm", 235, 200, "box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm", 235, 200, "box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm", 235, 200, "box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm", 235, 200, "box of 450 gm powder"));

        //set text
        //tv_mrp_total.setText(mcontext.getResources().getString(R.string.Rs) + " 350.0");
        //tv_price_discount.setText("-" + mcontext.getResources().getString(R.string.Rs) + " 30.0");
       // tv_to_be_paid.setText(mcontext.getResources().getString(R.string.Rs) + " 350.0");
       // tv_total_saving.setText(mcontext.getResources().getString(R.string.Rs) + " 30.0");
        tv_shipping_note.setText("Free shipping for orders above " + mcontext.getResources().getString(R.string.Rs) + "500");


        rc_cart.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_cart.setHasFixedSize(true);
        rc_cart.setAdapter(new CartAdapter(mcontext, SingletonAddToCart.getGsonInstance().getOptionList()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            tv_cart_empty.setVisibility(View.VISIBLE);
            nestedscroll_cart.setVisibility(View.GONE);
            rlayout_cart.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.rlayout_cart)
    public void ShowCart()
    {
        startActivity(new Intent(this,CartActivity.class));
    }

    @OnClick(R.id.btn_continue_cart)
    public void onClickContinue() {
        startActivity(new Intent(this, OrderSummaryActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    public class CartModel {
        String medicineName;
        int mrp;
        int price;
        String Contains;


        public CartModel(String medicineName, int mrp, int price, String contains) {
            this.medicineName = medicineName;
            this.mrp = mrp;
            this.price = price;
            Contains = contains;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public int getMrp() {
            return mrp;
        }

        public void setMrp(int mrp) {
            this.mrp = mrp;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getContains() {
            return Contains;
        }

        public void setContains(String contains) {
            Contains = contains;
        }
    }
}

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

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.CartAdapter;
import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.utils.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_cart)
    RecyclerView rc_cart;
    ArrayList<CartActivity.CartModel> cartModelArrayList=new ArrayList<>();
    private Context mcontext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm",235,200,"box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm",235,200,"box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm",235,200,"box of 450 gm powder"));
        cartModelArrayList.add(new CartModel("Horicks Lite Badam Jar 450 gm",235,200,"box of 450 gm powder"));

        rc_cart.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_cart.setHasFixedSize(true);
        rc_cart.setAdapter(new CartAdapter(mcontext, cartModelArrayList));

    }
    @OnClick(R.id.btn_continue_cart)
    public void onClickContinue()
    {
        startActivity(new Intent(this,OrderSummaryActivity.class));
    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }

    public class CartModel{
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

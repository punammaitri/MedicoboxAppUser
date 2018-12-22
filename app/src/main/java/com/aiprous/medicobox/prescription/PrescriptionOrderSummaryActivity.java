package com.aiprous.medicobox.prescription;

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
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PrescriptionOrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    RecyclerView rc_medicine_list;
    ArrayList<PrescriptionOrderSummaryActivity.ListModel> mlistModelsArray = new ArrayList<>();

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private String chooseDeliveryAddess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        rc_medicine_list = findViewById(R.id.rc_medicine_list);

        //add static data into List array list
        mlistModelsArray.add(new PrescriptionOrderSummaryActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionOrderSummaryAdapter(mContext, mlistModelsArray));

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


    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick(R.id.btnConfirmOrder)
    public void ButtonConfirmOrder() {
        startActivity(new Intent(this, ThankYouActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    public class ListModel {
        int image;
        String medicineName;
        String value;
        String mrp;
        String discount;
        String price;

        public ListModel(int image, String medicineName, String value, String mrp, String discount, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.value = value;
            this.mrp = mrp;
            this.discount = discount;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
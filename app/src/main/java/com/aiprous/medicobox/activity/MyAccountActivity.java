package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.prescription.PrescriptionChooseDeliveryAddressActivity;
import com.aiprous.medicobox.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountActivity extends AppCompatActivity {


    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.txtEmail)
    TextView txtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        txtEmail.setText(MedicoboxApp.onGetEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show cart size
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

    @OnClick(R.id.tv_edit_profile)
    public void onClickEditProfile() {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.tv_change_delivery_address)
    public void ChangeDeliveryAddress() {
        startActivity(new Intent(this, PrescriptionChooseDeliveryAddressActivity.class));
    }
}

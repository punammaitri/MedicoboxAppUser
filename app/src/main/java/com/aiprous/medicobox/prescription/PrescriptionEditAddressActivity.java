package com.aiprous.medicobox.prescription;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PrescriptionEditAddressActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            rlayout_cart.setVisibility(View.GONE);
        }
        else {
            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }
    @OnClick(R.id.rlayout_cart)
    public void ShowCart()
    {
        startActivity(new Intent(this,CartActivity.class));
    }
    @OnClick(R.id.btn_save)
    public void ButtonSave() {
        startActivity(new Intent(this, PrescriptionBillingAddressActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

}

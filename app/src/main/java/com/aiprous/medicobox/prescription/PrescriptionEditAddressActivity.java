package com.aiprous.medicobox.prescription;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_flat_no)
    EditText edtFlatNo;
    @BindView(R.id.edt_street)
    EditText edtStreet;
    @BindView(R.id.edt_landmark)
    EditText edtLandmark;
    @BindView(R.id.edt_pincde)
    EditText edtPincde;
    @BindView(R.id.edt_state)
    EditText edtState;
    @BindView(R.id.edt_city)
    EditText edtCity;

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

    @OnClick(R.id.btn_save)
    public void ButtonSave() {
        String lname=edtName.getText().toString();
        String lPhone=edtPhone.getText().toString();
        String lFlatNo=edtFlatNo.getText().toString();
        String lStreet=edtStreet.getText().toString();
        String lLandmark=edtLandmark.getText().toString();
        String lPincode=edtPincde.getText().toString();
        String lState=edtState.getText().toString();
        String lCity=edtCity.getText().toString();


        if(lname.length()==0&&lPhone.length()==0&&lFlatNo.length()==0&&lStreet.length()==0&&lLandmark.length()==0&&lPincode.length()==0&&lState.length()==0&&lCity.length()==0)
        {
            Toast.makeText(this, "All fields are required ", Toast.LENGTH_SHORT).show();
        }else if(lname.length()==0){
            edtName.setError("Please enter name");
        }else if(lPhone.length()==0){
            edtPhone.setError("Please enter phone number");
        }else if(lFlatNo.length()==0){
            edtFlatNo.setError("Please enter flat number or building name");
        }else if(lStreet.length()==0){
            edtStreet.setError("Please enter Street / Road Name");
        }else if(lLandmark.length()==0){
            edtLandmark.setError("Please enter landmark");
        }else if(lPincode.length()==0){
            edtPincde.setError("Please enter pincode");
        }else if(lState.length()==0){
            edtState.setError("Please enter state");
        }else if(lCity.length()==0){
            edtCity.setError("Please enter city");
        }else if(lPhone.length()<=9){
            edtPhone.setError("Phone number should be 10 digit");
        }else {
            startActivity(new Intent(this, PrescriptionBillingAddressActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }

    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

}

package com.aiprous.medicobox.activity;

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
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R.id.edt_last_name)
    EditText edtLastName;
    @BindView(R.id.edt_mobile_no)
    EditText edtMobileNo;
    @BindView(R.id.edt_email_id)
    EditText edtEmailId;
    @BindView(R.id.edt_gender)
    EditText edtGender;
    @BindView(R.id.edt_dob)
    EditText edtDob;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
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
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave()
    {
        String lfirst_name=edtFirstName.getText().toString();
        String lLast_Name=edtLastName.getText().toString();
        String lMobile_no=edtMobileNo.getText().toString();
        String lEmail_id=edtEmailId.getText().toString();
        String lDob=edtDob.getText().toString();
        String lPassword=edtPassword.getText().toString();
        String lConfirm_password=edtConfirmPassword.getText().toString();

        if(lfirst_name.length()==0) {
            edtFirstName.setError("Please enter first name");
        }else if(lLast_Name.length()==0){
            edtLastName.setError("Please enter last name");
        }else if(lMobile_no.length()==0){
            edtMobileNo.setError("Please enter mobile number");
        }else if(lEmail_id.length()==0) {
            edtEmailId.setError("Please enter email id");
        }else if(lPassword.length()==0){
            edtPassword.setError("Please enter password");
        }else if(lConfirm_password.length()==0){
            edtConfirmPassword.setError("Please enter confirm password");
        }else if(!lPassword.equals(lConfirm_password)){
            Toast.makeText(this, "Password and confirm password should be same", Toast.LENGTH_SHORT).show();
        }else {
            //API call
        }

    }

}

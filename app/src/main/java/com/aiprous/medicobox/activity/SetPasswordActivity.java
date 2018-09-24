package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;

public class SetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_proceed_set_password)
    Button btn_proceed_set_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_proceed_set_password)
    public void onCickSetPassword()
    {
        startActivity(new Intent(this,ForgotPasswordActivity.class));
        finish();
    }
}

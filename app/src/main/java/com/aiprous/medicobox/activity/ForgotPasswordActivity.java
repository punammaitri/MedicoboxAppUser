package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_set_password)
    Button btn_set_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_set_password)
    public void onClickSetPassword()
    {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}

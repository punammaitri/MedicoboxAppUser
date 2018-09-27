package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.aiprous.medicobox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_set_password)
    Button btn_set_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick(R.id.btn_set_password)
    public void onClickSetPassword()
    {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}

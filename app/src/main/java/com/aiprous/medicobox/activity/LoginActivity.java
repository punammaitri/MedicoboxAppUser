package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.tv_sign_up_here)
    TextView tv_sign_up_here;
    //@BindView(R.id.btn_sign_in_withotp)
    //Button btn_sign_in_withotp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @OnClick(R.id.btn_signup)
    public void onClickSignUp()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    @OnClick(R.id.tv_forgot_password)
    public void onClickPassword()
    {
        startActivity(new Intent(this,SetPasswordActivity.class));
        finish();
    }
    @OnClick(R.id.tv_sign_up_here)
    public void onCLickSignUpHere()
    {
        startActivity(new Intent(this,SignUpActivity.class));
        finish();
    }
    @OnClick(R.id.tv_sign_in_withotp)
    public void onClickSignInWithOtp()
    {
        startActivity(new Intent(this,OTPActivity.class));
        finish();
    }
}

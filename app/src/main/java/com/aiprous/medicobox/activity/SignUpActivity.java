package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aiprous.medicobox.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.tv_sign_in_here)
    TextView tv_sign_in_here;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    @OnClick(R.id.tv_sign_in_here)
    public void onClickSignInHere(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}

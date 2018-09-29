package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Build;
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
        init();
    }

    private void init() {
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
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

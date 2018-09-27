package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OTPActivity extends AppCompatActivity {

    @BindView(R.id.btn_otp_proceed)
    Button btn_otp_proceed;
    @BindView(R.id.edt_one)
    EditText edt_one;
    @BindView(R.id.edt_two)
    EditText edt_two;
    @BindView(R.id.edt_there)
    EditText edt_there;
    @BindView(R.id.edt_four)
    EditText edt_four;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));



        edt_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    edt_two.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        edt_two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    edt_there.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        edt_there.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    edt_four.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
    }

    @OnClick(R.id.btn_otp_proceed)
    public void onClickOtpProceesd()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.utils.BaseActivity;

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
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        edt_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    edt_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        edt_two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    edt_there.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        edt_there.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    edt_four.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    @OnClick(R.id.btn_otp_proceed)
    public void onClickOtpProceesd() {
        String lOne = edt_one.getText().toString();
        String ltwo = edt_two.getText().toString();
        String lThree = edt_there.getText().toString();
        String lfour = edt_four.getText().toString();
        if (lOne.length() == 0 && ltwo.length() == 0 && lThree.length() == 0 && lfour.length() == 0) {
            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

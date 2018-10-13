package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.utils.APIService;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.aiprous.medicobox.utils.IRetrofit;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_set_password)
    Button btn_set_password;
    CustomProgressDialog mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        mAlert = CustomProgressDialog.getInstance();
        JsonObject jsonObject = new JsonObject();
        //Add Json Object
        jsonObject.addProperty("confirmationKey", "1");

        AttemptToConfirmKey(jsonObject);
    }

    private void AttemptToConfirmKey(JsonObject jsonObject) {

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
            return;
        }
        mAlert.onShowProgressDialog(this, false);

        try {
            // Using the Retrofit
            IRetrofit jsonPostService = APIService.createService(IRetrofit.class, "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me/");
            Call<JsonObject> call = jsonPostService.keyConfirmation(jsonObject);
            call.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() == 200) {
                        BaseActivity.printLog("response-success : ", response.body().toString());
                        mAlert.onShowProgressDialog(ForgotPasswordActivity.this, false);
                        startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordActivity.class));
                        finish();
                    } else if (response.code() == 404) {
                        Toast.makeText(ForgotPasswordActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                        mAlert.onShowProgressDialog(ForgotPasswordActivity.this, false);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("response-failure", call.toString());
                    mAlert.onShowProgressDialog(ForgotPasswordActivity.this, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btn_set_password)
    public void onClickSetPassword() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

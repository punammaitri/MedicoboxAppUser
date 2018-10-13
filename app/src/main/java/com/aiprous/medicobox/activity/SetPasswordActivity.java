package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import static com.aiprous.medicobox.utils.BaseActivity.showToast;


public class SetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.btn_proceed_set_password)
    Button btn_proceed_set_password;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    private String lEmail;
    CustomProgressDialog mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        mAlert = CustomProgressDialog.getInstance();

    }


    @OnClick(R.id.btn_proceed_set_password)
    public void onCickSetPassword() {

        lEmail = edtEmail.getText().toString().trim();

        if (lEmail.length() > 0) {
            JsonObject jsonObject = new JsonObject();
            //Add Json Object
            jsonObject.addProperty("customerEmail", lEmail);
            jsonObject.addProperty("websiteId", 1);
            AttemptSetPassword(jsonObject, lEmail, 1);
        } else if (lEmail.length() == 0) {
            showToast(this, getResources().getString(R.string.error_email));
        }

    }

    private void AttemptSetPassword(JsonObject jsonObject, String lEmail, int i) {

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
            return;
        }
        mAlert.onShowProgressDialog(this, true);

        try {
            // Using the Retrofit
            IRetrofit jsonPostService = APIService.createService(IRetrofit.class, "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/");
            Call<JsonPrimitive> call = jsonPostService.emailAvailable(jsonObject);
            call.enqueue(new Callback<JsonPrimitive>() {

                @Override
                public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {

                    if (response.code() == 200) {
                        BaseActivity.printLog("response-success : ", response.body().toString());
                        mAlert.onShowProgressDialog(SetPasswordActivity.this, false);
                        startActivity(new Intent(SetPasswordActivity.this, ForgotPasswordActivity.class));
                        finish();
                    } else if (response.code() == 401) {
                        mAlert.onShowProgressDialog(SetPasswordActivity.this, false);
                    }
                }

                @Override
                public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                    Log.e("response-failure", call.toString());
                    mAlert.onShowProgressDialog(SetPasswordActivity.this, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

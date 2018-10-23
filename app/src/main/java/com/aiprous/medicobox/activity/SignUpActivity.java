package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.register.RegisterModel;
import com.aiprous.medicobox.utils.APIService;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.aiprous.medicobox.utils.IRetrofit;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.passwordValidation;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.tv_sign_in_here)
    TextView tv_sign_in_here;
    CustomProgressDialog mAlert;
    @BindView(R.id.edt_first_name)
    EditText edt_first_name;
    @BindView(R.id.edt_last_name)
    EditText edt_last_name;
    @BindView(R.id.edt_confirm_password)
    EditText edt_confirm_password;
    @BindView(R.id.edt_mobile)
    EditText edtMobile;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    public ArrayList<String> mTempArray = new ArrayList<String>();
    Context mContext = this;
    private RegisterModel detailModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        mAlert = CustomProgressDialog.getInstance();
    }

    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp() {

        String lName = edt_first_name.getText().toString().trim();
        String lMobile = edtMobile.getText().toString().trim();
        String lEmail = edtEmail.getText().toString().trim();
        String lPass = edtPassword.getText().toString().trim();

        String emailPattern = "[A-Za-z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (edt_first_name.getText().length() <= 2) {
            edt_first_name.setError("Name must be greater than 2 character");
        } else if (edtMobile.getText().length() <= 9) {
            edtMobile.setError("Mobile number must be greater 10 digit");
        } else if (!lEmail.matches(emailPattern)) {
            edtEmail.setError("Invalid email address");
        } else if (passwordValidation(mContext, lPass, edtPassword)) {
            JsonObject jsonObject = new JsonObject();
            JsonObject payerReg = new JsonObject();
            payerReg.addProperty("email", lEmail);
            payerReg.addProperty("firstname", lName);
            payerReg.addProperty("lastname", lName);
            payerReg.addProperty("store_id", 0);

            //Add Json Object
            jsonObject.add("customer", payerReg);
            jsonObject.addProperty("password", lPass);
            AttemptToRegister(jsonObject);
        }
    }

    private void AttemptToRegister(JsonObject jsonObject) {
        mAlert.onShowProgressDialog(this, true);
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
            return;
        }
        BaseActivity.printLog("response-json : ", jsonObject.toString());

        try {
            // Using the Retrofit
            IRetrofit jsonPostService = APIService.createService(IRetrofit.class, "http://user8.itsindev.com/medibox/index.php/rest/V1/");
            Call<JsonObject> call = jsonPostService.userRegistration(jsonObject);
            call.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    try {
                        if (response.body() != null) {
                            if (response.code() == 400) {
                                mAlert.onShowProgressDialog(SignUpActivity.this, false);
                            } else if (response.code() == 200) {

                                String getId = (response.body().get("id").getAsString());
                                String getFirstname = (response.body().get("firstname").getAsString());
                                String getLastname = (response.body().get("lastname").getAsString());
                                String getEmail = (response.body().get("email").getAsString());

                                BaseActivity.printLog("response-success : ", response.body().toString());
                                mAlert.onShowProgressDialog(SignUpActivity.this, false);
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class)
                                        .putExtra("id", "" + getId)
                                        .putExtra("firstname", "" + getFirstname)
                                        .putExtra("lastname", "" + getLastname)
                                        .putExtra("email", "" + getEmail));

                                MedicoboxApp.onSaveLoginDetail(getId, getFirstname, getLastname, "", getEmail);


                                //String mAllData= String.valueOf(response.body());
                           /* Gson gson = new Gson();
                            RegisterModel mRegistermodel = gson.fromJson(response.body(), RegisterModel.class);
                            Log.e("jj", "" + mRegistermodel.getAddresses());*/

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("response-failure", call.toString());
                    mAlert.onShowProgressDialog(SignUpActivity.this, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_sign_in_here)
    public void onClickSignInHere() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

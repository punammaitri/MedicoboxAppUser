package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.login.LoginModel;
import com.aiprous.medicobox.register.RegisterServiceProvider;
import com.aiprous.medicobox.utils.APICallback;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.BaseServiceResponseModel;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.aiprous.medicobox.utils.PrintUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.showToast;


public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @BindView(R.id.tv_sign_in_here)
    TextView tv_sign_in_here;
    RegisterServiceProvider registerServiceProvider;
    CustomProgressDialog mAlert;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_mobile)
    EditText edtMobile;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    private JSONObject requestJson;
    public ArrayList<String> mTempArray = new ArrayList<String>();

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
        mAlert = CustomProgressDialog.getInstance();
        registerServiceProvider = new RegisterServiceProvider(this);
        requestJson = new JSONObject();
    }

    @OnClick(R.id.btn_sign_up)
    public void onClickSignUp() {

        String lName = edtName.getText().toString().trim();
        String lMobile = edtMobile.getText().toString().trim();
        String lEmail = edtEmail.getText().toString().trim();
        String lPass = edtPassword.getText().toString().trim();

        if (lEmail.length() > 0 && lPass.length() > 0) {

            try {
                // We add the object to the main object
                JSONObject jsonAdd = new JSONObject();
                jsonAdd.put("email", "peterss8@mitash.com");
                jsonAdd.put("firstname", "Peter");
                jsonAdd.put("lastname", "s");
                jsonAdd.put("store_id", 0);

                requestJson.put("customer", jsonAdd);
                requestJson.put("password", "Peter.s@888");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AttemptToRegister(requestJson);
        } else if (lEmail.length() > 0) {
            showToast(this, getResources().getString(R.string.error_email));
        } else if (lPass.length() > 0) {
            showToast(this, getResources().getString(R.string.error_pass));
        } else {
            showToast(this, getResources().getString(R.string.error_user_pass));
        }

    }

    private void AttemptToRegister(JSONObject requestJson) {
        mAlert.onShowProgressDialog(this, true);
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
            return;
        }
        BaseActivity.printLog("json : ", requestJson.toString());

        registerServiceProvider.signUpData(requestJson.toString(), new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    int Status = (((LoginModel) serviceResponse).getStatus());
                    String message = ((LoginModel) serviceResponse).getMessage();
                    //ArrayList<FeatureModel.Data> mArrHospitalNames = ((FeatureModel) serviceResponse).getData();
                    if (Status == 200) {
                        Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        mAlert.onShowProgressDialog(SignUpActivity.this, false);
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    } else {
                        mAlert.onShowToastNotification(SignUpActivity.this, message);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(SignUpActivity.this, false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    if (apiErrorModel != null) {
                        PrintUtil.showToast(SignUpActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(SignUpActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(SignUpActivity.this);
                } finally {
                    mAlert.onShowProgressDialog(SignUpActivity.this, false);
                }
            }
        });
    }

    @OnClick(R.id.tv_sign_in_here)
    public void onClickSignInHere() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

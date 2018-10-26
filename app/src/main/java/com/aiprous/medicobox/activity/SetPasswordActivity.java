package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ISEMAILAVAILABLE;
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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("customerEmail", lEmail);
                jsonObject.put("websiteId", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AttemptSetPassword(jsonObject);
        } else if (lEmail.length() == 0) {
            showToast(this, getResources().getString(R.string.error_email));
        }

    }

    private void AttemptSetPassword(JSONObject jsonObject) {

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            mAlert.onShowProgressDialog(this, true);

            AndroidNetworking.post(ISEMAILAVAILABLE)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {

                                JSONObject jsonResponse = new JSONObject(response.toString());
                                boolean success = jsonResponse.getBoolean("success");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mAlert.onShowProgressDialog(SetPasswordActivity.this, false);
                            startActivity(new Intent(SetPasswordActivity.this, ForgotPasswordActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            mAlert.onShowProgressDialog(SetPasswordActivity.this, false);
                            /*startActivity(new Intent(SetPasswordActivity.this, ForgotPasswordActivity.class));
                            finish();*/
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }
}

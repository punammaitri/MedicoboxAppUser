package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.BuildConfig;
import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETUSERINFO;
import static com.aiprous.medicobox.utils.APIConstant.LOGIN;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.isValidEmailId;
import static com.aiprous.medicobox.utils.BaseActivity.showToast;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    @BindView(R.id.btn_signup)
    Button btn_signup;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.tv_sign_up_here)
    TextView tv_sign_up_here;
    @BindView(R.id.edt_mobile_email)
    EditText edtMobileEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;

    private String gmailProfileUrl;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String mgoogleusername, twitterProfileImageUrl, googleUsername, googleLastname, getFirebaseToken;
    GoogleApiClient mGoogleApiClient;
    private String lLoginwithGooglegmailId;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;
    private String mfacebookFlag = "facebook";
    private String mgmailFlag = "gmail";
    LoginButton btnfblogin;
    LinearLayout fb_login_layout;
    LinearLayout goglayout;
    CustomProgressDialog mAlert;
    private String lEmailMobile;
    private String lPass;
    private Context mContext = this;
    private String getMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Facebook initialize
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        mAlert = CustomProgressDialog.getInstance();

        //firebaseAnalytics(this.getClass().getSimpleName());
        btnfblogin = (LoginButton) findViewById(R.id.btnfblogin);
        goglayout = (LinearLayout) findViewById(R.id.goglayout);
        fb_login_layout = (LinearLayout) findViewById(R.id.fb_login_layout);
        goglayout.setOnClickListener(this);

        /////// Configure sign-in to request the user's ID, email address, and basic////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        ///// Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.///
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fb_login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == fb_login_layout) {
                    btnfblogin.performClick();
                }
            }
        });

        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };

        ////////////set the permissions for facebook/////////////////////
        btnfblogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    Toast.makeText(LoginActivity.this, "User logged out successfully", Toast.LENGTH_SHORT).show();
                    //rlProfileArea.setVisibility(View.GONE);
                }
            }
        };

        // Callback registration
        btnfblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getProfileData();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "User cancel login");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Problem for login");
            }
        });

    }

    public void getProfileData() {
        try {
            accessToken = AccessToken.getCurrentAccessToken();
            //rlProfileArea.setVisibility(View.VISIBLE);
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.d(TAG, "Graph Object :" + object);
                            try {
                                String name = object.getString("name");
                                if (!name.isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "Login with facebook is successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login with facebook is failed", Toast.LENGTH_SHORT).show();
                                }
                                Log.d(TAG, "Name :" + name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,gender,email");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            Log.e(TAG, "display name: " + googleSignInAccount.getDisplayName());
            try {
                mgoogleusername = googleSignInAccount.getDisplayName();
                googleUsername = googleSignInAccount.getGivenName();
                googleLastname = googleSignInAccount.getFamilyName();
                lLoginwithGooglegmailId = googleSignInAccount.getEmail();

                if (googleSignInAccount.getPhotoUrl() != null) {
                    gmailProfileUrl = googleSignInAccount.getPhotoUrl().toString();
                } else {
                    gmailProfileUrl = String.valueOf(Uri.parse("R.drawable.profileimage"));
                }
                //gmailProfileUrl = "https://pikmail.herokuapp.com/" + lLoginwithGooglegmailId + "?size=50";
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    public void printhashkey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.aiprous.medicobox", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    protected void onResume() {
        printhashkey();
        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        }
        super.onResume();
    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            Toast.makeText(this, "Login With Gmail is successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            //callSocialMedia(lLoginwithGooglegmailId, "social_media", getFirebaseToken, googleUsername, googleUsername, googleLastname, gmailProfileUrl, "gmail");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.goglayout) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
            googleSignIn();
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @OnClick(R.id.tv_forgot_password)
    public void onClickPassword() {
        startActivity(new Intent(this, MobileNumberActivity.class).putExtra("flag", "forgotpassword"));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.tv_sign_up_here)
    public void onCLickSignUpHere() {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.tv_sign_in_withotp)
    public void onClickSignInWithOtp() {
        // startActivity(new Intent(this, OTPActivity.class));
        startActivity(new Intent(this, MobileNumberActivity.class).putExtra("flag", "SignWithOTP"));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.btn_signup)
    public void onViewClicked() {

        lEmailMobile = edtMobileEmail.getText().toString().trim();
        lPass = edtPassword.getText().toString().trim();

        if (lEmailMobile.length() == 0) {
            showToast(this, getResources().getString(R.string.error_email));
        } else if (lPass.length() == 0) {
            showToast(this, getResources().getString(R.string.error_pass));
        } else {
            if (lEmailMobile.toString().matches("[a-zA-Z ]+")) {
                if (!isValidEmailId(edtMobileEmail)) {
                    showToast(this, "Please enter valid email id");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", lEmailMobile);
                        jsonObject.put("password", lPass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!isNetworkAvailable(this)) {
                        CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                    } else {
                        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
                        AttemptLogin(jsonObject);
                    }
                }
            } else {
                if (edtMobileEmail.length() <= 9) {
                    showToast(this, "Mobile number must be  10 digit");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", lEmailMobile);
                        jsonObject.put("password", lPass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (!isNetworkAvailable(this)) {
                        CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                    } else {
                        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
                        AttemptLogin(jsonObject);
                    }
                }
            }
        }
    }

    private void AttemptLogin(JSONObject jsonObject) {
        AndroidNetworking.post(LOGIN)
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            String getResponse = response.getString("response");
                            if (getResponse.contains("{")) {
                                // for removing braces
                                CustomProgressDialog.getInstance().dismissDialog();
                                String afterRemoveBrace = getResponse.replace("{", "").replace("}", "");
                                StringTokenizer getMessage = new StringTokenizer(afterRemoveBrace, ":");
                                String msg = getMessage.nextToken();
                                String error_msg = getMessage.nextToken();
                                Toast.makeText(LoginActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!isNetworkAvailable(LoginActivity.this)) {
                                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                                } else {
                                    getUserInfo(getResponse);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(LoginActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void getUserInfo(final String bearerToken) {
        AndroidNetworking.get(GETUSERINFO)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject responseObject = getAllResponse.get("response").getAsJsonObject();
                            String status = responseObject.get("status").getAsString();
                            if (status.equals("success")) {
                                JsonObject responseObjects = responseObject.get("data").getAsJsonObject();
                                String getId = responseObjects.get("id").getAsString();
                                String getGroupId = responseObjects.get("group_id").getAsString();
                                String getEmail = responseObjects.get("email").getAsString();
                                String getFirstname = responseObjects.get("firstname").getAsString();
                                String getLastname = responseObjects.get("lastname").getAsString();
                                String getStoreId = responseObjects.get("store_id").getAsString();
                                String getWebsiteId = responseObjects.get("website_id").getAsString();
                                JsonArray custom_attributes_array = responseObjects.get("custom_attributes").getAsJsonArray();

                                if (custom_attributes_array != null) {
                                    for (int j = 0; j < custom_attributes_array.size(); j++) {
                                        JsonObject customObject = custom_attributes_array.get(j).getAsJsonObject();
                                        getMobileNumber = customObject.get("value").getAsString();
                                    }
                                }
                                MedicoboxApp.onSaveLoginDetail(getId, bearerToken, getFirstname, getLastname, getMobileNumber, getEmail, getStoreId);
                                Toast.makeText(mContext, "Login successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                        .putExtra("email", "" + getEmail));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(LoginActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }
}
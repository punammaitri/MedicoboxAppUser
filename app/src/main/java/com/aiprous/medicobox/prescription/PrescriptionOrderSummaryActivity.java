package com.aiprous.medicobox.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GET_UPLOADED_PRESCRIPTION;
import static com.aiprous.medicobox.utils.APIConstant.UPLOADED_PRESCRIPTION_PLACE_ORDER;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class PrescriptionOrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    RecyclerView rc_medicine_list;
    ArrayList<ImageUrlModel> mlistModelsArray = new ArrayList<>();
    ArrayList<FinalOrderImageModel> mFinalModelsArray = new ArrayList<>();
    @BindView(R.id.txtusername)
    TextView txtusername;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtTelephone)
    TextView txtTelephone;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private String chooseDeliveryAddess;
    private String mAddressId;
    private String choose_delivery_address;
    private String PrescriptionImageModel;
    private String getPatientName;
    private String getAdditionalComment;
    private String getAddress;
    private String getMobile;
    private String getFullname;
    ArrayList<PrescriptionModel> detailModelArrayList;
    private String getDose="";
    private String getFlag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        rc_medicine_list = findViewById(R.id.rc_medicine_list);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.VISIBLE);
        } else {
            rlayout_cart.setVisibility(View.VISIBLE);
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (getIntent().getStringExtra("mAddressId") != null) {
            mAddressId = getIntent().getStringExtra("mAddressId");
            choose_delivery_address = getIntent().getStringExtra("choose_delivery_address");
            getPatientName = getIntent().getStringExtra("getPatientName");
            getAdditionalComment = getIntent().getStringExtra("getAdditionalComment");
            getAddress = getIntent().getStringExtra("getAddress");
            getMobile = getIntent().getStringExtra("getMobile");
            getFullname = getIntent().getStringExtra("getFullname");
            getDose = getIntent().getStringExtra("getDose");
            getFlag = getIntent().getStringExtra("getFlag");

            txtAddress.setText(getAddress);
            txtusername.setText(getFullname);
            txtTelephone.setText(getMobile);
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            GetAllPrescriptionAPI();
        }
    }

    private void GetAllPrescriptionAPI() {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GET_UPLOADED_PRESCRIPTION)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.getString("status").equals("success")) {
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JsonObject responseObject = getAllResponse.get("data").getAsJsonObject();

                                if (responseObject.has("images")) {
                                    mlistModelsArray.clear();
                                    JsonArray getImageUrl = responseObject.get("images").getAsJsonArray();
                                    for (int i = 0; i < getImageUrl.size(); i++) {
                                        String getUrl = getImageUrl.get(i).getAsString();
                                        ImageUrlModel imageUrlModel = new ImageUrlModel(getUrl);
                                        imageUrlModel.setImageUrl(getUrl);
                                        mlistModelsArray.add(imageUrlModel);
                                    }
                                    setListAdapter(mlistModelsArray);
                                }

                                CustomProgressDialog.getInstance().dismissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionOrderSummaryActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void setListAdapter(ArrayList<ImageUrlModel> mlistModelsArray) {
        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionOrderSummaryAdapter(PrescriptionOrderSummaryActivity.this, mlistModelsArray));
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick(R.id.btnConfirmOrder)
    public void ButtonConfirmOrder() {
        CallFinalOrder();
    }

    private void CallFinalOrder() {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("patient_name", getPatientName);
            jsonObject.put("additional_comment", getAdditionalComment);
            jsonObject.put("shipping_address", mAddressId);
            jsonObject.put("billing_address", "");
            jsonObject.put("order_type", getFlag);
            jsonObject.put("duration_days", getDose);
            Log.e("data", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(UPLOADED_PRESCRIPTION_PLACE_ORDER)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.has("status")) {
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                String status = getAllResponse.get("status").getAsString();

                                if (status.equals("success")){
                                    startActivity(new Intent(PrescriptionOrderSummaryActivity.this, ThankYouActivity.class));
                                    Toast.makeText(mContext, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                }
                                /*if (status.equals("success")) {
                                    mFinalModelsArray.clear();
                                    JsonObject data = getAllResponse.get("data").getAsJsonObject();
                                    String id = data.get("id").getAsString();
                                    String order_type = data.get("order_type").getAsString();
                                    String patient_name = data.get("patient_name").getAsString();
                                    String additional_comment = data.get("additional_comment").getAsString();
                                    String shiping_address = data.get("shiping_address").getAsString();
                                    String billing_address = data.get("billing_address").getAsString();
                                    String final_cart = data.get("final_cart").getAsString();
                                    String cart_total = data.get("cart_total").getAsString();
                                    JsonArray imagesjsonArray = data.get("images").getAsJsonArray();

                                    for (int i = 0; i < imagesjsonArray.size(); i++) {
                                        String getUrl = imagesjsonArray.get(i).getAsString();
                                        FinalOrderImageModel imageUrlModel = new FinalOrderImageModel(getUrl);
                                        imageUrlModel.setImageUrl(getUrl);
                                        mFinalModelsArray.add(imageUrlModel);
                                    }

                                    startActivity(new Intent(PrescriptionOrderSummaryActivity.this, ThankYouActivity.class));
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                }*/


                                CustomProgressDialog.getInstance().dismissDialog();

                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionOrderSummaryActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    public class ListModel {
        int image;
        String medicineName;
        String value;
        String mrp;
        String discount;
        String price;

        public ListModel(int image, String medicineName, String value, String mrp, String discount, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.value = value;
            this.mrp = mrp;
            this.discount = discount;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}
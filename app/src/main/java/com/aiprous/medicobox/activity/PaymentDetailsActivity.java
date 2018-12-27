package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ADD_TO_CART_ORDER_PLACE;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class PaymentDetailsActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_to_be_paid)
    TextView tv_to_be_paid;
    @BindView(R.id.tv_total_savings)
    TextView tv_total_savings;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private Context mContext = this;
    private JSONArray jsonArray;
    private String address_id;
    ArrayList<CartModel.Response> cartList;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        //set text
        tv_mrp_total.setText(this.getResources().getString(R.string.Rs) + " 350.0");
        tv_price_discount.setText("-" + this.getResources().getString(R.string.Rs) + " 30.0");
        tv_to_be_paid.setText(this.getResources().getString(R.string.Rs) + " 350.0");
        tv_total_savings.setText(this.getResources().getString(R.string.Rs) + "30.0");
    }

    @Override
    protected void onResume() {
        super.onResume();

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().dismissDialog();
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //get cart items through api
            CallOrderPlaceAPI();
        }

        // for passing cart model
        if (getIntent().getStringExtra("items") != null) {
            address_id = getIntent().getStringExtra("address_id");
            String cartListAsString = getIntent().getStringExtra("items");
            image = getIntent().getStringExtra("image");
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartModel.Response>>() {
            }.getType();
            cartList = gson.fromJson(cartListAsString, type);

            for (CartModel.Response cars : cartList) {
                Log.e("Cart Data", cars.getId());
            }
        }

    }

    private void CallOrderPlaceAPI() {
        //jsonArray = new JSONArray(mStreetArray);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address_id", address_id);
            jsonObject.put("items", cartList);
            jsonObject.put("shipping_method", "");
            jsonObject.put("payment_method", "cash");
            jsonObject.put("image", image);
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().dismissDialog();
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            callAPI(jsonObject);
        }
    }

    private void callAPI(JSONObject jsonObject) {
        AndroidNetworking.post(ADD_TO_CART_ORDER_PLACE)
                .addJSONObjectBody(jsonObject) // posting json
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                            String status = responseArray.get("status").getAsString();

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        //Toast.makeText(MyOrdersActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    @OnClick({R.id.searchview_medicine, R.id.tv_place_order, R.id.rlayout_back_button, R.id.rlayout_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchview_medicine:
                startActivity(new Intent(this, SearchViewActivity.class));
                break;
            case R.id.tv_place_order:
                startActivity(new Intent(this, OrderPlacedActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
            case R.id.rlayout_cart:
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }
}

package com.aiprous.medicobox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ORDER_CANCEL;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class CancelOrderActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.tv_order_id)
    TextView tv_order_id;
    private String order_id;
    private CancelOrderActivity mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getStringExtra("order_id") != null) {
            order_id = getIntent().getStringExtra("order_id");
            tv_order_id.setText("Order ID: " + order_id);
        }

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.VISIBLE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

    @OnClick(R.id.btn_continue)
    public void onClickCancelOrder() {

        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("order_id", order_id);
                Log.e("data", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(ORDER_CANCEL)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            String statusWithQuotes = getAllResponse.get("status").toString();
                            String statusWithoutQuotes = statusWithQuotes.replaceAll("^\"|\"$", "");

                            if (statusWithoutQuotes.equals("success")) {
                                Toast.makeText(mContext, "Order cancel successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Order not cancel", Toast.LENGTH_SHORT).show();
                            }
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
    }
}

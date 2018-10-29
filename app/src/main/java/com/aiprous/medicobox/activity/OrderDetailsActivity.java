package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.ProductListAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.ProductsModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class OrderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_product)
    RecyclerView rc_product;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_amount_paid)
    TextView tv_amount_paid;
    @BindView(R.id.tv_total_saved)
    TextView tv_total_saved;
    @BindView(R.id.tv_total_product_price)
    TextView tv_total_product_price;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    CustomProgressDialog mAlert;
    ArrayList<ProductsModel> mProductsModel = new ArrayList<ProductsModel>();
    ArrayList<ProductModel> mproductArrayList = new ArrayList<ProductModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);
        mAlert = CustomProgressDialog.getInstance();

        //set text default
        tv_total_product_price.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_price_discount.setText("-" + mContext.getResources().getString(R.string.Rs) + "30");
        tv_amount_paid.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_total_saved.setText(mContext.getResources().getString(R.string.Rs) + "30.0");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        //Add Json Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category_id", "38");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getAllproducts(jsonObject);
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
    }


    private void getAllproducts(JSONObject jsonObject) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            AndroidNetworking.post(GETPRODUCT)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            try {
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                                JSONArray getAllProductList = getAllObject.getJSONArray("response");//get the array with the key "response"

                                if (getAllProductList != null) {
                                    mProductsModel.clear();
                                    for (int i = 0; i < getAllProductList.length(); i++) {
                                        String id = getAllProductList.getJSONObject(i).get("id").toString();
                                        String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                        String title = getAllProductList.getJSONObject(i).get("title").toString();
                                        String price = getAllProductList.getJSONObject(i).get("price").toString();
                                        String imageUrl = getAllProductList.getJSONObject(i).get("image").toString();

                                        ProductsModel registerModel = new ProductsModel(id, sku, title, price, imageUrl);
                                        registerModel.setId(id);
                                        registerModel.setSku(sku);
                                        registerModel.setTitle(title);
                                        registerModel.setPrice(price);
                                        registerModel.setImage_url(imageUrl);
                                        mProductsModel.add(registerModel);
                                    }
                                }
                                CustomProgressDialog.getInstance().dismissDialog();
                                layoutManager = new LinearLayoutManager(mContext);
                                rc_product.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                                rc_product.setHasFixedSize(true);
                                rc_product.setAdapter(new ProductListAdapter(mContext, mProductsModel));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }

        /*try {
            // Using the Retrofit
            IRetrofit jsonPostService = APIService.createService(IRetrofit.class, "http://user8.itsindev.com/medibox/API/");
            Call<JsonObject> call = jsonPostService.getProducts(jsonObject);
            call.enqueue(new Callback<JsonObject>() {

                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    try {
                        if (response.body() != null) {
                            if (response.code() == 200) {
                                try {
                                    JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.body().toString());
                                    JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                                    JSONArray getAllProductList = getAllObject.getJSONArray("response");//get the array with the key "response"

                                    if (getAllProductList != null) {
                                        mProductsModel.clear();
                                        for (int i = 0; i < getAllProductList.length(); i++) {
                                            String id = getAllProductList.getJSONObject(i).get("id").toString();
                                            String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                            String title = getAllProductList.getJSONObject(i).get("title").toString();
                                            String price = getAllProductList.getJSONObject(i).get("price").toString();
                                            String imageUrl = getAllProductList.getJSONObject(i).get("image").toString();

                                            ProductsModel registerModel = new ProductsModel(id, sku, title, price, imageUrl);
                                            registerModel.setId(id);
                                            registerModel.setSku(sku);
                                            registerModel.setTitle(title);
                                            registerModel.setPrice(price);
                                            registerModel.setImage_url(imageUrl);
                                            mProductsModel.add(registerModel);
                                        }
                                    }

                                    layoutManager = new LinearLayoutManager(mContext);
                                    rc_product.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                                    rc_product.setHasFixedSize(true);
                                    rc_product.setAdapter(new ProductListAdapter(mContext, mProductsModel));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mAlert.onShowProgressDialog(OrderDetailsActivity.this, false);
                                BaseActivity.printLog("response-success : ", response.body().toString());
                            } else if (response.code() == 400) {
                                mAlert.onShowProgressDialog(OrderDetailsActivity.this, false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("response-failure", call.toString());
                    mAlert.onShowProgressDialog(OrderDetailsActivity.this, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_track_order)
    public void trackOrder() {
        startActivity(new Intent(this, OrderTrackingActivity.class));
    }

    public static class ProductModel {
        String medicine_name;
        String mrp_price;
        String item_contains;
        String medicine_price;

        public ProductModel(String medicine_name, String mrp_price, String item_contains, String medicine_price) {
            this.medicine_name = medicine_name;
            this.mrp_price = mrp_price;
            this.item_contains = item_contains;
            this.medicine_price = medicine_price;
        }

        public String getMedicine_name() {
            return medicine_name;
        }

        public void setMedicine_name(String medicine_name) {
            this.medicine_name = medicine_name;
        }

        public String getMrp_price() {
            return mrp_price;
        }

        public void setMrp_price(String mrp_price) {
            this.mrp_price = mrp_price;
        }

        public String getItem_contains() {
            return item_contains;
        }

        public void setItem_contains(String item_contains) {
            this.item_contains = item_contains;
        }

        public String getMedicine_price() {
            return medicine_price;
        }

        public void setMedicine_price(String medicine_price) {
            this.medicine_price = medicine_price;
        }
    }
}

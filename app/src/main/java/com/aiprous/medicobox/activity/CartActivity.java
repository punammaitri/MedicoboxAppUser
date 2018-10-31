package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
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
import com.aiprous.medicobox.adapter.CartAdapter;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETCARTITEMS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_cart)
    RecyclerView rc_cart;
    public static TextView tv_mrp_total;
    public static TextView tv_price_discount;
    public static TextView tv_to_be_paid;
    public static TextView tv_total_saving;
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;
    @BindView(R.id.tv_shipping_note)
    TextView tv_shipping_note;
    public static TextView tv_cart_empty;
    public static NestedScrollView nestedscroll_cart;
    ArrayList<CartModel.Items> cartModelArrayList = new ArrayList<>();
    private Context mcontext = this;
    CustomProgressDialog mAlert;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mAlert = CustomProgressDialog.getInstance();

        tv_mrp_total = (TextView) findViewById(R.id.tv_mrp_total);
        tv_price_discount = (TextView) findViewById(R.id.tv_price_discount);
        tv_to_be_paid = (TextView) findViewById(R.id.tv_to_be_paid);
        tv_total_saving = (TextView) findViewById(R.id.tv_total_saving);
        tv_cart_empty = (TextView) findViewById(R.id.tv_cart_empty);
        nestedscroll_cart = (NestedScrollView) findViewById(R.id.nestedscroll_cart);
        rlayout_cart = (RelativeLayout) findViewById(R.id.rlayout_cart);
        tv_cart_size = (TextView) findViewById(R.id.tv_cart_size);

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);


        tv_shipping_note.setText("Free shipping for orders above " + mcontext.getResources().getString(R.string.Rs) + "500");


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //get cart items through api
            getCartItems();
        }

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            tv_cart_empty.setVisibility(View.VISIBLE);
            nestedscroll_cart.setVisibility(View.GONE);
            rlayout_cart.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.btn_continue_cart)
    public void onClickContinue() {
        startActivity(new Intent(this, OrderSummaryActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }


    private void getCartItems() {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(GETCARTITEMS)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                            JSONArray getAllProductList = getAllObject.getJSONArray("items");//get the array with the key "response"

                            if (getAllProductList != null) {
                                cartModelArrayList.clear();
                                for (int i = 0; i < getAllProductList.length(); i++) {
                                    int id = Integer.parseInt(getAllProductList.getJSONObject(i).get("item_id").toString());
                                    String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                    int qty = Integer.parseInt(getAllProductList.getJSONObject(i).get("qty").toString());
                                    String name = getAllProductList.getJSONObject(i).get("name").toString();
                                    int price = Integer.parseInt(getAllProductList.getJSONObject(i).get("price").toString());
                                    String product_type = getAllProductList.getJSONObject(i).get("product_type").toString();
                                    String lquoteId = getAllProductList.getJSONObject(i).get("quote_id").toString();


                                    CartModel.Items listModel = new CartModel.Items(lquoteId, product_type, price, name, qty, sku, id);
                                    listModel.setQuote_id(lquoteId);
                                    listModel.setProduct_type(product_type);
                                    listModel.setPrice(price);
                                    listModel.setName(name);
                                    listModel.setQty(qty);
                                    listModel.setSku(sku);
                                    listModel.setItem_id(id);
                                    cartModelArrayList.add(listModel);
                                }
                            }
                            CustomProgressDialog.getInstance().dismissDialog();
                            layoutManager = new LinearLayoutManager(mcontext);
                            rc_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_cart.setHasFixedSize(true);
                            rc_cart.setAdapter(new CartAdapter(mcontext, cartModelArrayList));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }
}
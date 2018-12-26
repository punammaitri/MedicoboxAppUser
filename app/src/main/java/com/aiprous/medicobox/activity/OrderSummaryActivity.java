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
import com.aiprous.medicobox.adapter.CartAdapter;
import com.aiprous.medicobox.adapter.OrderSummaryAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.model.CartOrderSummaryModel;
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

public class OrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.rc_order_summary)
    RecyclerView rc_order_summary;
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
    @BindView(R.id.tv_free_shipping_note)
    TextView tv_free_shipping_note;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<CartOrderSummaryModel.Response> orderSummaryArrayList = new ArrayList<>();
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        //set Text
        tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs) + " 350.0");
        tv_price_discount.setText("-" + mContext.getResources().getString(R.string.Rs) + " 30.0");
        tv_to_be_paid.setText(mContext.getResources().getString(R.string.Rs) + " 350.0");
        tv_total_savings.setText(mContext.getResources().getString(R.string.Rs) + " 30.0");
        tv_free_shipping_note.setText("Free shipping for orders above " + mContext.getResources().getString(R.string.Rs) + "500");


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //get cart items through api
            getCartItems(MedicoboxApp.onGetAuthToken());
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

    @OnClick(R.id.btn_confirm_order)
    public void confirmOrder() {
        startActivity(new Intent(this, PaymentDetailsActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch()
    {
        startActivity(new Intent(this,SearchViewActivity.class));
    }

    private void getCartItems(final String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GETCARTITEMS)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                            JSONArray getAllProductList = getAllObject.getJSONArray("res");//get the array with the key "response"

                            if (getAllProductList != null) {
                                orderSummaryArrayList.clear();
                                for (int i = 0; i < getAllProductList.length(); i++) {
                                    int item_id = Integer.parseInt(getAllProductList.getJSONObject(i).get("item_id").toString());
                                    String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                    int qty = Integer.parseInt(getAllProductList.getJSONObject(i).get("qty").toString());
                                    String id = getAllProductList.getJSONObject(i).get("id").toString();
                                    String name = getAllProductList.getJSONObject(i).get("name").toString();
                                    String price = getAllProductList.getJSONObject(i).get("price").toString();
                                    String product_type = getAllProductList.getJSONObject(i).get("product_type").toString();
                                    String lquoteId = getAllProductList.getJSONObject(i).get("quote_id").toString();
                                    String sale_price = getAllProductList.getJSONObject(i).get("sale_price").toString();
                                    String short_description = getAllProductList.getJSONObject(i).get("short_description").toString();
                                    String image = getAllProductList.getJSONObject(i).get("image").toString();
                                    String prescription = getAllProductList.getJSONObject(i).get("prescription").toString();
                                    int discount = Integer.parseInt(getAllProductList.getJSONObject(i).get("discount").toString());
                                    int prescription_req = Integer.parseInt(getAllProductList.getJSONObject(i).get("prescription").toString());

                                    CartOrderSummaryModel.Response listModel = new CartOrderSummaryModel.Response(discount, prescription, image,
                                            short_description, sale_price, lquoteId, product_type, price, name, id, qty, sku, item_id, prescription_req);
                                    listModel.setItem_id(item_id);
                                    listModel.setSku(sku);
                                    listModel.setQty(qty);
                                    listModel.setId(id);
                                    listModel.setName(name);
                                    listModel.setPrice(price);
                                    listModel.setProduct_type(product_type);
                                    listModel.getQuote_id();
                                    listModel.setSale_price(sale_price);
                                    listModel.setShort_description(short_description);
                                    listModel.setImage(image);
                                    listModel.setPrescription(prescription);
                                    listModel.setDiscount(discount);
                                    listModel.setPrescription_req(prescription_req);
                                    orderSummaryArrayList.add(listModel);
                                }
                            }

                            //visible cart layout
                            if (!orderSummaryArrayList.isEmpty()) {
                                tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
                                rlayout_cart.setVisibility(View.VISIBLE);
                            }

                            //set Adapter
                            layoutManager = new LinearLayoutManager(mContext);
                            rc_order_summary.setLayoutManager(new LinearLayoutManager(OrderSummaryActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_order_summary.setHasFixedSize(true);
                            rc_order_summary.setAdapter(new OrderSummaryAdapter(OrderSummaryActivity.this, orderSummaryArrayList));

                            CustomProgressDialog.getInstance().dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(OrderSummaryActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }
}

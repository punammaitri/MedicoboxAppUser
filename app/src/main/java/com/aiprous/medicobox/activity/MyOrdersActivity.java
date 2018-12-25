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

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.MyOrdersAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.USERORDER;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.searchview_order_id)
    SearchView searchview_order_id;
    @BindView(R.id.rc_my_order_list)
    RecyclerView rc_my_order_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    ArrayList<MyOrdersModel> myOrdersArrayList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext = this;
    private MyOrdersAdapter mlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        searchview_order_id.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        myOrdersArrayList.add(new MyOrdersModel("MB1528394829281", "29/09/2018", "280.00", "0"));
        myOrdersArrayList.add(new MyOrdersModel("MB1628394829281", "29/09/2018", "280.00", "1"));
        myOrdersArrayList.add(new MyOrdersModel("MB1728394829281", "29/09/2018", "280.00", "2"));


        layoutManager = new LinearLayoutManager(mContext);
        rc_my_order_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_my_order_list.setHasFixedSize(true);
        mlistAdapter = new MyOrdersAdapter(mContext, myOrdersArrayList);
        rc_my_order_list.setAdapter(mlistAdapter);


        try {
            searchview_order_id.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (myOrdersArrayList != null && !myOrdersArrayList.isEmpty()) {
                        ArrayList<MyOrdersModel> filteredModelList = filter(myOrdersArrayList, newText);
                        mlistAdapter.setFilter(filteredModelList);
                    }

                    return true;
                }

                private ArrayList<MyOrdersModel> filter(ArrayList<MyOrdersModel> models, String query) {
                    query = query.toLowerCase();

                    final ArrayList<MyOrdersModel> filteredModelList = new ArrayList<>();

                    for (MyOrdersModel model : models) {
                        final String text = model.orderId.toLowerCase();
                        if (text.contains(query)) {
                            filteredModelList.add(model);
                        }
                    }
                    return filteredModelList;
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //show cart size
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        //call my order API
        if (!isNetworkAvailable(MyOrdersActivity.this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            getMyOrderAPI(MedicoboxApp.onGetAuthToken());
        }
    }

    private void getMyOrderAPI(String bearerToken) {
        AndroidNetworking.post(USERORDER)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                      /*  try {
                            JSONObject jsonObject = new JSONObject(response.getString("response"));
                            String getId = jsonObject.get("id").toString();
                            String getGroupId = jsonObject.get("group_id").toString();
                            String getEmail = jsonObject.get("email").toString();
                            String getFirstname = jsonObject.get("firstname").toString();
                            String getLastname = jsonObject.get("lastname").toString();
                            String getStoreId = jsonObject.get("store_id").toString();
                            String getWebsiteId = jsonObject.get("website_id").toString();
                            String getMobile = jsonObject.get("mobile").toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
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

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    public class MyOrdersModel {
        String orderId;
        String order_date;
        String order_price;
        String deliverystatus;

        public MyOrdersModel(String orderId, String order_date, String order_price, String deliverystatus) {
            this.orderId = orderId;
            this.order_date = order_date;
            this.order_price = order_price;
            this.deliverystatus = deliverystatus;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch()
    {
        startActivity(new Intent(this,SearchViewActivity.class));
    }
}

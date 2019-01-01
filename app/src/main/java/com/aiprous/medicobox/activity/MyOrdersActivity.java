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
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.MyOrdersAdapter;
import com.aiprous.medicobox.adapter.SearchViewAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.MyOrdersModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
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
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.searchview_order_id)
    SearchView searchview_order_id;
    @BindView(R.id.rc_my_order_list)
    RecyclerView rc_my_order_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext = this;
    private MyOrdersAdapter mlistAdapter;
    ArrayList<MyOrdersModel> mMyorder = new ArrayList<>();

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

        try {
            searchview_order_id.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        if (mMyorder != null && !mMyorder.isEmpty()) {
                            ArrayList<MyOrdersModel> filteredModelList = filter(mMyorder, newText);
                            if (!(filteredModelList.size() == 0)){
                                mlistAdapter.setFilter(filteredModelList);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                private ArrayList<MyOrdersModel> filter(ArrayList<MyOrdersModel> models, String query) {
                    query = query;

                    final ArrayList<MyOrdersModel> filteredModelList = new ArrayList<>();

                    for (MyOrdersModel model : models) {
                        final String text = model.getEntity_id();
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

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        //show cart size
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        //call my order API
        if (!isNetworkAvailable(MyOrdersActivity.this)) {
            CustomProgressDialog.getInstance().dismissDialog();
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

                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject responseArray = getAllResponse.get("response").getAsJsonObject();
                            String status = responseArray.get("status").getAsString();

                            if (status.equals("success")) {
                                JsonArray order_data = responseArray.get("order_data").getAsJsonArray();
                                mMyorder.clear();

                                for (int i = 0; i < order_data.size(); i++) {
                                    JsonObject jsonObjectForOrderData = order_data.get(i).getAsJsonObject();
                                    String entity_id = jsonObjectForOrderData.get("entity_id").getAsString();
                                    String user_status = jsonObjectForOrderData.get("status").getAsString();
                                    String created_at = jsonObjectForOrderData.get("created_at").getAsString();
                                    String grand_total = jsonObjectForOrderData.get("grand_total").getAsString();

                                    MyOrdersModel myOrdersModel = new MyOrdersModel(entity_id, user_status, grand_total, created_at);
                                    myOrdersModel.setEntity_id(entity_id);
                                    myOrdersModel.setStatus(user_status);
                                    myOrdersModel.setGrand_total(grand_total);
                                    myOrdersModel.setCreated_at(created_at);
                                    mMyorder.add(myOrdersModel);
                                }
                            }

                            layoutManager = new LinearLayoutManager(mContext);
                            rc_my_order_list.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_my_order_list.setHasFixedSize(true);
                            mlistAdapter = new MyOrdersAdapter(MyOrdersActivity.this, mMyorder);
                            rc_my_order_list.setAdapter(mlistAdapter);
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
}

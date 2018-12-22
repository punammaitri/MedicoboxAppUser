package com.aiprous.medicobox.activity;

import android.app.Dialog;
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
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETCARTID;
import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ListActivity extends AppCompatActivity implements ListAdapter.DismissAlertInterface {

    // @BindView(R.id.rc_medicine_list)
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    RecyclerView rc_medicine_list;
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;
    ArrayList<ListModel> mListModelArray = new ArrayList<ListModel>();
    @BindView(R.id.txtNoDataFound)
    TextView txtNoDataFound;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter mlistAdapter;
    CustomProgressDialog mAlert;
    private String mCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mAlert = CustomProgressDialog.getInstance();

        rlayout_cart = (RelativeLayout) findViewById(R.id.rlayout_cart);
        tv_cart_size = (TextView) findViewById(R.id.tv_cart_size);
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rc_medicine_list = (RecyclerView) findViewById(R.id.rc_medicine_list);


        try {
            searchview_medicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (mListModelArray != null && !mListModelArray.isEmpty()) {
                        ArrayList<ListModel> filteredModelList = filter(mListModelArray, newText);
                        mlistAdapter.setFilter(filteredModelList);
                    }

                    return true;
                }

                private ArrayList<ListModel> filter(ArrayList<ListModel> models, String query) {
                    query = query.toLowerCase();

                    final ArrayList<ListModel> filteredModelList = new ArrayList<>();

                    for (ListModel model : models) {
                        final String text = model.getTitle().toLowerCase();
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

        if (MedicoboxApp.onGetCartID().isEmpty()) {
            if (!isNetworkAvailable(this)) {
                CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
            } else {
                AttemptGetCartId();
            }
        }

        //set cart value
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
            rlayout_cart.setVisibility(View.VISIBLE);
        }

        attemptToCallGetAllProductAPI();
    }

    private void attemptToCallGetAllProductAPI() {
        try {
            if (getIntent().getStringExtra("subcategoryId") != null) {
                mCategoryId = getIntent().getStringExtra("subcategoryId");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                jsonObject.put("category_id", mCategoryId);

                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    getAllproducts(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void showCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }

    private void getAllproducts(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(GETPRODUCT)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        txtNoDataFound.setVisibility(View.GONE);

                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject jsonObject = getAllResponse.get("response").getAsJsonObject();
                            JsonArray jsonProductArray = jsonObject.getAsJsonArray("products");
                            if (jsonProductArray != null) {
                                mListModelArray.clear();
                                for (int i = 0; i < jsonProductArray.size(); i++) {
                                    String id = jsonProductArray.get(i).getAsJsonObject().get("id").getAsString();
                                    String sku = jsonProductArray.get(i).getAsJsonObject().get("sku").getAsString();
                                    String title = jsonProductArray.get(i).getAsJsonObject().get("title").getAsString();
                                    String price = jsonProductArray.get(i).getAsJsonObject().get("price").getAsString();
                                    String sale_price = jsonProductArray.get(i).getAsJsonObject().get("sale_price").getAsString();
                                    String discount = jsonProductArray.get(i).getAsJsonObject().get("discount").getAsString();
                                    String short_description = jsonProductArray.get(i).getAsJsonObject().get("short_description").getAsString();
                                    String prescription_required = jsonProductArray.get(i).getAsJsonObject().get("prescription_required").getAsString();
                                    String image = jsonProductArray.get(i).getAsJsonObject().get("image").getAsString();
                                    String wishlist = jsonProductArray.get(i).getAsJsonObject().get("wishlist").getAsString();

                                    ListModel listModel = new ListModel(id, sku, title, price, sale_price, discount, short_description, prescription_required, image, wishlist);
                                    listModel.setId(id);
                                    listModel.setSku(sku);
                                    listModel.setTitle(title);
                                    listModel.setPrice(price);
                                    listModel.setSale_price(sale_price);
                                    listModel.setDiscount(discount);
                                    listModel.setShort_description(short_description);
                                    listModel.setPrescription_required(prescription_required);
                                    listModel.setImage(image);
                                    listModel.setWishlist(wishlist);
                                    mListModelArray.add(listModel);
                                }
                            }

                            if (!mListModelArray.isEmpty()) {
                                layoutManager = new LinearLayoutManager(mContext);
                                rc_medicine_list.setLayoutManager(new LinearLayoutManager(ListActivity.this, LinearLayoutManager.VERTICAL, false));
                                rc_medicine_list.setHasFixedSize(true);
                                rc_medicine_list.setAdapter(new ListAdapter(ListActivity.this, mListModelArray));
                            }
                            CustomProgressDialog.getInstance().dismissDialog();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        CustomProgressDialog.getInstance().dismissDialog();
                        // handle error
                        txtNoDataFound.setVisibility(View.VISIBLE);
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    private void AttemptGetCartId() {
        AndroidNetworking.post(GETCARTID)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                        MedicoboxApp.onSaveCartId(response);
                        Log.e("Cart id", "Cart Id  : " + response.toString());
                        // mAlert.onShowProgressDialog(ListActivity.this, false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        // mAlert.onShowProgressDialog(ListActivity.this, false);
                        //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                        Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void DismissAlert(Dialog dialog) {
        dialog.dismiss();
        attemptToCallGetAllProductAPI();
    }
}
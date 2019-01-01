package com.aiprous.medicobox.instaorder;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.GetWishListModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ADD_TO_CART_WISHLIST;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_WISHLIST;
import static com.aiprous.medicobox.utils.APIConstant.SHARE_WISHLIST;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class InstaAddNewListActivity extends AppCompatActivity implements InstaAddNewListAdapter.InstaAddNewListInterface {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    public RecyclerView rc_medicine_list;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.txt_no_data_found)
    TextView txtNoDataFound;
    @BindView(R.id.txtInstaOrder)
    TextView txtInstaOrder;
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private Dialog dialog;
    private TextView txtSave, txtCancel;
    ArrayList<GetWishListModel> mGetWishListModels = new ArrayList<GetWishListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_add_new_list);
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
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CallGetAllWishListAPI();
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void ShowNewInstaListAlert(final InstaAddNewListActivity mActivityContext) {
        dialog = new Dialog(mActivityContext, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.alert_new_insta_list);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        txtSave = dialog.findViewById(R.id.txtSave);
        txtCancel = dialog.findViewById(R.id.txtCancel);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    private void CallGetAllWishListAPI() {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                Log.e("url", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(GET_ALL_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            if (response.contains("There is no wishlist")) {
                                CustomProgressDialog.getInstance().dismissDialog();
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                rc_medicine_list.setVisibility(View.GONE);
                                txtInstaOrder.setVisibility(View.GONE);
                            } else {
                                txtNoDataFound.setVisibility(View.GONE);
                                rc_medicine_list.setVisibility(View.VISIBLE);
                                txtInstaOrder.setVisibility(View.VISIBLE);
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JsonArray asJsonArray = getAllResponse.getAsJsonArray("response");

                                if (asJsonArray != null) {
                                    mGetWishListModels.clear();
                                    for (int i = 0; i < asJsonArray.size(); i++) {
                                        String wishlist_name_id = ((JsonObject) asJsonArray.get(i)).get("wishlist_name_id").getAsString();
                                        String wishlist_name = ((JsonObject) asJsonArray.get(i)).get("wishlist_name").getAsString();
                                        JsonArray items = ((JsonObject) asJsonArray.get(i)).get("items").getAsJsonArray();

                                        GetWishListModel getWishListModel = new GetWishListModel(wishlist_name_id, wishlist_name, items);
                                        getWishListModel.setWishlist_name_id(wishlist_name_id);
                                        getWishListModel.setWishlist_name(wishlist_name);
                                        getWishListModel.setItems(items);
                                        mGetWishListModels.add(getWishListModel);
                                    }
                                }

                                //set all wishlist adapter
                                layoutManager = new LinearLayoutManager(mContext);
                                rc_medicine_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                rc_medicine_list.setHasFixedSize(true);
                                rc_medicine_list.setAdapter(new InstaAddNewListAdapter(InstaAddNewListActivity.this, mGetWishListModels));
                                CustomProgressDialog.getInstance().dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
    }

    @Override
    public void Delete() {
        CallGetAllWishListAPI();
    }

    @Override
    public void shareWishList(String userId, String wishlist_name_id, String edt_emails, String edt_messages, final Dialog dialog) {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", userId);
                jsonObject.put("wishlistname_id", wishlist_name_id);
                jsonObject.put("emails", edt_emails);
                jsonObject.put("message", edt_messages);
                Log.e("url", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(SHARE_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            String responseStatus = getAllResponse.get("status").getAsString();
                            if (responseStatus.equals("success")) {
                                String responseMsg = getAllResponse.get("message").getAsString();
                                CustomProgressDialog.getInstance().dismissDialog();
                                Toast.makeText(mContext, "" + responseMsg, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                String responseMsg = getAllResponse.get("message").getAsString();
                                Toast.makeText(mContext, "" + responseMsg, Toast.LENGTH_SHORT).show();
                            }
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

    @Override
    public void addToCartWishList(String wishlist_name_id) {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("wishlist_name_id", Integer.parseInt(wishlist_name_id));
                Log.e("url", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(ADD_TO_CART_WISHLIST)
                    .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject jsonResponse = getAllResponse.get("response").getAsJsonObject();
                            String status = jsonResponse.get("status").getAsString();
                            if (status.equals("success")) {
                                CustomProgressDialog.getInstance().dismissDialog();
                                Toast.makeText(mContext, "Wishlist added to cart successfully", Toast.LENGTH_SHORT).show();
                            }
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

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}

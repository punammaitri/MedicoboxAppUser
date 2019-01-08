package com.aiprous.medicobox.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.model.FinalPaymentModel;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.NEW_ADD_TO_CART_ORDER_PLACE;
import static com.aiprous.medicobox.utils.APIConstant.ORDER_ASSIGN;
import static com.aiprous.medicobox.utils.APIConstant.SEND_SMS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class PaymentDetailsActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
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
    public Bitmap mBitmap;
    ArrayList<FinalPaymentModel> mFinalPaymentModels = new ArrayList<>();
    private Uri imageBinaryUri;
    public String imageConvertedString = "";
    private String mQuoteId;
    private String quote_id;

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

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        //Convert bitmap to string
        try {
            imageBinaryUri = Uri.parse(getIntent().getStringExtra("image"));
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageBinaryUri);
            mBitmap = BitmapFactory.decodeStream(inputStream);
            imageConvertedString = convertBitmapToString(mBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // for passing cart model
        if (getIntent().getStringExtra("items") != null) {
            address_id = getIntent().getStringExtra("address_id");
            quote_id = getIntent().getStringExtra("quote_id");

            String cartListAsString = getIntent().getStringExtra("items");
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartModel.Response>>() {
            }.getType();

            cartList = gson.fromJson(cartListAsString, type);

            //for loop add item in final payment model
            for (int j = 0; j < cartList.size(); j++) {
                String productId = cartList.get(j).getId();
                int qty = cartList.get(j).getQty();
                String price = cartList.get(j).getPrice();

                FinalPaymentModel finalPaymentModel = new FinalPaymentModel(productId, qty, price);
                finalPaymentModel.setProduct_id(productId);
                finalPaymentModel.setQty(qty);
                finalPaymentModel.setPrice(price);
                mFinalPaymentModels.add(finalPaymentModel);
            }

            //for converting arraylist to json array
            try {
                Gson gsons = new Gson();
                String listString = gsons.toJson(mFinalPaymentModels,
                        new TypeToken<ArrayList<FinalPaymentModel>>() {
                        }.getType());
                jsonArray = new JSONArray(listString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void CallOrderPlaceAPI() {

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        //jsonArray = new JSONArray(mStreetArray);

        /*try {
            jsonObject.put("address_id", address_id);
            jsonObject.put("items", jsonArray);
            jsonObject.put("shipping_method", "");
            jsonObject.put("payment_method", "cash");
            jsonObject.put("image", imageConvertedString);
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("quote_id", quote_id);
            jsonObject.put("address_id", address_id);
            jsonObject.put("image", imageConvertedString);
            jsonObject.put("payment_method", "checkmo");
            jsonObject.put("shipping_method", "wkvendordropship_wkvendordropship");
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //CallSendSmsApi
        AndroidNetworking.post(NEW_ADD_TO_CART_ORDER_PLACE)
                .addJSONObjectBody(jsonObject) // posting json
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
                                String orderId = responseArray.get("order_id").getAsString();
                                //send sms to user
                                CallOrderAssignApi(orderId,address_id);

                                /*startActivity(new Intent(PaymentDetailsActivity.this, OrderPlacedActivity.class));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);*/
                            } else {
                                String msg = responseArray.get("msg").getAsString();
                                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PaymentDetailsActivity.this, "Something happen from server side", Toast.LENGTH_SHORT).show();
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
                if (!isNetworkAvailable(this)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    //Call get cart items API
                    CallOrderPlaceAPI();
                }
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

    public String convertBitmapToString(Bitmap bitmap) {
        Bitmap immagex = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

  /*  private void CallOrderPlaceAPI() {

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        //jsonArray = new JSONArray(mStreetArray);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address_id", address_id);
            jsonObject.put("quote_id", MedicoboxApp.onGetCartID());
            jsonObject.put("shipping_method", "");
            jsonObject.put("payment_method", "cash");
            jsonObject.put("image", imageConvertedString);
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(NEW_ADD_TO_CART_ORDER_PLACE)
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

                            if (status.equals("success")) {
                                String orderId = responseArray.get("order_id").getAsString();
                                //send sms to user
                                CallSendSmsApi(orderId);
                            } else {
                                String msg = responseArray.get("msg").getAsString();
                                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            }

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
    }*/


    private void CallOrderAssignApi(final String orderId, String address_id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", orderId);
            jsonObject.put("address_id", address_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(ORDER_ASSIGN )
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject responseObject = getAllResponse.get("response").getAsJsonObject();
                            String status = responseObject.get("status").getAsString();

                            if (status.equals("success")) {
                                CallSendSmsApi(orderId);
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
                        //Toast.makeText(MyOrdersActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void CallSendSmsApi(final String orderId) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile_number", "91" + MedicoboxApp.onGetMobileNo());
            jsonObject.put("message", "Your order placed successfully" + " Your order number is " + orderId);
            Log.e("url", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(SEND_SMS)
                .addJSONObjectBody(jsonObject) // posting json
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
                                Toast.makeText(mContext, "Your order placed successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PaymentDetailsActivity.this, OrderPlacedActivity.class)
                                        .putExtra("order_id", "" + orderId));
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);

                                CustomProgressDialog.getInstance().dismissDialog();
                            } else {
                                String msg = responseArray.get("msg").getAsString();
                                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            }

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
}
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.SINGLE_ORDER;
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
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtOrderId)
    TextView txtOrderId;
    @BindView(R.id.txtorderDate)
    TextView txtorderDate;

    @BindView(R.id.txtShippingAmount)
    TextView txtShippingAmount;
    @BindView(R.id.txtShippingAddressUsername)
    TextView txtShippingAddressUsername;
    @BindView(R.id.txtShippingAddress)
    TextView txtShippingAddress;
    @BindView(R.id.txtBillingAddressUsername)
    TextView txtBillingAddressUsername;
    @BindView(R.id.txtBillingAddress)
    TextView txtBillingAddress;

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    CustomProgressDialog mAlert;
    ArrayList<ProductsModel> mProductsModel = new ArrayList<ProductsModel>();
    ArrayList<ProductModel> mproductArrayList = new ArrayList<ProductModel>();
    private static DecimalFormat df2 = new DecimalFormat(".##");
    private String order_id;

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

        tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs) + "350.0");
        tv_total_saved.setText(mContext.getResources().getString(R.string.Rs) + "30.0");
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

        if (getIntent().getStringExtra("order_id") != null) {
            order_id = getIntent().getStringExtra("order_id");
            CallOrderDetailsAPI(order_id);
        }

    }

    private void CallOrderDetailsAPI(String order_id) {
        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //Add Json Object
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("order_id", order_id);
                getSingleOrderAPI(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }


    private void getSingleOrderAPI(JSONObject jsonObject) {
        AndroidNetworking.post(SINGLE_ORDER)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            String success = response.getString("status");

                            if (success.equals("success")) {
                                JsonObject getJsonObject1 = (JsonObject) new JsonParser().parse(response.getString("order_data"));
                                String entity_id = getJsonObject1.get("entity_id").getAsString();
                                String status = getJsonObject1.get("status").getAsString();
                                String increment_id = getJsonObject1.get("increment_id").getAsString();
                                String grand_total = getJsonObject1.get("grand_total").getAsString();
                                String total_due = getJsonObject1.get("total_due").getAsString();
                                JsonObject shipping_address_object = getJsonObject1.get("shipping_address").getAsJsonObject();
                                JsonObject billing_address_object = getJsonObject1.get("billing_address").getAsJsonObject();

                                //for shipping address
                                if (shipping_address_object != null) {
                                    String firstname = shipping_address_object.get("firstname").getAsString();
                                    String lastname = shipping_address_object.get("lastname").getAsString();
                                    String street = shipping_address_object.get("street").getAsString();
                                    String city = shipping_address_object.get("city").getAsString();
                                    String telephone = shipping_address_object.get("telephone").getAsString();
                                    String country_id = shipping_address_object.get("country_id").getAsString();
                                    String postcode = shipping_address_object.get("postcode").getAsString();

                                    txtShippingAddressUsername.setText(firstname + " " + lastname);
                                    String fullShippingAddress = street + "," + city + "," + country_id + "\n" + postcode;
                                    txtShippingAddress.setText(fullShippingAddress);
                                }

                                //for billing address
                                if (billing_address_object != null) {
                                    String firstname = billing_address_object.get("firstname").getAsString();
                                    String lastname = billing_address_object.get("lastname").getAsString();
                                    String street = billing_address_object.get("street").getAsString();
                                    String city = billing_address_object.get("city").getAsString();
                                    String telephone = billing_address_object.get("telephone").getAsString();
                                    String country_id = billing_address_object.get("country_id").getAsString();
                                    String postcode = billing_address_object.get("postcode").getAsString();

                                    txtBillingAddressUsername.setText(firstname + " " + lastname);
                                    String fullBillingAddress = street + "," + city + "," + country_id + "\n" + postcode;
                                    txtBillingAddress.setText(fullBillingAddress);
                                }

                                //for showing date
                                String created_at = getJsonObject1.get("created_at").getAsString();
                                StringTokenizer splitDate = new StringTokenizer(created_at, " ");
                                String date = splitDate.nextToken();
                                String time = splitDate.nextToken();
                                txtorderDate.setText("Order Date:" + date);

                                Double price_discount = getJsonObject1.get("discount_amount").getAsDouble();
                                Double shipping_amount = getJsonObject1.get("shipping_amount").getAsDouble();
                                tv_price_discount.setText("-" + mContext.getResources().getString(R.string.Rs) + price_discount);
                                txtShippingAmount.setText(mContext.getResources().getString(R.string.Rs) + shipping_amount);
                                Double amount_paid = null;

                                try {
                                    //for amount paid
                                    Double grand_total_int = getJsonObject1.get("grand_total").getAsDouble();
                                    Double total_due_int = getJsonObject1.get("total_due").getAsDouble();
                                    amount_paid = grand_total_int - total_due_int;
                                    tv_amount_paid.setText(mContext.getResources().getString(R.string.Rs) + amount_paid);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //remove digit after dot
                                double input = Double.parseDouble(grand_total);
                                // for set value
                                tv_total_product_price.setText(mContext.getResources().getString(R.string.Rs) + df2.format(input));
                                txtOrderId.setText("Order ID: " + increment_id);
                                txtStatus.setText(status);

                                JsonArray itemArrayJson = getJsonObject1.get("items").getAsJsonArray();
                                if (itemArrayJson != null) {
                                    mProductsModel.clear();
                                    for (int i = 0; i < itemArrayJson.size(); i++) {

                                        JsonObject jsonObjectForItems = itemArrayJson.get(i).getAsJsonObject();
                                        String item_id = jsonObjectForItems.get("item_id").getAsString();
                                        String order_id = jsonObjectForItems.get("order_id").getAsString();
                                        String sku = jsonObjectForItems.get("sku").getAsString();
                                        String name = jsonObjectForItems.get("name").getAsString();
                                        String price = jsonObjectForItems.get("price").getAsString();
                                        String mrp_total = jsonObjectForItems.get("base_price").getAsString();
                                        String image = jsonObjectForItems.get("image").getAsString();

                                        ProductsModel productsModel = new ProductsModel(item_id, order_id, sku, name, price, image, mrp_total);
                                        productsModel.setItem_id(item_id);
                                        productsModel.setOrder_id(order_id);
                                        productsModel.setSku(sku);
                                        productsModel.setName(name);
                                        productsModel.setPrice(price);
                                        productsModel.setImage(image);
                                        productsModel.setMrp_total(mrp_total);
                                        mProductsModel.add(productsModel);
                                    }
                                }
                            }

                            layoutManager = new LinearLayoutManager(mContext);
                            rc_product.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_product.setHasFixedSize(true);
                            rc_product.setAdapter(new ProductListAdapter(mContext, mProductsModel));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomProgressDialog.getInstance().dismissDialog();
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

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    @OnClick(R.id.btn_track_order)
    public void trackOrder() {
        startActivity(new Intent(this, OrderTrackingActivity.class)
        .putExtra("order_id",""+order_id));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }
}

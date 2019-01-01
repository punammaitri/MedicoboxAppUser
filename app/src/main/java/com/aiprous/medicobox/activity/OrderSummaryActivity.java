package com.aiprous.medicobox.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.OrderSummaryAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.model.CartOrderSummaryModel;
import com.aiprous.medicobox.prescription.PrescriptionEditAddressActivity;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETCARTITEMS;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_ADDRESS;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class OrderSummaryActivity extends AppCompatActivity {

    @BindView(R.id.rc_order_summary)
    RecyclerView rc_order_summary;
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
    @BindView(R.id.tv_free_shipping_note)
    TextView tv_free_shipping_note;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.imgPrescription)
    ImageView imgPrescription;
    @BindView(R.id.txtOrderSummaryBillingAdd)
    TextView txtOrderSummaryBillingAdd;
    @BindView(R.id.txtOrderSummaryBillingUsername)
    TextView txtOrderSummaryBillingUsername;
    @BindView(R.id.txtOrderSummaryBillingAddress)
    TextView txtOrderSummaryBillingAddress;
    @BindView(R.id.txtOrderSummaryBillingTelephone)
    TextView txtOrderSummaryBillingTelephone;
    @BindView(R.id.cardBilling)
    CardView cardBilling;
    @BindView(R.id.txtOrderSummaryShippingAdd)
    TextView txtOrderSummaryShippingAdd;
    @BindView(R.id.txtOrderSummaryShippingUsername)
    TextView txtOrderSummaryShippingUsername;
    @BindView(R.id.txtOrderSummaryShippingAddress)
    TextView txtOrderSummaryShippingAddress;
    @BindView(R.id.txtOrderSummaryShippingTelephone)
    TextView txtOrderSummaryShippingTelephone;
    @BindView(R.id.cardShipping)
    CardView cardShipping;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<CartOrderSummaryModel.Response> orderSummaryArrayList = new ArrayList<>();
    private Context mContext = this;
    private Bitmap upload_presc_url;
    Bitmap bmp = null;
    private String billing_id, billing_firstname, billing_lastname, billing_city, billing_country_id, billing_region_id;
    private String billing_postcode, billing_flat, billing_telephone, billing_street, billing_landmark;
    private String shipping_id;
    private String shipping_lastname;
    private String shipping_firstname;
    private String shipping_postcode;
    private String shipping_region_id;
    private String shipping_country_id;
    private String shipping_city;
    private String shipping_telephone;
    private String shipping_flat;
    private String shipping_street;
    private String shipping_landmark;
    ArrayList<CartModel.Response> cartList = new ArrayList<>();

    private String imageBinaryString;
    public Bitmap mBitmap;
    public String imageConvertedString = "";
    private Uri imageBinaryUri ;

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

        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        // for passing cart model
        if (getIntent().getStringExtra("cart_model") != null) {
            //load cart model
            String cartListAsString = getIntent().getStringExtra("cart_model");
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartModel.Response>>() {
            }.getType();
            cartList = gson.fromJson(cartListAsString, type);
            for (CartModel.Response cars : cartList) {
                Log.e("Cart Data", cars.getId());
            }
        }

        //load image in bitmap
        if (getIntent().getStringExtra("imageBinaryString") != null) {
            try {
                imageBinaryUri = Uri.parse(getIntent().getStringExtra("imageBinaryString"));
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(imageBinaryUri);
                mBitmap = BitmapFactory.decodeStream(inputStream);
                imgPrescription.setImageBitmap(mBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            imgPrescription.setVisibility(View.GONE);
        }




        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().dismissDialog();
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            //get cart items through api
            getCartItems(MedicoboxApp.onGetAuthToken());
        }
        CallAddressAPI();
    }

    private void CallAddressAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", MedicoboxApp.onGetId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable(this)) {
            CustomProgressDialog.getInstance().dismissDialog();
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            callGetAddress(jsonObject);
        }
    }

    private void callGetAddress(JSONObject jsonObject) {
        AndroidNetworking.post(GET_ALL_ADDRESS)
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
                                JsonArray jsonAddressArray = responseArray.get("address").getAsJsonArray();
                                if (!(jsonAddressArray.size() == 0)) {
                                    for (int i = 0; i < jsonAddressArray.size(); i++) {
                                        JsonObject asJsonObject = jsonAddressArray.get(i).getAsJsonObject();
                                        if (asJsonObject.get("default_billing").getAsString().equalsIgnoreCase("true")) {
                                            cardBilling.setVisibility(View.VISIBLE);
                                            billing_id = asJsonObject.get("id").getAsString();
                                            billing_firstname = asJsonObject.get("firstname").getAsString();
                                            billing_lastname = asJsonObject.get("lastname").getAsString();
                                            billing_city = asJsonObject.get("city").getAsString();
                                            billing_country_id = asJsonObject.get("country_id").getAsString();
                                            billing_region_id = asJsonObject.get("region_id").getAsString();
                                            billing_postcode = asJsonObject.get("postcode").getAsString();
                                            billing_telephone = asJsonObject.get("telephone").getAsString();

                                            JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                            JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                            billing_flat = streetInnerArray.get(0).getAsString();
                                            billing_street = streetInnerArray.get(1).getAsString();
                                            billing_landmark = streetInnerArray.get(2).getAsString();

                                            String fullBillingAddress = billing_flat + "," + billing_street + "," + billing_landmark + "," + "\n" +
                                                    billing_city + "," + billing_country_id + "," + billing_postcode;
                                            txtOrderSummaryBillingAddress.setText(fullBillingAddress);
                                            txtOrderSummaryBillingUsername.setText(billing_firstname + " " + billing_lastname);
                                            txtOrderSummaryBillingTelephone.setText(billing_telephone);
                                            txtOrderSummaryBillingAdd.setText("EDIT");
                                        } else {
                                            cardShipping.setVisibility(View.VISIBLE);
                                            shipping_id = asJsonObject.get("id").getAsString();
                                            shipping_firstname = asJsonObject.get("firstname").getAsString();
                                            shipping_lastname = asJsonObject.get("lastname").getAsString();
                                            shipping_city = asJsonObject.get("city").getAsString();
                                            shipping_country_id = asJsonObject.get("country_id").getAsString();
                                            shipping_region_id = asJsonObject.get("region_id").getAsString();
                                            shipping_postcode = asJsonObject.get("postcode").getAsString();
                                            shipping_telephone = asJsonObject.get("telephone").getAsString();

                                            JsonArray streetArray = asJsonObject.get("street").getAsJsonArray();
                                            JsonArray streetInnerArray = streetArray.getAsJsonArray();
                                            shipping_flat = streetInnerArray.get(0).getAsString();
                                            shipping_street = streetInnerArray.get(1).getAsString();
                                            shipping_landmark = streetInnerArray.get(2).getAsString();

                                            String fullShippingAddress = shipping_flat + "," + shipping_street + "," + shipping_landmark + "," + "\n" +
                                                    shipping_city + "," + shipping_country_id + "," + shipping_postcode;
                                            txtOrderSummaryShippingAddress.setText(fullShippingAddress);
                                            txtOrderSummaryShippingUsername.setText(shipping_firstname + " " + shipping_lastname);
                                            txtOrderSummaryShippingTelephone.setText(shipping_telephone);
                                            txtOrderSummaryShippingAdd.setText("EDIT");
                                        }
                                    }
                                }
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

    @OnClick({R.id.txtOrderSummaryBillingAdd, R.id.txtOrderSummaryShippingAdd, R.id.rlayout_back_button,
            R.id.btn_confirm_order, R.id.searchview_medicine, R.id.rlayout_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtOrderSummaryBillingAdd:
                if (txtOrderSummaryBillingAdd.getText().equals("ADD")) {
                    startActivity(new Intent(OrderSummaryActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("billingFlag", "true")
                            .putExtra("order_summary", "true"));
                } else {
                    startActivity(new Intent(OrderSummaryActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("billingFlag", "true")
                            .putExtra("id", billing_id)
                            .putExtra("firstname", billing_firstname)
                            .putExtra("lastname", billing_lastname)
                            .putExtra("city", billing_city)
                            .putExtra("country_id", billing_country_id)
                            .putExtra("region_id", billing_region_id)
                            .putExtra("postcode", billing_postcode)
                            .putExtra("telephone", billing_telephone)
                            .putExtra("flat", billing_flat)
                            .putExtra("street", billing_street)
                            .putExtra("landmark", billing_landmark)
                            .putExtra("order_summary", "true"));
                }
                break;
            case R.id.txtOrderSummaryShippingAdd:
                if (txtOrderSummaryShippingAdd.getText().equals("ADD")) {
                    startActivity(new Intent(OrderSummaryActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "true")
                            .putExtra("order_summary", "true"));
                } else {
                    startActivity(new Intent(OrderSummaryActivity.this, PrescriptionEditAddressActivity.class)
                            .putExtra("shippingFlag", "true")
                            .putExtra("id", shipping_id)
                            .putExtra("firstname", shipping_firstname)
                            .putExtra("lastname", shipping_lastname)
                            .putExtra("city", shipping_city)
                            .putExtra("country_id", shipping_country_id)
                            .putExtra("region_id", shipping_region_id)
                            .putExtra("postcode", shipping_postcode)
                            .putExtra("telephone", shipping_telephone)
                            .putExtra("flat", shipping_flat)
                            .putExtra("street", shipping_street)
                            .putExtra("landmark", shipping_landmark)
                            .putExtra("order_summary", "true"));
                }
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
            case R.id.btn_confirm_order:
                Gson gson = new Gson();
                String cartModel = gson.toJson(cartList);
                startActivity(new Intent(this, PaymentDetailsActivity.class)
                        .putExtra("address_id", "" + billing_id)
                        .putExtra("items", "" + cartModel)
                        .putExtra("image", "" + imageBinaryUri));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.searchview_medicine:
                startActivity(new Intent(this, SearchViewActivity.class));
                break;
            case R.id.rlayout_cart:
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }
}
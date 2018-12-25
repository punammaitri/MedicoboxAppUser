package com.aiprous.medicobox.prescription;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.adapter.SearchProductViewAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.model.SearchModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;
import static com.aiprous.medicobox.utils.APIConstant.SEARCHPRODUCT;
import static com.aiprous.medicobox.utils.APIConstant.UPLOADED_PRESCRIPTION_ADD_CART;
import static com.aiprous.medicobox.utils.APIConstant.UPLOADED_PRESCRIPTION_DELETE_CART;


public class PrescriptionUploadOptionActivity extends AppCompatActivity implements SearchProductViewAdapter.DeleteCartItemInterface {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.edt_duration_of_dose)
    EditText edt_duration_of_dose;
    @BindView(R.id.txtCartItems)
    TextView txtCartItems;
    @BindView(R.id.txt_duration_example)
    TextView txtDurationExample;
    @BindView(R.id.linear_order_everything)
    LinearLayout linearOrderEverything;
    @BindView(R.id.edt_specify_medicine)
    AutoCompleteTextView edt_specify_medicine;
    @BindView(R.id.linear_specify_medicine)
    LinearLayout linearSpecifyMedicine;
    @BindView(R.id.rb_order_everything)
    RadioButton mRadioButtonOrderEverything;
    @BindView(R.id.rb_specify_medicine)
    RadioButton mRadioButtonSpecifyMedicine;
    @BindView(R.id.rb_call_me)
    RadioButton mRadioButtonCallMe;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    @BindView(R.id.img_attach_arrow)
    ImageView img_attach_arrow;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.recyclerspecifyMedicine)
    RecyclerView recyclerspecifyMedicine;
    @BindView(R.id.linSearchProduct)
    LinearLayout linSearchProduct;
    RecyclerView rc_medicine_list;
    ArrayList<PrescriptionUploadOptionActivity.ListModel> mlistModelsArray = new ArrayList<>();
    ArrayList<GetImageUrlModel> mGetImageUrlModel = new ArrayList<>();
    ArrayList<GetImageUrlModel> mUpdateGetImageUrlModel = new ArrayList<>();
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchModel> searchModelsArrayList = new ArrayList<>();
    ArrayList<SearchModel> searchModelFinalArrayList = new ArrayList<>();
    private SearchProductViewAdapter mSearchViewAdapter;
    private String getPatientname;
    private String getAdditionalComment;
    private String mFlag = "";
    private String getDose = "";
    private String getDoseparameter;
    ArrayAdapter<SearchModel> adapter;
    String mProductIdForDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_order_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        img_attach_arrow.setImageResource(R.drawable.arrow_yellow);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rc_medicine_list = findViewById(R.id.rc_medicine_list);

        //add static data into List array list
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionUploadOptionAdapter(mContext, mlistModelsArray));


        edt_specify_medicine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {

                String getSearchText = String.valueOf(newText);

                for (int i = 0; i < searchModelsArrayList.size(); i++) {
                    if (getSearchText.equals(searchModelsArrayList.get(i).getTitle())) {

                        String id = searchModelsArrayList.get(i).getId();
                        String sku = searchModelsArrayList.get(i).getSku();
                        String title = searchModelsArrayList.get(i).getTitle();
                        String price = searchModelsArrayList.get(i).getPrice();
                        String discount = searchModelsArrayList.get(i).getDiscount();
                        String short_description = searchModelsArrayList.get(i).getShort_description();
                        String image = searchModelsArrayList.get(i).getImage();

                        SearchModel searchModel = new SearchModel(id, sku, title, price, discount, short_description, image);
                        searchModel.setId(id);
                        searchModel.setSku(sku);
                        searchModel.setTitle(title);
                        searchModel.setPrice(price);
                        searchModel.setDiscount(discount);
                        searchModel.setShort_description(short_description);
                        searchModel.setImage(image);
                        searchModelFinalArrayList.add(searchModel);
                        edt_specify_medicine.setText("");
                        //call upload prescription add to cart API

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("product_id", id);
                            jsonObject.put("qty", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CallUploadPrescriptionAddTocart(jsonObject);
                        break;
                    }
                }

                linSearchProduct.setVisibility(View.VISIBLE);
                txtCartItems.setVisibility(View.VISIBLE);
                //set adapter
                layoutManager = new LinearLayoutManager(mContext);
                recyclerspecifyMedicine.setLayoutManager(new LinearLayoutManager(PrescriptionUploadOptionActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerspecifyMedicine.setHasFixedSize(true);
                mSearchViewAdapter = new SearchProductViewAdapter(PrescriptionUploadOptionActivity.this, searchModelFinalArrayList);
                recyclerspecifyMedicine.setAdapter(mSearchViewAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if (getIntent().getStringExtra("getPatientName") != null) {
            getPatientname = getIntent().getStringExtra("getPatientName");
            getAdditionalComment = getIntent().getStringExtra("getAdditionalComment");
        }
        CallSearchAPI();
    }

    private void CallSearchAPI() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "");
            searchAllProduct(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick({R.id.rb_order_everything, R.id.rb_specify_medicine, R.id.rb_call_me, R.id.rlayout_back_button, R.id.btnContinue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_order_everything:
                linearOrderEverything.setVisibility(View.VISIBLE);
                rc_medicine_list.setVisibility(View.GONE);
                linearSpecifyMedicine.setVisibility(View.GONE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow);

                linSearchProduct.setVisibility(View.GONE);
                txtCartItems.setVisibility(View.GONE);

                mFlag = "order_everything";
                break;
            case R.id.rb_specify_medicine:
                linearSpecifyMedicine.setVisibility(View.VISIBLE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.GONE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow);

                mFlag = "specify_medicine";
                break;
            case R.id.rb_call_me:
                linearSpecifyMedicine.setVisibility(View.GONE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.VISIBLE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow_up);

                linSearchProduct.setVisibility(View.GONE);
                txtCartItems.setVisibility(View.GONE);

                mFlag = "call_me";
                break;
            case R.id.btnContinue:

                if (mFlag.equals("call_me")) {
                    CallNextActivity(mFlag);
                } else if (mFlag.equals("order_everything")) {
                    if (edt_duration_of_dose.getText().length() == 0) {
                        edt_duration_of_dose.setError("Please enter amount");
                    } else {
                        getDose = edt_duration_of_dose.getText().toString();
                        CallNextActivity(mFlag);
                    }
                } else if (mFlag.equals("specify_medicine")) {
                    CallNextActivity(mFlag);
                }
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
        }
    }

    private void CallNextActivity(String mFlag) {

        GetImageUrlModel getImageUrlModel = new GetImageUrlModel(getPatientname, getAdditionalComment, mFlag, getDose);
        getImageUrlModel.setPatientName(getPatientname);
        getImageUrlModel.setAdditionalComment(getAdditionalComment);
        getImageUrlModel.setUploadPrescriptionFlag(mFlag);
        getImageUrlModel.setGetDoseParam(getDose);
        mUpdateGetImageUrlModel.add(getImageUrlModel);

        Gson gson = new Gson();
        String newUpdatedmodel = gson.toJson(mUpdateGetImageUrlModel);
        startActivity(new Intent(mContext, PrescriptionChooseDeliveryAddressActivity.class)
                .putExtra("PrescriptionImageModel", newUpdatedmodel)
                .putExtra("choose_delivery_address", "true")
                .putExtra("getPatientName", getPatientname)
                .putExtra("getAdditionalComment", getAdditionalComment));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void DeleteCartItem(String id) {

        mProductIdForDelete = id;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "delete");
            jsonObject.put("product_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CallUploadPrescriptionDeleteItem(jsonObject);
    }

    public class ListModel {
        int image;
        String medicineName;
        String value;
        String mrp;
        String discount;
        String price;

        public ListModel(int image, String medicineName, String value, String mrp, String discount, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.value = value;
            this.mrp = mrp;
            this.discount = discount;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    private void searchAllProduct(final JSONObject productname) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(SEARCHPRODUCT)
                .addJSONObjectBody(productname)
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

                            for (int i = 0; i < getAllProductList.length(); i++) {
                                String id = getAllProductList.getJSONObject(i).get("id").toString();
                                String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                String title = getAllProductList.getJSONObject(i).get("title").toString();
                                String price = getAllProductList.getJSONObject(i).get("price").toString();
                                String discount = getAllProductList.getJSONObject(i).get("discount").toString();
                                String short_description = getAllProductList.getJSONObject(i).get("short_description").toString();
                                String image = getAllProductList.getJSONObject(i).get("image").toString();

                                SearchModel searchModel = new SearchModel(id, sku, title, price, discount, short_description, image);
                                searchModel.setId(id);
                                searchModel.setSku(sku);
                                searchModel.setTitle(title);
                                searchModel.setPrice(price);
                                searchModel.setDiscount(discount);
                                searchModel.setShort_description(short_description);
                                searchModel.setImage(image);
                                searchModelsArrayList.add(searchModel);
                            }

                            //for hiding the view
                            if (searchModelsArrayList.size() == 0) {
                                linSearchProduct.setVisibility(View.GONE);
                                txtCartItems.setVisibility(View.GONE);
                            }

                            //set sarch adapter
                            adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, searchModelsArrayList);
                            edt_specify_medicine.setThreshold(1);
                            edt_specify_medicine.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(PrescriptionUploadOptionActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    private void CallUploadPrescriptionAddTocart(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(UPLOADED_PRESCRIPTION_ADD_CART)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            String status = getAllResponse.get("status").getAsString();
                            if (status.equals("success")) {
                                Toast.makeText(mContext, "Item added to cart successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "failure", Toast.LENGTH_SHORT).show();
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
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }


    private void CallUploadPrescriptionDeleteItem(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(UPLOADED_PRESCRIPTION_DELETE_CART)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            String status = getAllResponse.get("status").getAsString();
                            if (status.equals("success")) {
                                for (int i = 0; i < searchModelFinalArrayList.size(); i++) {
                                    if (mProductIdForDelete.equals(searchModelFinalArrayList.get(i).getId())) {
                                        searchModelFinalArrayList.remove(i);
                                        mSearchViewAdapter = new SearchProductViewAdapter(PrescriptionUploadOptionActivity.this, searchModelFinalArrayList);
                                        recyclerspecifyMedicine.setAdapter(mSearchViewAdapter);
                                        break;
                                    }
                                }

                                Toast.makeText(mContext, "Item Deleted from cart successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "failure", Toast.LENGTH_SHORT).show();
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
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

}
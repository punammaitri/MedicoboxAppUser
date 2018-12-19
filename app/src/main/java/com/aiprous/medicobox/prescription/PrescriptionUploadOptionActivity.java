package com.aiprous.medicobox.prescription;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.activity.SearchViewActivity;
import com.aiprous.medicobox.adapter.SearchProductViewAdapter;
import com.aiprous.medicobox.adapter.SearchViewAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.SearchModel;
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

import static com.aiprous.medicobox.utils.APIConstant.SEARCHPRODUCT;


public class PrescriptionUploadOptionActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.txt_duration_of_dose)
    TextView txtDurationOfDose;
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

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchModel> searchModelsArrayList = new ArrayList<>();
    private SearchProductViewAdapter mSearchViewAdapter;

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

                String ff = String.valueOf(newText);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", ff);
                    searchAllProduct(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (searchModelsArrayList != null && !searchModelsArrayList.isEmpty()) {
                    ArrayList<SearchModel> filteredModelList = filter(searchModelsArrayList, ff);
                    mSearchViewAdapter.setFilter(filteredModelList);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ArrayList<SearchModel> filter(ArrayList<SearchModel> models, String query) {
        query = query.toLowerCase();

        final ArrayList<SearchModel> filteredModelList = new ArrayList<>();

        for (SearchModel model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
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

                break;
            case R.id.rb_specify_medicine:
                linearSpecifyMedicine.setVisibility(View.VISIBLE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.GONE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow);

                break;
            case R.id.rb_call_me:
                linearSpecifyMedicine.setVisibility(View.GONE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.VISIBLE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow_up);

                linSearchProduct.setVisibility(View.GONE);
                txtCartItems.setVisibility(View.GONE);
                break;
            case R.id.btnContinue:
                startActivity(new Intent(this, PrescriptionChooseDeliveryAddressActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
        }
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
                        // Toast.makeText(mcontext, response.toString(), Toast.LENGTH_SHORT).show();
                        searchModelsArrayList.clear();
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

                            linSearchProduct.setVisibility(View.VISIBLE);
                            txtCartItems.setVisibility(View.VISIBLE);

                            //set adapter
                            layoutManager = new LinearLayoutManager(mContext);
                            recyclerspecifyMedicine.setLayoutManager(new LinearLayoutManager(PrescriptionUploadOptionActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerspecifyMedicine.setHasFixedSize(true);
                            mSearchViewAdapter = new SearchProductViewAdapter(mContext, searchModelsArrayList);
                            recyclerspecifyMedicine.setAdapter(mSearchViewAdapter);
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
}
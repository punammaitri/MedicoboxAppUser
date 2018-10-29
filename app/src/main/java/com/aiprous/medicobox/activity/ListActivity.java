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
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.adapter.ProductListAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.model.ProductsModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
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
import static com.aiprous.medicobox.utils.APIConstant.GETCARTID;
import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;

import static com.aiprous.medicobox.utils.APIConstant.LOGIN;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ListActivity extends AppCompatActivity {

    // @BindView(R.id.rc_medicine_list)
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    RecyclerView rc_medicine_list;
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;
    ArrayList<ListModel> mListModelArray = new ArrayList<ListModel>();
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter mlistAdapter;
    CustomProgressDialog mAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mAlert = CustomProgressDialog.getInstance();

        rlayout_cart=(RelativeLayout)findViewById(R.id.rlayout_cart);
        tv_cart_size=(TextView)findViewById(R.id.tv_cart_size);
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rc_medicine_list = (RecyclerView) findViewById(R.id.rc_medicine_list);

        //add static data into array list
       /* mlistModelsArray.add(new ListModel(R.drawable.bottle, "ABC", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "xyz", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "pqr", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "aaa", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "ccc", "Bottle of 60 tablet", "150", "30%", "135"));
*/

        try{
            searchview_medicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if(mListModelArray!=null&&!mListModelArray.isEmpty())
                    {
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

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


        //Add Json Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category_id", "38");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getAllproducts(jsonObject);

        if(MedicoboxApp.onGetCartID().isEmpty())
        {
            AttemptGetCartId();
        }

        //set cart value
        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            rlayout_cart.setVisibility(View.GONE);
        }
        else {
            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }
    @OnClick(R.id.rlayout_cart)
    public void showCart()
    {
        startActivity(new Intent(this,CartActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }

    private void getAllproducts(JSONObject jsonObject) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            AndroidNetworking.post(GETPRODUCT)
                    .addJSONObjectBody(jsonObject)
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

                                if (getAllProductList != null) {
                                    mListModelArray.clear();
                                    for (int i = 0; i < getAllProductList.length(); i++) {
                                        String id = getAllProductList.getJSONObject(i).get("id").toString();
                                        String sku = getAllProductList.getJSONObject(i).get("sku").toString();
                                        String title = getAllProductList.getJSONObject(i).get("title").toString();
                                        String price = getAllProductList.getJSONObject(i).get("price").toString();
                                        String imageUrl = getAllProductList.getJSONObject(i).get("image").toString();
                                        String sales_price=getAllProductList.getJSONObject(i).get("sale_price").toString();
                                        String short_description=getAllProductList.getJSONObject(i).get("short_description").toString();

                                        ListModel listModel = new ListModel(id, sku, title, price,sales_price,short_description,imageUrl);
                                        listModel.setId(id);
                                        listModel.setSku(sku);
                                        listModel.setTitle(title);
                                        listModel.setPrice(price);
                                        listModel.setSale_price(sales_price);
                                        listModel.setShort_description(short_description);
                                        listModel.setImage(imageUrl);
                                        mListModelArray.add(listModel);
                                    }
                                }
                                CustomProgressDialog.getInstance().dismissDialog();
                                layoutManager = new LinearLayoutManager(mContext);
                                rc_medicine_list.setLayoutManager(new LinearLayoutManager(ListActivity.this, LinearLayoutManager.VERTICAL, false));
                                rc_medicine_list.setHasFixedSize(true);
                                rc_medicine_list.setAdapter(new ListAdapter(mContext, mListModelArray));


                            } catch (JSONException e) {
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
    }


        private void AttemptGetCartId() {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
           // mAlert.onShowProgressDialog(this, true);
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
    }

}

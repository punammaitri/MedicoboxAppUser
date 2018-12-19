package com.aiprous.medicobox.activity;

import android.content.Context;
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
import com.aiprous.medicobox.adapter.MainCategoryAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.MainCategoryModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.UPDATEDGETCATEGORY;


public class MainCategoryActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_main_category)
    RecyclerView rc_main_category;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<MainCategoryModel> mMainCategoryArrayList=new ArrayList<>();
    private Context mContext = this;
    ArrayList<MainCategoryModel.SubCat> lSubCatArrayList;
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        rlayout_cart = (RelativeLayout) findViewById(R.id.rlayout_cart);
        tv_cart_size = (TextView) findViewById(R.id.tv_cart_size);
        searchview_medicine.setFocusable(false);
        //set cart value
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
            rlayout_cart.setVisibility(View.VISIBLE);
        }
        //get call main category
        CallGetCategory();
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }

    private void CallGetCategory() {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(UPDATEDGETCATEGORY)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {

                            JSONObject jsonObj = new JSONObject(response.toString());
                            JSONArray lResponseArray = jsonObj.getJSONArray("response");

                            for(int i=0; i<lResponseArray.length(); i++) {
                                JSONObject jsonObjCat = lResponseArray.getJSONObject(i);

                                String lcategoryName = jsonObjCat.getString("categoryName").toString();
                                String lCategoryId = jsonObjCat.getString("CategoryId").toString();
                                String limage_url = jsonObjCat.getString("image_url").toString();


                                Object SubCategory = jsonObjCat.get("SubCategory");
                                if (SubCategory instanceof JSONArray) {
                                    // It's an array

                                    // getting inner array SubCategory
                                    JSONArray lSubCategoryArray = jsonObjCat.getJSONArray("SubCategory");

                                    //fetch suncategory items
                                    lSubCatArrayList= new ArrayList<>();
                                    for(int j=0; j<lSubCategoryArray.length(); j++) {
                                        JSONObject json = lSubCategoryArray.getJSONObject(j);
                                        String lsubcategoryName=json.getString("categoryName").toString();
                                        String lsubcategoryID=json.getString("CategoryId").toString();
                                        String lsubcategoryurl=json.getString("image_url").toString();


                                        MainCategoryModel.SubCat lsubCat= new MainCategoryModel.SubCat(lsubcategoryName,lsubcategoryID,lsubcategoryurl);
                                        lsubCat.setCategoryName(lsubcategoryName);
                                        lsubCat.setCategoryId(lsubcategoryID);
                                        lsubCat.setImage_url(lsubcategoryurl);
                                        lSubCatArrayList.add(lsubCat);

                                    }

                                }

                                MainCategoryModel lmainCategoryModel=new MainCategoryModel(lcategoryName,lCategoryId,limage_url,lSubCatArrayList);
                                lmainCategoryModel.setCategoryName(lcategoryName);
                                lmainCategoryModel.setCategoryId(lCategoryId);
                                lmainCategoryModel.setImage_url(limage_url);
                                lmainCategoryModel.setSubCats(lSubCatArrayList);
                                mMainCategoryArrayList.add(lmainCategoryModel);
                            }

                            layoutManager = new LinearLayoutManager(mContext);
                            rc_main_category.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rc_main_category.setHasFixedSize(true);
                            rc_main_category.setAdapter(new MainCategoryAdapter(mContext, mMainCategoryArrayList));

                            CustomProgressDialog.getInstance().dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(MainCategoryActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

}

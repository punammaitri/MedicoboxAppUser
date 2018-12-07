package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.MainCategoryAdapter;
import com.aiprous.medicobox.adapter.NotificationAdapter;
import com.aiprous.medicobox.adapter.ProductListAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.model.MainCategoryModel;
import com.aiprous.medicobox.model.ProductsModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;
import static com.aiprous.medicobox.utils.APIConstant.GETUSERINFO;
import static com.aiprous.medicobox.utils.APIConstant.UPDATEDGETCATEGORY;


public class MainCategoryActivity extends AppCompatActivity {

    @BindView(R.id.rc_main_category)
    RecyclerView rc_main_category;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<MainCategoryModel> mMainCategoryArrayList=new ArrayList<>();
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //get call main category
        CallGetCategory();
    }



    private void CallGetCategory() {
        AndroidNetworking.get(UPDATEDGETCATEGORY)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
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

                            for(int i=0;i<getAllProductList.length();i++)
                            {
                                String lcategoryName=getAllProductList.getJSONObject(i).get("categoryName").toString();
                                String lCategoryId=getAllProductList.getJSONObject(i).get("CategoryId").toString();
                                String limage_url=getAllProductList.getJSONObject(i).get("image_url").toString();
                                JsonArray lsubCategory = ((JsonObject) getAllProductList.get(i)).get("SubCategory").getAsJsonArray();

                                MainCategoryModel lmainCategoryModel=new MainCategoryModel(lcategoryName,lCategoryId,limage_url,lsubCategory);
                                lmainCategoryModel.setCategoryName(lcategoryName);
                                lmainCategoryModel.setCategoryId(lCategoryId);
                                lmainCategoryModel.setImage_url(limage_url);
                                lmainCategoryModel.setSubCategory(lsubCategory);
                                mMainCategoryArrayList.add(lmainCategoryModel);
                            }
                            layoutManager = new LinearLayoutManager(mContext);
                            rc_main_category.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rc_main_category.setHasFixedSize(true);
                            rc_main_category.setAdapter(new MainCategoryAdapter(mContext, mMainCategoryArrayList));


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

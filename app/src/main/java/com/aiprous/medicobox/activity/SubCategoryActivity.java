package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.SubCategoryAdapter;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.MainCategoryModel;
import com.aiprous.medicobox.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubCategoryActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    AutoCompleteTextView searchview_medicine;
    @BindView(R.id.rc_subcategory)
    RecyclerView rc_subcategory;
    @BindView(R.id.tv_main_category_name)
    TextView tv_main_category_name;
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
     ArrayList<MainCategoryModel.SubCat> subCatArrayList=new ArrayList<>();
    public static RelativeLayout rlayout_cart;
    public static TextView tv_cart_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rlayout_cart = (RelativeLayout) findViewById(R.id.rlayout_cart);
        tv_cart_size = (TextView) findViewById(R.id.tv_cart_size);
        searchview_medicine.setFocusable(false);
        //set cart value
        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.VISIBLE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
            rlayout_cart.setVisibility(View.VISIBLE);
        }

        rlayout_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CartActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        if (getIntent().getStringExtra("SubCategoryArray") != null) {
            Gson gson = new Gson();
            String dataModel = getIntent().getStringExtra("SubCategoryArray");
            Type listOfItemData = new TypeToken<List<MainCategoryModel.SubCat>>() {
            }.getType();

            subCatArrayList = gson.fromJson(dataModel, listOfItemData);
            tv_main_category_name.setText(getIntent().getStringExtra("MainCategoryName"));
        }

        layoutManager = new LinearLayoutManager(mContext);
        rc_subcategory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rc_subcategory.setHasFixedSize(true);
        rc_subcategory.setAdapter(new SubCategoryAdapter(mContext, subCatArrayList));

    }


    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }

    @OnClick(R.id.searchview_medicine)
    public void onClicksearch()
    {
        startActivity(new Intent(this,SearchViewActivity.class));
    }
}

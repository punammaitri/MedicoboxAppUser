package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.adapter.ProductDetailsAdapter;
import com.aiprous.medicobox.adapter.ViewPagerAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.model.ListModel;
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
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.activity.ListActivity.rlayout_cart;
import static com.aiprous.medicobox.activity.ListActivity.tv_cart_size;
import static com.aiprous.medicobox.utils.APIConstant.ADDTOCART;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.EDITCARTITEM;
import static com.aiprous.medicobox.utils.APIConstant.GETPRODUCT;
import static com.aiprous.medicobox.utils.APIConstant.SINGLEPRODUCT;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.rv_people_also_viewed)
    RecyclerView rv_people_also_viewed;
    @BindView(R.id.tv_product_mrp)
    TextView tv_product_mrp;
    @BindView(R.id.tv_product_price)
    TextView tv_product_price;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.btn_add_to_cart)
    Button btn_add_to_cart;
    @BindView(R.id.rlayout_plus_minus)
    RelativeLayout rlayout_plus_minus;
    @BindView(R.id.tv_value)
    TextView tv_value;
    ArrayList<HomeFragment.Product> mlistModelsArray = new ArrayList<>();
    private Context mcontext = this;
    private int dotscount;
    private ImageView[] dots;

    private String mProductId;
    private String mVisibiltyFlag;
    private String mCartId;
    private String mSku;
    private int mQty;
    private String mItemId;
    private ArrayList<AddToCartOptionDetailModel> ItemModelList;
    boolean foundduplicateItem;
    private String mImageURL;
    private String mMedicineName;
    private String mValue;
    int total = 0;
    private String mMrp;
    private String mdiscount;
    private String mPrice;
    int setValuePosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        //remove back slash
        String getCartId = MedicoboxApp.onGetCartID();
        String lCartId=getCartId;
        mCartId=lCartId.replace("\"", "");

        tv_product_mrp.setText(mcontext.getResources().getString(R.string.Rs) + "150.00");
        tv_product_mrp.setPaintFlags(tv_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_product_price.setText(mcontext.getResources().getString(R.string.Rs) + "135.00");
        //set view pager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            SliderDots.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //add static data
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle, "ABC", "150", "30%", "135"));


        //set adapter
        rv_people_also_viewed.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
        rv_people_also_viewed.setHasFixedSize(true);
        rv_people_also_viewed.setAdapter(new ProductDetailsAdapter(mcontext, mlistModelsArray));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            rlayout_cart.setVisibility(View.GONE);
        }
        else {
            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
        }

        if(getIntent().getStringExtra("productId")!=null)
        {
            mProductId=getIntent().getStringExtra("productId");
            mVisibiltyFlag=getIntent().getStringExtra("VisibiltyFlag");
            mSku=getIntent().getStringExtra("SKU");
            mQty= Integer.parseInt(getIntent().getStringExtra("Qty"));
            mImageURL=getIntent().getStringExtra("imageUrl");
            mMedicineName=getIntent().getStringExtra("MedicineName");
            mValue=getIntent().getStringExtra("value");
            mPrice=getIntent().getStringExtra("price");

            tv_value.setText(""+mQty);
            getSingleproducts(mProductId);
        }

        if(mVisibiltyFlag.equals("1")){
            rlayout_plus_minus.setVisibility(View.VISIBLE);
            btn_add_to_cart.setClickable(false);
            btn_add_to_cart.setBackgroundColor(Color.parseColor("#808080"));

        }else {
            rlayout_plus_minus.setVisibility(View.GONE);
            btn_add_to_cart.setClickable(true);
            btn_add_to_cart.setBackgroundColor(Color.parseColor("#1f2c4c"));
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart()
    {
        startActivity(new Intent(this,CartActivity.class));
    }

    @OnClick(R.id.llayout_product_detail)
    public void onClickProductDetail() {
        startActivity(new Intent(this, ProductDescriptionActivity.class));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail() {
        finish();
    }

    private void getSingleproducts(String productId) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressDialog.getInstance().showDialog(mcontext, "", APIConstant.PROGRESS_TYPE);
            AndroidNetworking.get(SINGLEPRODUCT+productId)
                    .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                           // Toast.makeText(mcontext, response.toString(), Toast.LENGTH_SHORT).show();
                            CustomProgressDialog.getInstance().dismissDialog();
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

    @OnClick(R.id.btn_add_to_cart)
    public void onClickAddTocart()
    {

        //call guest add to cart api
        try {

            setValuePosition = Integer.parseInt(tv_value.getText().toString()) + 1;
            mQty=setValuePosition;
            tv_value.setText("" + setValuePosition);

            JSONObject object = new JSONObject();
            object.put("quote_id",mCartId);
            object.put("sku", mSku);
            object.put("qty", tv_value.getText());

            //Add Json Object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cart_item", object);

            AttemptAddToCart(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_plus)
    public void onClickPlus()
    {
        setValuePosition = Integer.parseInt(tv_value.getText().toString()) + 1;
        mQty=setValuePosition;
        tv_value.setText("" + setValuePosition);

        SingletonAddToCart singletonAddToCart = SingletonAddToCart.getGsonInstance();
        ItemModelList = singletonAddToCart.getOptionList();
        //call edit cart api
        try {
            for(int i=0;i<ItemModelList.size();i++)
            {
                if(mMedicineName.equals(ItemModelList.get(i).getMedicineName()))
                {
                    mItemId=ItemModelList.get(i).getItem_id();
                }
            }

            JSONObject object = new JSONObject();
            object.put("quote_id",mCartId);
            object.put("item_id", mItemId);
            object.put("qty", mQty);


            //Add Json Object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cart_item", object);

            callEditCartItem(jsonObject,MedicoboxApp.onGetAuthToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_minus)
    public void onClickMinus() {

         setValuePosition = Integer.parseInt(tv_value.getText().toString());
         if (setValuePosition != 1) {
            --setValuePosition;
            tv_value.setText("" + setValuePosition);
            mQty = setValuePosition;

            // AddItemsToCart();
            //call edit cart api
            try {

                SingletonAddToCart singletonAddToCart = SingletonAddToCart.getGsonInstance();
                ItemModelList = singletonAddToCart.getOptionList();

                for(int i=0;i<ItemModelList.size();i++)
                {
                    if(mMedicineName.equals(ItemModelList.get(i).getMedicineName()))
                    {
                        mItemId=ItemModelList.get(i).getItem_id();
                    }
                }

                JSONObject object = new JSONObject();
                object.put("quote_id", mCartId);
                object.put("item_id", mItemId);
                object.put("qty", mQty);


                //Add Json Object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cart_item", object);


                callEditCartItem(jsonObject, MedicoboxApp.onGetAuthToken());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //Add to cart API
    private void AttemptAddToCart(JSONObject jsonObject) {
        // mAlert.onShowProgressDialog(this, true);
        if (!isNetworkAvailable(mcontext)) {
            Toast.makeText(mcontext, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            AndroidNetworking.post(ADDTOCART)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            rlayout_plus_minus.setVisibility(View.VISIBLE);
                            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
                            rlayout_cart.setVisibility(View.VISIBLE);
                            try{
                                mItemId = response.getString("item_id");

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseActivity.printLog("response-success : ", response.toString());
                            //save item id into itemid variable
                            addItemsSingleton();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    private void callEditCartItem(JSONObject jsonObject, String bearerToken) {

        if (!isNetworkAvailable(mcontext)) {
            Toast.makeText(mcontext, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressDialog.getInstance().showDialog(mcontext, "", APIConstant.PROGRESS_TYPE);
            AndroidNetworking.put(EDITCARTITEM+mItemId)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders(Authorization, BEARER + bearerToken)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            try{
                                mItemId = response.getString("item_id");

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseActivity.printLog("response-success : ", response.toString());
                            //save item id into itemid variable
                            addItemsSingleton();
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            // Toast.makeText(mContext, "Failed to load data", Toast.LENGTH_SHORT).show();
                            CustomProgressDialog.getInstance().dismissDialog();
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    private void addItemsSingleton() {
        SingletonAddToCart singletonOptionData = SingletonAddToCart.getGsonInstance();
        ItemModelList = singletonOptionData.getOptionList();

        if (ItemModelList != null && !ItemModelList.isEmpty()) {
            Iterator<AddToCartOptionDetailModel> iterator = ItemModelList.iterator();
            foundduplicateItem = false;

            while (iterator.hasNext()) {
                AddToCartOptionDetailModel tempObj = iterator.next();
                if (tempObj.getMedicineName() != null && mMedicineName != null && tempObj.getMedicineName().equalsIgnoreCase(mMedicineName)) {
                    //  tempObj.setPrice("" + total);
                    tempObj.setQty("" + mQty);
                    // tempObj.setUrl(image_url);
                    foundduplicateItem = true;
                }
            }
            if (!foundduplicateItem) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty,mSku,mItemId);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setItem_id(mItemId);
                singletonOptionData.option.add(md);
            } else if (foundduplicateItem && mQty == 0 && total == 0) {

                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getQty().equals("0")) {
                        ItemModelList.remove(i);
                        if(!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()){
                            //this is for make cart icon visible
                            rlayout_cart.setVisibility(View.VISIBLE);
                        }
                        else {
                           // rlayout_cart.setVisibility(View.GONE);
                        }
                    }
                }

            }
        } else {
            if (mQty != 0) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty,mSku,mItemId);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setItem_id(mItemId);
                singletonOptionData.option.add(md);
            }
        }

       // String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        //tv_cart_size.setText(cart_size);
        // calculateTotalPrice();

    }








}

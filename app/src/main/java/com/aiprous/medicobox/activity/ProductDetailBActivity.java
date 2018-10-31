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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.SubstitutesProductAdapter;
import com.aiprous.medicobox.adapter.ViewPagerAdapter;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.ADDTOCART;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.EDITCARTITEM;
import static com.aiprous.medicobox.utils.APIConstant.SINGLEPRODUCT;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ProductDetailBActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.rv_substitute_product)
    RecyclerView rv_substitute_product;
    @BindView(R.id.tv_medicine_contains)
    TextView tv_medicine_contains;
    @BindView(R.id.spinner_count)
    Spinner spinner_count;
    @BindView(R.id.tv_mrp_price)
    TextView tv_mrp_price;
    @BindView(R.id.tv_per_tablet_price)
    TextView tv_per_tablet_price;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.tv_value)
    TextView tv_value;
    @BindView(R.id.rlayout_plus_minus)
    RelativeLayout rlayout_plus_minus;
    @BindView(R.id.btn_add_to_cart)
    Button btn_add_to_cart;


    ArrayList<SubstituteProductModel> substituteProductModelArrayList = new ArrayList<>();
    private Context mcontext = this;
    private int dotscount;
    private ImageView[] dots;
    String[] mValueStrip = {"1 Strip", "2 Strip", "3 Strip", "4 Strip", "5 Strip"};

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
        setContentView(R.layout.activity_product_detail_b);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_mrp_price.setText(mcontext.getResources().getString(R.string.Rs) + "68.60");
        tv_per_tablet_price.setText("(" + mcontext.getResources().getString(R.string.Rs) + "6.86/Tablet SR" + ")");

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);


        //remove back slash
        String getCartId = MedicoboxApp.onGetCartID();
        String lCartId=getCartId;
        mCartId=lCartId.replace("\"", "");

        //set spinner
        //Creating the ArrayAdapter instance having the value list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mValueStrip);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_count.setAdapter(aa);
        //add underline to text
        tv_medicine_contains.setPaintFlags(tv_medicine_contains.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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


        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's", "By Abbott", 50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's", "By Abbott", 50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's", "By Abbott", 50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's", "By Abbott", 50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's", "By Abbott", 50));

        //set adapter
        rv_substitute_product.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rv_substitute_product.setHasFixedSize(true);
        rv_substitute_product.setAdapter(new SubstitutesProductAdapter(mcontext, substituteProductModelArrayList));


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

    public class SubstituteProductModel {
        public String name;
        public String company;
        public int price;

        public SubstituteProductModel(String name, String company, int price) {
            this.name = name;
            this.company = company;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
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
                          //  Toast.makeText(mcontext, response.toString(), Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.llayout_drug_information)
    public void onClickDrugInfo()
    {
        startActivity(new Intent(this,DrugInformationTabActivity.class));
    }

}

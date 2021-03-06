package com.aiprous.medicobox.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.model.CartModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.activity.CartActivity.nestedscroll_cart;
import static com.aiprous.medicobox.activity.CartActivity.relMainView;
import static com.aiprous.medicobox.activity.CartActivity.tv_cart_empty;
import static com.aiprous.medicobox.activity.CartActivity.tv_cart_size;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.DELETECARTITEMS;
import static com.aiprous.medicobox.utils.APIConstant.EDITCARTITEM;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<CartModel.Response> mCartArrayList;
    private CartActivity mContext;
    private ArrayList<AddToCartOptionDetailModel> ItemModelList;
    boolean foundduplicateItem;
    int setValuePosition = 1;
    int total = 0;
    private String mMedicineName;
    private String mValue;
    private String mMrp;
    private String mdiscount;
    private String mPrice;
    int mQty;
    String mImageURL;
    public float mTotalMRPprice = 0;
    private float mTotalPrice = 0;
    private String mSku;
    private String mItemId;
    CustomProgressDialog mAlert;
    private float mCalculatePrice;
    private ShowPrescriptionInterface mShowPrescriptionInterface;

    public CartAdapter(CartActivity mContext, ArrayList<CartModel.Response> mCartArrayList) {
        this.mContext = mContext;
        this.mCartArrayList = mCartArrayList;
        this.mShowPrescriptionInterface = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        mAlert = CustomProgressDialog.getInstance();
        holder.tv_item_name.setText(mCartArrayList.get(position).getName());
        holder.tv_medicine_contains.setText(mCartArrayList.get(position).getShort_description());
        holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + mCartArrayList.get(position).getPrice());

        if (mCartArrayList.get(position).getPrice().equals("") || mCartArrayList.get(position).getPrice().equals("0")) {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) +"0");
        } else {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mCartArrayList.get(position).getPrice());
        }


     /*   if (mCartArrayList.get(position).getSale_price().isEmpty()) {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mCartArrayList.get(position).getPrice());
        } else {
            holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mCartArrayList.get(position).getPrice());
        }*/

        if (mCartArrayList.get(position).getPrescription_req() == 1) {
            mContext.showPrescriptionUpload(1);
        }

        //set number of items
        holder.tv_value.setText("" + mCartArrayList.get(position).getQty());

        //Add to cart
        mMedicineName = mCartArrayList.get(position).getName();
        mValue = mCartArrayList.get(position).getShort_description();
        mMrp = String.valueOf(mCartArrayList.get(position).getPrice());
        mdiscount = String.valueOf(mCartArrayList.get(position).getDiscount());

        if (mCartArrayList.get(position).getSale_price().isEmpty()) {
            mPrice = String.valueOf(mCartArrayList.get(position).getPrice());
        } else {
            mPrice = String.valueOf(mCartArrayList.get(position).getSale_price());
        }
        mImageURL = mCartArrayList.get(position).getImage();
        mQty = Integer.parseInt(holder.tv_value.getText().toString());
        mCalculatePrice = mQty * Float.parseFloat(mPrice);

        AddItemsToSingleton();


        if (position == getItemCount() - 1) {
            holder.view_cart_item.setVisibility(View.GONE);
        }

        holder.tv_plus.setTag(position);
        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lItemIndex = Integer.parseInt("" + v.getTag());
                mMedicineName = mCartArrayList.get(lItemIndex).getName();
                mValue = mCartArrayList.get(lItemIndex).getShort_description();
                mMrp = String.valueOf(mCartArrayList.get(lItemIndex).getPrice());
                mdiscount = String.valueOf(mCartArrayList.get(lItemIndex).getDiscount());
                if (String.valueOf(mCartArrayList.get(lItemIndex).getSale_price()).isEmpty()) {
                    mPrice = String.valueOf(mCartArrayList.get(lItemIndex).getPrice());
                } else {
                    mPrice = String.valueOf(mCartArrayList.get(lItemIndex).getSale_price());
                }


                mCalculatePrice = mQty + Float.parseFloat(mPrice);

                mImageURL = mCartArrayList.get(lItemIndex).getImage();
                mSku = mCartArrayList.get(lItemIndex).getSku();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) + 1;
                mQty = setValuePosition;
                holder.tv_value.setText("" + setValuePosition);
                //call edit cart api
                try {

                    JSONObject object = new JSONObject();
                    object.put("quote_id", mCartArrayList.get(lItemIndex).getQuote_id());
                    object.put("item_id", mCartArrayList.get(lItemIndex).getItem_id());
                    object.put("qty", mQty);

                    //Add Json Object
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cart_item", object);

                    mItemId = String.valueOf(mCartArrayList.get(lItemIndex).getItem_id());
                    if (!isNetworkAvailable(mContext)) {
                        CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                    } else {
                        callEditCartItem(jsonObject, MedicoboxApp.onGetAuthToken());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.tv_minus.setTag(position);
        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lItemIndex = Integer.parseInt("" + v.getTag());
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString());
                if (setValuePosition != 0) {
                    --setValuePosition;
                    holder.tv_value.setText("" + setValuePosition);
                    mQty = setValuePosition;
                    mMedicineName = mCartArrayList.get(lItemIndex).getName();
                    mValue = mCartArrayList.get(lItemIndex).getShort_description();
                    mMrp = String.valueOf(mCartArrayList.get(lItemIndex).getPrice());
                    mdiscount = String.valueOf(mCartArrayList.get(lItemIndex).getDiscount());
                    if (String.valueOf(mCartArrayList.get(lItemIndex).getSale_price()).isEmpty()) {
                        mPrice = String.valueOf(mCartArrayList.get(lItemIndex).getPrice());
                    } else {
                        mPrice = String.valueOf(mCartArrayList.get(lItemIndex).getSale_price());
                    }
                    mCalculatePrice = mQty * Float.parseFloat(mPrice);
                    mImageURL = mCartArrayList.get(lItemIndex).getImage();
                    mSku = mCartArrayList.get(lItemIndex).getSku();
                    if (holder.tv_value.getText().equals("0")) {

                    }

                    //call edit cart api
                    try {
                        JSONObject object = new JSONObject();
                        object.put("quote_id", mCartArrayList.get(lItemIndex).getQuote_id());
                        object.put("item_id", mCartArrayList.get(lItemIndex).getItem_id());
                        object.put("qty", mQty);

                        //Add Json Object
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("cart_item", object);
                        mItemId = String.valueOf(mCartArrayList.get(lItemIndex).getItem_id());
                        if (!isNetworkAvailable(mContext)) {
                            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                        } else {
                            callEditCartItem(jsonObject, MedicoboxApp.onGetAuthToken());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        holder.rlayout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mItemId = String.valueOf(mCartArrayList.get(position).getItem_id());
                //call delete api
                if (!isNetworkAvailable(mContext)) {
                    CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
                } else {
                    AttemptDeleteCartItem();
                }

                SingletonAddToCart singletonOptionData = SingletonAddToCart.getGsonInstance();
                ItemModelList = singletonOptionData.getOptionList();
                if (!ItemModelList.isEmpty()) {
                    for (int i = 0; i < ItemModelList.size(); i++) {
                        if (ItemModelList.get(i).getMedicineName().equals(mCartArrayList.get(position).getName())) {
                            ItemModelList.remove(i);
                            //notifyDataSetChanged();
                            if (!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
                                //this is for make cart icon visible
                                CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                                relMainView.setVisibility(View.GONE);
                                nestedscroll_cart.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                }

                for (int j = 0; j < mCartArrayList.size(); j++) {
                    String lItemId = String.valueOf(mCartArrayList.get(j).getItem_id());
                    if (mItemId.equals(lItemId)) {
                        mCartArrayList.remove(j);
                        notifyDataSetChanged();
                        break;
                    }
                }

                if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
                    CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                    relMainView.setVisibility(View.VISIBLE);
                    nestedscroll_cart.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (mCartArrayList == null) ? 0 : mCartArrayList.size();
    }

    private void AddItemsToSingleton() {
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
                    tempObj.setCalculatePrice(mCalculatePrice);
                    // tempObj.setUrl(image_url);
                    foundduplicateItem = true;
                }
            }
            if (!foundduplicateItem) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount, mPrice, "" + mQty, mSku, mItemId, mCalculatePrice);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setImage(mItemId);
                md.setCalculatePrice(mCalculatePrice);
                singletonOptionData.option.add(md);
            } else if (foundduplicateItem && mQty == 0 && total == 0) {

                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getQty().equals("0")) {
                        ItemModelList.remove(i);
                        if (!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
                            //this is for make cart icon visible
                            CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                            relMainView.setVisibility(View.GONE);
                            nestedscroll_cart.setVisibility(View.VISIBLE);
                        } else {

                            CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                            relMainView.setVisibility(View.VISIBLE);
                            nestedscroll_cart.setVisibility(View.GONE);
                        }
                    }

                }
                notifyDataSetChanged();
            }
        } else {
            if (mQty != 0) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount, mPrice, "" + mQty, mSku, mItemId, mCalculatePrice);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setItem_id(mItemId);
                md.setCalculatePrice(mCalculatePrice);
                singletonOptionData.option.add(md);
            }
        }

        String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        tv_cart_size.setText(cart_size);
        calculateTotalPrice();

    }

    //To calculate the total
    public void calculateTotalPrice() {
        mTotalMRPprice = 0;
        mTotalPrice = 0;
        if (ItemModelList != null) {
            Iterator<AddToCartOptionDetailModel> iterator = ItemModelList.iterator();
            while (iterator.hasNext()) {
                AddToCartOptionDetailModel tempObj = iterator.next();
                if (tempObj.getMrp() != null && !tempObj.getMrp().isEmpty()) {
                    try {
                        Float lqty = Float.valueOf(tempObj.getQty());
                        String tempMRPCost = tempObj.getMrp();
                        Float tempPrice = tempObj.getCalculatePrice();
                        mTotalMRPprice = mTotalMRPprice + (Float.parseFloat(tempMRPCost) * lqty);
                        mTotalPrice = mTotalPrice + tempPrice;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            CartActivity.tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs) + "" + mTotalMRPprice);
            Float lPriceDiscount = mTotalMRPprice - mTotalPrice;

           /*  float lprice=mTotalMRPprice-mTotalPrice;
             Float getTotalDiscount = (lprice / mTotalMRPprice) * 100;*/

            CartActivity.tv_price_discount.setText(mContext.getResources().getString(R.string.Rs) + "" + lPriceDiscount);
            CartActivity.tv_to_be_paid.setText(mContext.getResources().getString(R.string.Rs) + "" + mTotalPrice);
            CartActivity.tv_total_saving.setText(mContext.getResources().getString(R.string.Rs) + "" + lPriceDiscount);
        }
    }

    private void AttemptDeleteCartItem() {
        AndroidNetworking.delete(DELETECARTITEMS + mItemId)
                .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        CustomProgressDialog.getInstance().dismissDialog();
                        // Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                        if (response.toString().equals("true")) {
                            Toast.makeText(mContext, "Product deleted successfully", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                        Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                    }
                });
    }

    private void callEditCartItem(JSONObject jsonObject, String bearerToken) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.put(EDITCARTITEM + mItemId)
                .addJSONObjectBody(jsonObject)
                .addHeaders(Authorization, BEARER + bearerToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            mItemId = response.getString("item_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        BaseActivity.printLog("response-success : ", response.toString());
                        //save item id into itemid variable
                        AddItemsToSingleton();
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_medicine_contains)
        TextView tv_medicine_contains;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_cart_item)
        View view_cart_item;
        @BindView(R.id.tv_minus)
        TextView tv_minus;
        @BindView(R.id.tv_plus)
        TextView tv_plus;
        @BindView(R.id.tv_value)
        TextView tv_value;
        @BindView(R.id.rlayout_delete)
        RelativeLayout rlayout_delete;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ShowPrescriptionInterface {
        public void showPrescriptionUpload(int value);
    }
}
